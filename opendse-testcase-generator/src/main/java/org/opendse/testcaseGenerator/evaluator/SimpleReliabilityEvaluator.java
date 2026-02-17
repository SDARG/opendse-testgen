package org.opendse.testcaseGenerator.evaluator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jreliability.bdd.BDDProvider;
import org.jreliability.bdd.BDDProviderFactory;
import org.jreliability.bdd.BDDTTRF;
import org.jreliability.bdd.javabdd.JBDDProviderFactory;
import org.jreliability.booleanfunction.common.ANDTerm;
import org.jreliability.booleanfunction.common.LiteralTerm;
import org.jreliability.evaluator.MomentEvaluator;
import org.jreliability.function.ReliabilityFunction;
import org.jreliability.function.common.ExponentialReliabilityFunction;
import org.jreliability.function.common.SimpleFunctionTransformer;
import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Element;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;
import net.sf.opendse.optimization.ImplementationEvaluator;

public class SimpleReliabilityEvaluator implements ImplementationEvaluator {

	protected final Objective objective;

	protected int priority;

	protected static final String MTTF = "mttf";

	public SimpleReliabilityEvaluator(int priority, boolean min) {
		super();

		this.objective = new Objective(MTTF, min ? Objective.Sign.MIN : Objective.Sign.MAX);
		this.priority = priority;
	}

	@Override
	public Specification evaluate(Specification implementation, Objectives objectives) {

		Architecture<Resource, Link> architecture = implementation.getArchitecture();
		Mappings<Task, Resource> mappings = implementation.getMappings();

		Set<Element> elements = new HashSet<Element>();
		elements.addAll(architecture.getVertices());
		//elements.addAll(architecture.getEdges());
		elements.addAll(mappings.getAll());

		Map<Element, ReliabilityFunction> reliabilityFunctions = new HashMap<Element, ReliabilityFunction>();

		for (Element e : elements) {
			if(e.getAttribute("reliability") != null) {
				double lambda = ((Number) e.getAttribute("reliability")).doubleValue();
				reliabilityFunctions.put(e, new ExponentialReliabilityFunction(lambda));
			}
			
		}
		
		org.apache.commons.collections15.Transformer<Element, ReliabilityFunction> transformer = new SimpleFunctionTransformer<>(reliabilityFunctions);
		//serial structure
		ANDTerm systemTerm = new ANDTerm();
		for(Element e : reliabilityFunctions.keySet()) {
			systemTerm.add(new LiteralTerm<Element>(e));
		}
		
		BDDProviderFactory bddProviderFactory = new JBDDProviderFactory();
		BDDProvider<Element> bddProvider = bddProviderFactory.getProvider();
		BDDTTRF<Element> bddTTRF = new BDDTTRF<>(bddProvider);
		ReliabilityFunction systemReliabilityFunction = bddTTRF.convert(systemTerm, transformer);
		
		MomentEvaluator momentEvaluator = new MomentEvaluator(1);
		Double mttf = momentEvaluator.evaluate(systemReliabilityFunction);

		objectives.add(objective, mttf);

		return null;
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
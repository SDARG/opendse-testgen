package org.opendse.testcaseGenerator.evaluator;

import org.opt4j.core.config.annotations.Order;

import com.google.inject.multibindings.Multibinder;

import net.sf.opendse.optimization.ImplementationEvaluator;
import net.sf.opendse.optimization.evaluator.EvaluatorModule;

public class SimpleReliabilityEvaluatorModule extends EvaluatorModule{

	@Order(2)
	protected int priority = 0;
	@Order(1)
	protected Type type = Type.MAX;
	
	public enum Type {
		MIN,MAX;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	protected void config() {
		SimpleReliabilityEvaluator evaluator = new SimpleReliabilityEvaluator( priority, type == Type.MIN);
		
		Multibinder<ImplementationEvaluator> multibinder = Multibinder.newSetBinder(binder(),
				ImplementationEvaluator.class);
		multibinder.addBinding().toInstance(evaluator);
	}
	
}

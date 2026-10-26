package org.opendse.testcasegenerator;

import org.opendse.testcasegenerator.application.ApplicationBuilder;
import org.opendse.testcasegenerator.architecture.ArchitectureBuilder;
import org.opendse.testcasegenerator.mappings.Mapper;
import org.opendse.testcasegenerator.modelextension.CommunicationResource;
import org.opendse.testcasegenerator.modelextension.FunctionalResource;
import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;

/**
 *  The Specification class manages the creation of the separate parts of a specification
 */
public class SpecificationBuilder {

	protected final ApplicationBuilder aplBuilder;
	protected final ArchitectureBuilder arcBuilder;
	protected final Mapper mapper;
	
	
	@Inject
	public SpecificationBuilder(ApplicationBuilder aplBuilder, ArchitectureBuilder arcBuilder, Mapper mapper, Rand rand) {
		 this.aplBuilder = aplBuilder;
		 this.arcBuilder = arcBuilder;
		 this.mapper = mapper;
		 FunctionalResource.rand = rand;
		 CommunicationResource.rand = rand;
	}
	
	/*
	 * builds and returns the specification
	 */
	public Specification build() {
		
		/*
		 * Builds the architecture according to parameters
		 */
		arcBuilder.build();
		Architecture<Resource, Link> architecture = arcBuilder.build();
		
		/*
		 * Builds the application according to parameters
		 */
		Application<Task, Dependency> application = aplBuilder.build();
		
		/*
		 * Sets all mappings according to parameters
		 */		
		Mappings<Task, Resource> mappings = mapper.map(architecture, application);
		
	
		return new Specification(application, architecture, mappings);
	}
	
}

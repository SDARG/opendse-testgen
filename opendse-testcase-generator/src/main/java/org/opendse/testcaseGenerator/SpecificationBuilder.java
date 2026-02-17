package org.opendse.testcaseGenerator;

import java.util.ArrayList;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.GatewayBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.NoCBuilder;
import org.opendse.testcaseGenerator.modelextension.CommunicationResource;
import org.opendse.testcaseGenerator.modelextension.FunctionalResource;
import org.opendse.testcaseGenerator.modules.ArchitectureModule.ArchitectureType;
import org.opendse.testcaseGenerator.modules.SpecBuilderModule;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomMersenneTwister;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;

/**
 *  The Specification class manages the creation of the separate parts of a specification
 */
public class SpecificationBuilder {

	ApplicationBuilder aplBuilder;
	ArchitectureBuilder arcBuilder;
	Mapper mapper;
	
	
	@Inject
	public SpecificationBuilder(ApplicationBuilder aplBuilder, ArchitectureBuilder arcBuilder, Mapper mapper, Rand rand) {
		 this.aplBuilder = aplBuilder;
		 this.arcBuilder = arcBuilder;
		 this.mapper = mapper;
		 FunctionalResource.rand = rand;
		 CommunicationResource.rand = rand;
	}
	/*
	 * creates the parts of the specification
	 * builds and returns the specification
	 */
	public Specification build() {
		
		/*
		 * Builds the architecture according to parameters
		 */
		arcBuilder.build();
		Architecture<Resource, Link> architecture = arcBuilder.getArchitecture();
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

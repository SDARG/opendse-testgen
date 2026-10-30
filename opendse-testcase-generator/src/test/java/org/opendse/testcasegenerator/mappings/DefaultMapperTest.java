package org.opendse.testcasegenerator.mappings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opendse.testcasegenerator.application.ParallelApplicationBuilder;
import org.opendse.testcasegenerator.architecture.BackboneBuilder;
import org.opendse.testcasegenerator.modelextension.CommunicationResource;
import org.opendse.testcasegenerator.modelextension.FunctionalResource;
import org.opendse.testcasegenerator.modules.ApplicationModule.NumberOfTasks;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomMersenneTwister;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class DefaultMapperTest extends MapperTest{

	private Rand rand = new RandomMersenneTwister(0);
	private DefaultMapper mapper;
	private Architecture<Resource, Link> architecture;
	private Application<Task, Dependency> application;
	
	@BeforeEach
	void setup() {
		CommunicationResource.rand = rand;
		FunctionalResource.rand = rand;
		this.mapper = new DefaultMapper(rand, 0.5, 0.5, 0.5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		BackboneBuilder archBuilder = new BackboneBuilder(4, 20, false, 0, rand);
		this.architecture = archBuilder.build();
		ParallelApplicationBuilder appBuilder = new ParallelApplicationBuilder(rand, NumberOfTasks.normal, 3, 3, 3, 0.5, true, 3);
		this.application=appBuilder.build();
	}
	
	/*
	 * test if the mappings are correct
	 * checks that each task has at least one mapping and no double mappings to same resource
	 * checks if each task is only mapped to corresponding resource type
	 */
	@Test
	void testMappingsCreated() {
		Mappings<Task, Resource> mappings = mapper.map(architecture, application);
		//check that each task has correct number of mappings
		for(Task t : application.getVertices()) {
			if(!(t instanceof Communication)) {
				//check number of mappings correct
				Set<Resource> targets = new HashSet<Resource>(mappings.getTargets(t));
				assertTrue(targets.size() > 0,"each task should have at least one mapping");
				//check target of mappings is correct resource type (sensor tasks -> sensor, actuator task -> actuator, cpu task -> cpu)
				assertTrue(mappedToCorrectResourceType(t, targets),"each task should only be mapped to its corresponding resource type");
			}
		}
	}
	
}

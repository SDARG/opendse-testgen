package org.opendse.testcasegenerator.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opendse.testcasegenerator.modelextension.CommunicationResource;
import org.opendse.testcasegenerator.modelextension.FunctionalResource;
import org.opendse.testcasegenerator.modelextension.Switch;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomMersenneTwister;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

public class BackboneBuilderTest {

	private BackboneBuilder builder;
	private Rand rand = new RandomMersenneTwister(0);
	
	@BeforeEach
	void setup() {
		//build a default backbone with 3 switches, 10 resources, and multiple connections not allowed
		this.builder = new BackboneBuilder(3,10,false,0,rand);
		CommunicationResource.rand = rand;
		FunctionalResource.rand = rand;
	}
	
	/*
	 * tests that size of architecture is correct in terms of number of switches/resources
	 * also checks that all switches are fully connected and each resource only connected to one switch
	 */
	@Test
	void testArchitectureWithoutMulitCOnnectionsCorrect() {
		Architecture<Resource, Link> architecture = builder.build();
		int switchNumber = 0;
		int resourceNumber = 0;
		Set<Switch> switches = new HashSet<Switch>();
		Set<Resource> resources = new HashSet<Resource>();
		for(Resource resource : architecture.getVertices()) {
			if(resource instanceof Switch) {
				switchNumber++;
				switches.add((Switch)resource);
			}
			else {
				resourceNumber++;
				resources.add(resource);
			}
		}
		//check that number of components correct
		assertEquals(3, switchNumber);
		assertEquals(10, resourceNumber);
		
		//check structure of architecture correct
		for(Switch sw : switches) {
			assertTrue(connectedToAllSwitches(sw, switches, architecture));
		}
		
		//check each resource only connected to one switch
		for(Resource r : resources) {
			//just count neighbors since resources not connected to each other, only to switches
			assertEquals(1, architecture.getNeighborCount(r));
		}
	}
	
	/*
	 * tests that size of architecture is correct in terms of number of switches/resources
	 * also checks that all switches are fully connected and number of connections per resource is in defined limits
	 */
	@Test
	void testArchitectureWithMulitCOnnectionsCorrect() {
		BackboneBuilder multiConnectionBuilder = new BackboneBuilder(3, 10, true, 2, rand);
		Architecture<Resource, Link> multiConnectionArchitecture = multiConnectionBuilder.build();
		int switchNumber = 0;
		int resourceNumber = 0;
		Set<Switch> switches = new HashSet<Switch>();
		Set<Resource> resources = new HashSet<Resource>();
		for(Resource resource : multiConnectionArchitecture.getVertices()) {
			if(resource instanceof Switch) {
				switchNumber++;
				switches.add((Switch)resource);
			}
			else {
				resourceNumber++;
				resources.add(resource);
			}
		}
		//check that number of components correct
		assertEquals(3, switchNumber);
		assertEquals(10, resourceNumber);
		
		//check structure of architecture correct
		for(Switch sw : switches) {
			assertTrue(connectedToAllSwitches(sw, switches, multiConnectionArchitecture));
		}
		
		//check each resource only connected to one switch
		for(Resource r : resources) {
			//just count neighbors since resources not connected to each other, only to switches
			int numberOfConnections = multiConnectionArchitecture.getNeighborCount(r);
			assertTrue((numberOfConnections > 0) && (numberOfConnections <= 2));
		}
	}
	
	
	/*
	 * returns true if given switch connected to all switches
	 */
	private boolean connectedToAllSwitches(Switch sw, Set<Switch> switches,Architecture<Resource, Link> architecture) {
		Set<Resource> neighbors = (Set<Resource>) architecture.getNeighbors(sw);
		neighbors.removeIf((Resource r) -> !(r instanceof Switch));
		neighbors.add(sw);
		//just check if both sets are equal, then given switch is connected to all switches
		return neighbors.equals(switches);
	}
	
	/*
	 * test structure correct
	 * 	-> all switches connected to each other
	 * 	-> each resource only connected to one switch
	 *  -> if multi connections allowed check their limit e.g. 2 with 3 switches
	 */
	
	
	
}

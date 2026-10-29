package org.opendse.testcasegenerator.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opendse.testcasegenerator.modelextension.Bus;
import org.opendse.testcasegenerator.modelextension.CommunicationResource;
import org.opendse.testcasegenerator.modelextension.FunctionalResource;
import org.opendse.testcasegenerator.modelextension.Gateway;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomMersenneTwister;

import edu.uci.ics.jung.graph.util.Pair;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

public class GatewayBuilderTest {

	private GatewayBuilder builder;
	private Rand rand = new RandomMersenneTwister(0);
	
	@BeforeEach
	void setup() {
		//build a default gateway with no multibus, 3 buses, and 2 subarchitectures per bus
		this.builder = new GatewayBuilder(false, 3, 2, rand);
		CommunicationResource.rand = rand;
		FunctionalResource.rand = rand;
	}
	
	/*
	 * checks that correct number of buses are created and they are all connected to gateway
	 */
	@Test
	void testCorrectArchitectureStructure() {
		Architecture<Resource, Link> architecture = builder.build();
		
		//first check number of buses and gateways
		int numberOfBuses = 0;
		int numberOfGateways = 0;
		Set<Bus> buses = new HashSet<Bus>();
		Gateway gateway = null;
		for(Resource resource : architecture.getVertices()) {
			if(resource instanceof Bus) {
				numberOfBuses++;
				buses.add((Bus) resource);
			}
			else if(resource instanceof Gateway) {
				numberOfGateways++;
				gateway=(Gateway)resource;
			}
		}
		assertEquals(3, numberOfBuses,"architecture should contain 3 buses");
		assertEquals(1, numberOfGateways,"architecture should contain 1 gateway");
		
		//check all buses connected to gateway and correct number of subarchitectures
		for(Bus bus : buses) {
			Collection<Link> links = architecture.getIncidentEdges(bus);
			assertTrue(busConnectedToGateway(links, gateway, architecture));
			assertTrue(busCOnnectedToCorrectNumberOfSubarchitectures(links, gateway, architecture));
		}
	}
	
	/*
	 * test if the multibus option works correctly which should place 3 buses instead of 1 between gateway and each subarchitecture
	 */
	@Test
	void testMultibusOption() {
		GatewayBuilder multibusBuilder = new GatewayBuilder(true, 3, 2, rand);
		Architecture<Resource, Link> multibusArchitecture = multibusBuilder.build();
		Set<Resource> subarchitectureConnections = getAllSubarchitectureConnections(multibusArchitecture);
		//check for each subarch connection that is has 3 bus neighbors in graph
		for(Resource resource : subarchitectureConnections) {
			assertEquals(3, busesInCollection(multibusArchitecture.getNeighbors(resource)));
		}
	}
	
	/*
	 * returns the number of buses in the given collection of neighbor resources
	 */
	private int busesInCollection(Collection<Resource> neighbors) {
		int busNumber = 0;
		for(Resource neighbor : neighbors) {
			if(neighbor instanceof Bus) {
				busNumber++;
			}
		}
		return busNumber;
	}
	
	/*
	 * returns a set of resources that from the connection between a bus and a subarchitecture
	 */
	private Set<Resource> getAllSubarchitectureConnections(Architecture<Resource, Link> architecture){
		Set<Resource> subarchitectureConnections = new HashSet<Resource>();
		for(Resource resource : architecture.getVertices()) {
			//only check resources that are not gateway or bus
			if(!(resource instanceof Bus) && !(resource instanceof Gateway)) {
				Collection<Link> links = architecture.getIncidentEdges(resource);
				for(Link link : links) {
					Pair<Resource> endpoints = architecture.getEndpoints(link);
					//if one endpoint is a bus then add the other one as subarch connection
					if(endpoints.getFirst() instanceof Bus) {
						subarchitectureConnections.add(endpoints.getSecond());
					}
					else if(endpoints.getSecond() instanceof Bus) {
						subarchitectureConnections.add(endpoints.getFirst());
					}
				}
			}
		}
		return subarchitectureConnections;
	}
	
	/*
	 * checks if in the given links one is connected to gateway in the architecture
	 */
	private boolean busConnectedToGateway(Collection<Link> links, Gateway gateway, Architecture<Resource, Link> architecture) {
		int connectionsToGateway = 0;
		for(Link link : links) {
			if(architecture.getEndpoints(link).contains(gateway)) {
				connectionsToGateway++;
			}
		}
		//should only be connected to gateway once
		return connectionsToGateway == 1;
	}
	
	/*
	 * checks correct number of subarchitctures connected to a bus
	 */
	private boolean busCOnnectedToCorrectNumberOfSubarchitectures(Collection<Link> links, Gateway gateway, Architecture<Resource, Link> architecture) {
		int connectionsToSubarchitectures = 0;
		for(Link link : links) {
			if(!architecture.getEndpoints(link).contains(gateway)) {
				connectionsToSubarchitectures++;
			}
		}
		//should be more than 0 and at most 2 subarchitectures connected to each bus
		return (connectionsToSubarchitectures > 0) && (connectionsToSubarchitectures <= 2);
	}
	
}

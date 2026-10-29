package org.opendse.testcasegenerator.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opendse.testcasegenerator.modelextension.CommInterface;
import org.opendse.testcasegenerator.modelextension.CommunicationResource;
import org.opendse.testcasegenerator.modelextension.FunctionalResource;
import org.opendse.testcasegenerator.modelextension.NoCRouter;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomMersenneTwister;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

public class NoCBuilderTest {
	
	NoCBuilder builder;
	private Rand rand = new RandomMersenneTwister(0);
	
	@BeforeEach
	void setup() {
		//build a 3x3 NoC
		this.builder = new NoCBuilder(3,3,rand);
		CommunicationResource.rand = rand;
		FunctionalResource.rand = rand;
	}
	
	@Test
	void testNoCStructure() {
		Architecture<Resource, Link> architecture = builder.build();
		int NoCRouterNumber = 0;
		Set<CommInterface> interfaces = new HashSet<CommInterface>();
		for(Resource resource : architecture.getVertices()) {
			if(resource instanceof NoCRouter) {
				NoCRouterNumber++;
			}
			else if(resource instanceof CommInterface) {
				interfaces.add((CommInterface)resource);
			}
		}
		//check number of NoC routers correct
		assertEquals(9, NoCRouterNumber,"there should be 9 NoC routers in a 3x3 NoC");
		
		//check that each interface is connected to one NoC router
		for(CommInterface commInterface : interfaces) {
			Collection<Resource> neighbors = architecture.getNeighbors(commInterface);
			neighbors.removeIf((Resource r) -> !(r instanceof NoCRouter));
			assertEquals(1, neighbors.size(),"each interface should only be connected to 1 NoC router");
		}
	}

}

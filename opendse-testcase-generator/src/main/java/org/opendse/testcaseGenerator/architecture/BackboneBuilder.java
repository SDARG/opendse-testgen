package org.opendse.testcaseGenerator.architecture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opendse.testcaseGenerator.modelextension.Actuator;
import org.opendse.testcaseGenerator.modelextension.Cpu;
import org.opendse.testcaseGenerator.modelextension.Sensor;
import org.opendse.testcaseGenerator.modelextension.Switch;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

/**
 * The {@link BackboneBuilder} builds a backbone architecture with a configurable number of fully connected switches and several processing resources connected to them.
 */
public class BackboneBuilder extends ArchitectureBuilder{
	
	
	protected final int switchNumber;
	protected final int resourceNumber;
	protected final boolean multipleConnectionsAllowed;
	protected final int numberOfConnections;	
	protected final Rand rand;
	
	
	@Inject
	public BackboneBuilder(
			@Constant(value = "switchNumber", namespace = BackboneBuilder.class) int switchNumber,
			@Constant(value = "resourceNumber", namespace = BackboneBuilder.class) int resourceNumber,
			@Constant(value = "multipleConnectionsAllowed", namespace = BackboneBuilder.class) boolean multipleConnectionsAllowed,
			@Constant(value = "numberOfConnections", namespace = BackboneBuilder.class) int numberOfConnections,
			Rand rand) {
		this.switchNumber = switchNumber;
		this.resourceNumber = resourceNumber;
		this.multipleConnectionsAllowed=multipleConnectionsAllowed;
		this.numberOfConnections=numberOfConnections;
		this.rand = rand;
	}

	@Override
	public Architecture<Resource, Link> build() {
		//reset counters for the architecture
		this.architecture = new Architecture<Resource, Link>();
		ArchitectureBuilder.resetCounters();
		
		List<Switch> switches = new ArrayList<Switch>();
		
		/*
		 * create the needed switch nodes
		 */
		for(int i=0;i<switchNumber;i++) {
			Switch sw = new Switch("switch"+useSwitchCounter());
			sw.setAttributes();
			architecture.addVertex(sw);
			switches.add(sw);
		}
		
		/*
		 * connect all the switch nodes to each other
		 */
		for(int i=0;i<switches.size();i++) {
			for(int j=i+1;j<switches.size();j++) {
				Link link = new Link("link"+ArchitectureBuilder.useedgeCounter());
				architecture.addEdge(link, switches.get(i), switches.get(j),EdgeType.DIRECTED);
				link = new Link("link"+ArchitectureBuilder.useedgeCounter());
				architecture.addEdge(link, switches.get(j), switches.get(i),EdgeType.DIRECTED);
			}
		}
		
		/*
		 * create cpu nodes and sensors/actuators
		 * 1/8 will be sensors/actuators each and remaining 3/4 will be cpus
		 */
		final int sensorNumber = resourceNumber/8;
		final int actuatorNumber = resourceNumber/8;
		final int cpuNumber = resourceNumber - sensorNumber - actuatorNumber;
		List<Resource> resources = new ArrayList<Resource>();
		//generate sensors
		for(int i=0;i<sensorNumber;i++) {
			Sensor sensor = new Sensor("sensor"+useSensorCounter());
			sensor.setAttributes();
			architecture.addVertex(sensor);
			resources.add(sensor);
		}
		//generate actuators
		for(int i=0;i<actuatorNumber;i++) {
			Actuator actuator = new Actuator("actuator"+useActuatorCounter());
			actuator.setAttributes();
			architecture.addVertex(actuator);
			resources.add(actuator);
		}
		//generate cpus
		for(int i=0;i<cpuNumber;i++) {
			Cpu cpu = new Cpu("cpu"+usecpuCounter());
			cpu.setAttributes();
			architecture.addVertex(cpu);
			resources.add(cpu);
		}
		
		
		if(multipleConnectionsAllowed) {
			/*
			 * connect processing resources to one or multiple switches
			 */
			for(Resource resource : resources) {
				int toConnect = rand.nextInt(1, numberOfConnections);
				if(toConnect >= switches.size()) {
					/*
					 * connect to all switches and go to next resource
					 */
					for(Switch sw : switches) {
						Link link = new Link("link"+ArchitectureBuilder.useedgeCounter());
						architecture.addEdge(link, sw, resource,EdgeType.DIRECTED);
						link = new Link("link"+ArchitectureBuilder.useedgeCounter());
						architecture.addEdge(link, resource, sw,EdgeType.DIRECTED);
					}
					continue;
				}
				Set<Resource> connectedSwitches = new HashSet<Resource>();
				int connected = 0;
				while(connected < toConnect) {
					 //choose next switch at random
					Switch sw = switches.get(rand.nextInt(switches.size()));
					//check if not already connected
					if(!connectedSwitches.contains(sw)) {
						//if not, connect and add switch to set
						connectedSwitches.add(sw);
						Link link = new Link("link"+ArchitectureBuilder.useedgeCounter());
						architecture.addEdge(link, sw, resource,EdgeType.DIRECTED);
						link = new Link("link"+ArchitectureBuilder.useedgeCounter());
						architecture.addEdge(link, resource, sw,EdgeType.DIRECTED);
						connected++;
					}
				}
			}
		}
		else {
			/*
			 * connect processing resources to one switch chosen at random
			 */
			for(Resource resource : resources) {
				Switch sw = switches.get(rand.nextInt(switches.size()));
				Link link = new Link("link"+ArchitectureBuilder.useedgeCounter());
				architecture.addEdge(link, sw, resource,EdgeType.DIRECTED);
				link = new Link("link"+ArchitectureBuilder.useedgeCounter());
				architecture.addEdge(link, resource, sw,EdgeType.DIRECTED);
			}
		}
		return architecture;	
	}

}

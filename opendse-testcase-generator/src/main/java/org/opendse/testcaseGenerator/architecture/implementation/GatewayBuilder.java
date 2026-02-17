package org.opendse.testcaseGenerator.architecture.implementation;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.SubarchitectureBuilder.SubarchitectureType;
import org.opendse.testcaseGenerator.modelextension.Bus;
import org.opendse.testcaseGenerator.modelextension.CANBus;
import org.opendse.testcaseGenerator.modelextension.EthernetBus;
import org.opendse.testcaseGenerator.modelextension.Gateway;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomDefault;
import org.opt4j.core.common.random.RandomMersenneTwister;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
/*
 * The GatewayBuilder is used to create a Gateway Structure  
 */
public class GatewayBuilder  extends ArchitectureBuilder{

	private boolean multibus;
	private int busNumber;
	private int resourceNumber;
	
	Rand rand;
	
	
	@Inject
	public GatewayBuilder(
			@Constant(value = "multibus", namespace = GatewayBuilder.class) boolean multibus,
			@Constant(value = "busNumber", namespace = GatewayBuilder.class) int busNumber,
			@Constant(value = "resourceNumber", namespace = GatewayBuilder.class) int resourceNumber,
			Rand rand) {
		
		this.multibus = multibus;
		this.busNumber = busNumber;
		this.resourceNumber = resourceNumber;
		this.rand = rand;
	}
	
	/*
	 * Creates a Gateway and connects it to the specified number of Buses, then creates up to a specified number of subarchitectures at these Buses
	 */
	@Override
	public void build() {
		//reset counters for the architecture
		this.architecture = new Architecture<Resource, Link>();
		ArchitectureBuilder.resetCounters();
		
		//Creates the Gateway
		Gateway gateway = new Gateway("Gateway");
		gateway.setAttributes();
		architecture.addVertex(gateway);
		ArrayList<Resource> buslist = new ArrayList<Resource>();
		SubarchitectureBuilder builder = new SubarchitectureBuilder(SubarchitectureType.CPU,architecture, buslist, rand);
		
		//Creates the Buses and connects them to the Gateway, if multibus is true 3 different buses will be created at every spot
		for(int j = 0; j< busNumber;j++) {
			buslist = new ArrayList<Resource>();
			if(multibus) {
					Bus bus = new Bus("bus"+usebusCounter());
					bus.setAttributes();
					architecture.addVertex(bus);
					Link link1 = new Link("link"+ArchitectureBuilder.useedgeCounter());
					architecture.addEdge(link1, bus, gateway);
					buslist.add(bus);
					Bus ethernetbus = new EthernetBus("bus"+usebusCounter());
					ethernetbus.setAttributes();
					architecture.addVertex(ethernetbus);
					Link link2 = new Link("link"+ArchitectureBuilder.useedgeCounter());
					architecture.addEdge(link2, ethernetbus, gateway);
					buslist.add(ethernetbus);
					Bus canbus = new CANBus("bus"+usebusCounter());
					canbus.setAttributes();
					architecture.addVertex(canbus);
					Link link3 = new Link("link"+ArchitectureBuilder.useedgeCounter());
					architecture.addEdge(link3, canbus, gateway);
					buslist.add(canbus);
			
			} else {
				Bus bus = new Bus("bus"+usebusCounter());
				bus.setAttributes();
				architecture.addVertex(bus);
				Link link = new Link("link"+ArchitectureBuilder.useedgeCounter());
				architecture.addEdge(link, bus, gateway);
				buslist.add(bus);
			}
			
			
			//Uses the SubarchitectureBuilder to attach functional Resources to the Buses
			builder.setBus(buslist);
			for(int i = 0; i <= rand.nextInt(resourceNumber);i++) {
				switch(rand.nextInt(SubarchitectureType.values().length+1)) {
				case 0:
					builder.setType(SubarchitectureType.CPU);
					break;
				case 1:
					builder.setType(SubarchitectureType.CPU);
					break;
				case 2:
					builder.setType(SubarchitectureType.IO);
					break;
				case 3:
					builder.setType(SubarchitectureType.SENSOR);
					break;
				default:
					builder.setType(SubarchitectureType.CPU);
				break;
				}
				builder.build();
			}
		}
	}
	
	public void setMultibus(boolean multibus) {
			this.multibus = multibus;
	}


}

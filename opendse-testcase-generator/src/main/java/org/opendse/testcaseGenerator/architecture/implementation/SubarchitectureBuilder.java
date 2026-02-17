package org.opendse.testcaseGenerator.architecture.implementation;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.TileBuilder.TileType;
import org.opendse.testcaseGenerator.modelextension.Actuator;
import org.opendse.testcaseGenerator.modelextension.CommInterface;
import org.opendse.testcaseGenerator.modelextension.Cpu;
import org.opendse.testcaseGenerator.modelextension.Gateway;
import org.opendse.testcaseGenerator.modelextension.Sensor;
import org.opt4j.core.common.random.Rand;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
/*
 * The SubarchitectureBuilder creates and attaches a substructure with FunctionalResources to a given Resource
 */
public class SubarchitectureBuilder  extends ArchitectureBuilder{

	
	ArrayList<Resource> currentBuslist;
	Rand rand;
	SubarchitectureType currentType;
	public  enum SubarchitectureType{
		CPU,
		SENSOR,
		IO
	}
	
	public SubarchitectureBuilder(SubarchitectureType type,Architecture<Resource,Link> architecture, ArrayList<Resource> buslist, Rand rand) {
		currentType = type;
		this.architecture = architecture;
		currentBuslist = buslist;
		this.rand = rand;
	}
	
	/*
	 * Builds a Subarchitecture with the currently active Type connected to the Resources in currentBuslist
	 */
	@Override
	public void build() {
			switch(currentType) {
			case CPU: buildCpu();
				break;
			case SENSOR: buildSensors();
				break;
			case IO: buildIO();
				break;
			default: buildCpu();
				break;
			
		}
		
	}
	/*
	 *  Builds a Subarchitecture with up to 8 Cpus connected to all Resource in currentBuslist with a CommInterface
	 */
	public void buildCpu() {
	
		CommInterface cInterface = new CommInterface("CommInterface"+usecInterfaceCounter());
		cInterface.setAttributes();
		architecture.addVertex(cInterface);
		for(Resource bus: currentBuslist) {
			Link link = new Link("l"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(link, cInterface,bus);
			}
		Cpu tempResource;
		Link tempLink;
		for(int i = 0; i <= rand.nextInt(7)+1; i++){
			tempResource = new Cpu("cpu"+usecpuCounter());
			tempResource.setAttributes();
			tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(tempLink, cInterface, tempResource);
		}
	}
	/*
	 *  Builds a Subarchitecture with 4 Sensors connected to a Cpu, that is connected to all Resource in currentBuslist
	 */
	public void buildSensors() {
		
		Cpu cpu = new Cpu("cpu"+usecpuCounter());
		cpu.setAttributes();
		architecture.addVertex(cpu);
		for(Resource bus: currentBuslist) {
			Link link = new Link("l"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(link, cpu,bus);
			}
		Sensor tempResource;
		Link tempLink;
		for(int i = 0; i < 4; i++){
			tempResource = new Sensor("sensor"+useSensorCounter());
			tempResource.setAttributes();
			tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(tempLink, cpu, tempResource);
		}
	}
	/*
	 *  Builds a Subarchitecture with a Sensor and an Actuator connected to a Cpu, that is connected to all Resource in currentBuslist
	 */
	public void buildIO() {
		Cpu cpu = new Cpu("cpu"+usecpuCounter());
		cpu.setAttributes();
		architecture.addVertex(cpu);
		for(Resource bus: currentBuslist) {
		Link link = new Link("l"+ArchitectureBuilder.useedgeCounter());	
		architecture.addEdge(link, cpu,bus);
		}		
		Actuator actuator = new Actuator("actuator"+useActuatorCounter());
		actuator.setAttributes();
		Sensor sensor = new Sensor("sensor"+useSensorCounter());
		sensor.setAttributes();
		Link link1 = new Link("l"+ArchitectureBuilder.useedgeCounter());
		Link link2 = new Link("l"+ArchitectureBuilder.useedgeCounter());
		architecture.addEdge(link1, cpu, sensor);
		architecture.addEdge(link2, cpu, actuator);
	}
	/*
	 * changes the Type of the Tile that should be created
	 */
	public void setType(SubarchitectureType type) {
		currentType = type;
	}
	/*
	 * sets the Resource to which the tile should be connected
	 */
	public void setBus(ArrayList<Resource> buslist) {
		this.currentBuslist = buslist;
	}
	
	
}

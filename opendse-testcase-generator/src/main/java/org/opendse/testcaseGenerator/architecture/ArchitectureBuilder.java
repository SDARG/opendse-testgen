package org.opendse.testcaseGenerator.architecture;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

/**
 * Abstract Class for generation of a Architecture
 */
public abstract class ArchitectureBuilder{

	
	protected Architecture<Resource, Link> architecture;
	
	/*
	 * the following counters are used to keep track of resource counts for procedurally generating identifier of resources
	 */
	private static int edgeCounter = 1;
	private static int resourceCounter = 1;
	private static int cInterfaceCounter = 1;
	private static int cpuCounter = 1;
	private static int ramCounter = 1;
	private static int dataCounter = 1;
	private static int sensorCounter = 1;
	private static int actuatorCounter = 1;
	private static int busCounter = 1;
	private static int switchCounter = 1;
	
	
	public abstract Architecture<Resource,Link> build();	
	
	
	public static void resetCounters() {
		ArchitectureBuilder.edgeCounter = 1;
		ArchitectureBuilder.resourceCounter = 1;
		ArchitectureBuilder.cInterfaceCounter = 1;
		ArchitectureBuilder.cpuCounter = 1;
		ArchitectureBuilder.ramCounter = 1;
		ArchitectureBuilder.dataCounter = 1;
		ArchitectureBuilder.sensorCounter = 1;
		ArchitectureBuilder.actuatorCounter = 1;
		ArchitectureBuilder.busCounter = 1;
		ArchitectureBuilder.switchCounter = 1;
	}

	
	protected static int useedgeCounter() {
		edgeCounter++;
		return edgeCounter-1;
		
	}
	protected static int useresourceCounter() {
		resourceCounter++;
		return resourceCounter-1;
		
	}
	protected static int usecInterfaceCounter() {
		cInterfaceCounter++;
		return cInterfaceCounter-1;
		
	}
	
	protected static int usecpuCounter() {
		cpuCounter++;
		return cpuCounter-1;
		
	}
	
	protected static int useramCounter() {
		ramCounter++;
		return ramCounter-1;
		
	}
	
	protected static int usedataCounter() {
		dataCounter++;
		return dataCounter-1;
		
	}
	
	protected static int useSensorCounter() {
		sensorCounter++;
		return sensorCounter-1;
		
	}
	
	protected static int useActuatorCounter() {
		actuatorCounter++;
		return actuatorCounter-1;
		
	}
	
	protected static int usebusCounter() {
		busCounter++;
		return busCounter-1;
		
	}
	
	protected static int useSwitchCounter() {
		switchCounter++;
		return switchCounter-1;
		
	}
	
	public static int getCpuCounter() {
		return cpuCounter;
	}
	
	public static int getActuatorCounter() {
		return actuatorCounter;
	}
	
	public static int getSensorCounter() {
		return sensorCounter;
	}
	
	public static int getSwitchCounter() {
		return switchCounter;
	}
	

		
}

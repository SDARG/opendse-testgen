package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;

/**
 * The EthernetBus is a class to model a variation of a  basic Bus 
 */
public class EthernetBus extends Bus{

	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	private static double minThroughput = 500;
	private static double maxThroughput = 1000;
	
	public  EthernetBus(String id) {
		super(id);
		
	}
	public EthernetBus(Element parent) {
		super(parent);
		
	}

	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("throughput", rand.nextInt((int)(maxThroughput-minThroughput))+minThroughput);
	}
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability, double minThroughput, double maxThroughput ) {
		EthernetBus.minPower = minPower;
		EthernetBus.maxPower = maxPower;
		EthernetBus.minArea = minArea;
		EthernetBus.maxArea = maxArea;
		EthernetBus.minReliability = minReliability;
		EthernetBus.maxReliability = maxReliability;
		EthernetBus.minThroughput = minThroughput;
		EthernetBus.maxThroughput = maxThroughput;
	}
}

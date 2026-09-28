package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;

/**
 * The Gateway is a class to model a simple variation of a  CommunicationResource 
 */
public class Gateway extends CommunicationResource {

	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	private static double minThroughput = 5;
	private static double maxThroughput = 10;
	
	public Gateway(String id) {
		super(id);
	
	}
	public Gateway(Element parent) {
		super(parent);
		
	}
	
	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("throughput", rand.nextInt((int)(maxThroughput-minThroughput))+minThroughput);
	}
	
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability, double minThroughput, double maxThroughput ) {
		Gateway.minPower = minPower;
		Gateway.maxPower = maxPower;
		Gateway.minArea = minArea;
		Gateway.maxArea = maxArea;
		Gateway.minReliability = minReliability;
		Gateway.maxReliability = maxReliability;
		Gateway.minThroughput = minThroughput;
		Gateway.maxThroughput = maxThroughput;
	}

}
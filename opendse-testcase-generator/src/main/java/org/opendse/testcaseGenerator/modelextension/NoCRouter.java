package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;

/**
 * The NoCRouter is a class to model a simple variation of a  CommunicationResource 
 */
public class NoCRouter extends CommunicationResource{
	
	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	private static double minThroughput = 5;
	private static double maxThroughput = 10;
	
	
	public NoCRouter(String id, int xcoord, int ycoord) {
		super(id);
		setAttribute("xcoord", xcoord);
		setAttribute("ycoord", ycoord);
	}

	public NoCRouter(String id) {
		super(id);
		
	
	}
	

	public NoCRouter(Element parent, int xcoord, int ycoord) {
		super(parent);
		setAttribute("xcoord", xcoord);
		setAttribute("ycoord", ycoord);
	}
	
	public NoCRouter(Element parent) {
		super(parent);
		
	
	}
	
	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("throughput", rand.nextInt((int)(maxThroughput-minThroughput))+minThroughput);
	}
	
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability, double minThroughput, double maxThroughput ) {
		NoCRouter.minPower = minPower;
		NoCRouter.maxPower = maxPower;
		NoCRouter.minArea = minArea;
		NoCRouter.maxArea = maxArea;
		NoCRouter.minReliability = minReliability;
		NoCRouter.maxReliability = maxReliability;
		NoCRouter.minThroughput = minThroughput;
		NoCRouter.maxThroughput = maxThroughput;
	}
	

}

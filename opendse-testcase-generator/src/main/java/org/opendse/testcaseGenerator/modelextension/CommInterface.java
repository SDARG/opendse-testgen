package org.opendse.testcaseGenerator.modelextension;

import java.util.Random;

import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Element;

/**
 * The CommInterface is a class to model a simple variation of a  CommunicationResource 
 */

public class CommInterface extends CommunicationResource {

	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	private static double minThroughput = 5;
	private static double maxThroughput = 10;
	
	public CommInterface(String id) {
		super(id);
		
	}
	public CommInterface(Element parent) {
		super(parent);
	}
	
	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("throughput", rand.nextInt((int)(maxThroughput-minThroughput))+minThroughput);
	}

	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability, double minThroughput, double maxThroughput ) {
		CommInterface.minPower = minPower;
		CommInterface.maxPower = maxPower;
		CommInterface.minArea = minArea;
		CommInterface.maxArea = maxArea;
		CommInterface.minReliability = minReliability;
		CommInterface.maxReliability = maxReliability;
		CommInterface.minThroughput = minThroughput;
		CommInterface.maxThroughput = maxThroughput;
	}
}

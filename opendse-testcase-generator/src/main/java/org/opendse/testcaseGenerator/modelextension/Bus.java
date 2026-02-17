package org.opendse.testcaseGenerator.modelextension;

import java.util.Random;

import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Element;

/**
 * The Bus is a class to model a simple variation of a  CommunicationResource 
 */
public class Bus extends CommunicationResource{

	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	private static double minThroughput = 5;
	private static double maxThroughput = 10;
	
	public  Bus(String id) {
		super(id);
		
	}
	public Bus(Element parent) {
		super(parent);
		
	}

	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("throughput", rand.nextInt((int)(maxThroughput-minThroughput))+minThroughput);
	}
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability, double minThroughput, double maxThroughput ) {
		Bus.minPower = minPower;
		Bus.maxPower = maxPower;
		Bus.minArea = minArea;
		Bus.maxArea = maxArea;
		Bus.minReliability = minReliability;
		Bus.maxReliability = maxReliability;
		Bus.minThroughput = minThroughput;
		Bus.maxThroughput = maxThroughput;
	}
}

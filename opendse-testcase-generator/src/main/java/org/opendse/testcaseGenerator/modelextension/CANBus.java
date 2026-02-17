package org.opendse.testcaseGenerator.modelextension;

import java.util.Random;

import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Element;

/**
 * The CANBus is a class to model a variation of a  basic Bus 
 */
public class CANBus extends Bus {
	
	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	private static double minThroughput = 50;
	private static double maxThroughput = 100;
	
	public  CANBus(String id) {
		super(id);
		
	}
	public CANBus(Element parent) {
		super(parent);
		
	}

	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("throughput", rand.nextInt((int)(maxThroughput-minThroughput))+minThroughput);
	}
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability, double minThroughput, double maxThroughput ) {
		CANBus.minPower = minPower;
		CANBus.maxPower = maxPower;
		CANBus.minArea = minArea;
		CANBus.maxArea = maxArea;
		CANBus.minReliability = minReliability;
		CANBus.maxReliability = maxReliability;
		CANBus.minThroughput = minThroughput;
		CANBus.maxThroughput = maxThroughput;
	}
}

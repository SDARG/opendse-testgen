package org.opendse.testcaseGenerator.modelextension;

import java.util.Random;

import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Resource;

/**
 * The Sensor is a class to model a simple sensor resource 
 */
public class Sensor extends FunctionalResource{

	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	
	public Sensor(Element parent) {
		super(parent);
		
	}
	public Sensor(String id) {
		super(id);
		
	}
	
	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
	}
	
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability ) {
		Sensor.minPower = minPower;
		Sensor.maxPower = maxPower;
		Sensor.minArea = minArea;
		Sensor.maxArea = maxArea;
		Sensor.minReliability = minReliability;
		Sensor.maxReliability = maxReliability;
	}
}
	

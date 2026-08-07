package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;
import net.sf.opendse.optimization.constraints.SpecificationCapacityConstraints;

/**
 * The Actuator is a class to model a simple actuator resource 
 */
public class Actuator extends FunctionalResource {

	
	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;

	
	public Actuator(Element parent) {
		super(parent);		
		
	}
	public Actuator(String id) {
		super(id);
		
	}
	
	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
		this.setAttribute("capacity"+SpecificationCapacityConstraints.CAPACITY_MAX, 3);
	}
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability ) {
		Actuator.minPower = minPower;
		Actuator.maxPower = maxPower;
		Actuator.minArea = minArea;
		Actuator.maxArea = maxArea;
		Actuator.minReliability = minReliability;
		Actuator.maxReliability = maxReliability;
	}
}

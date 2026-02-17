package org.opendse.testcaseGenerator.modelextension;

import java.util.Random;

import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Resource;

/**
 * The Cpu is a class to model a simple processing resource 
 */
public class Cpu extends FunctionalResource{

	private static double minPower = 10.0;
	private static double maxPower = 20.0;
	private static double minArea = 10.0;
	private static double maxArea = 20.0;
	private static double minReliability = 0.005;
	private static double maxReliability = 0.05;
	
	
	public Cpu(Element parent) {
		super(parent);		
	}
	public Cpu(String id) {
		super(id);
	}
	
	public void setAttributes(){
		this.setAttribute("power", rand.nextInt((int)(maxPower-minPower))+minPower);
		this.setAttribute("area", rand.nextInt((int)(maxArea-minArea))+minArea);
		this.setAttribute("reliability", (rand.nextDouble() * (maxReliability - minReliability)) + minReliability);
	}
	
	public static void setAttributeBoundaries(double minPower, double maxPower,double minArea,double maxArea,double minReliability,double maxReliability ) {
		Cpu.minPower = minPower;
		Cpu.maxPower = maxPower;
		Cpu.minArea = minArea;
		Cpu.maxArea = maxArea;
		Cpu.minReliability = minReliability;
		Cpu.maxReliability = maxReliability;
	}
}

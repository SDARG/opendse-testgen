package org.opendse.testcaseGenerator;

import java.util.ArrayList;

import org.opendse.testcaseGenerator.modelextension.Actuator;
import org.opendse.testcaseGenerator.modelextension.ActuatorTask;
import org.opendse.testcaseGenerator.modelextension.Cpu;
import org.opendse.testcaseGenerator.modelextension.CpuTask;
import org.opendse.testcaseGenerator.modelextension.Sensor;
import org.opendse.testcaseGenerator.modelextension.SensorTask;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * Default implementation of a Mapper.
 * Creates Mappings only for corresponding Resource and Task types
 */
public class DefaultMapper extends Mapper{
	
private static int mappingCounter = 1;
	
	//Attribute boundaries for all Attributes
	private double minPower = 1.0;
	private double maxPower = 5.0;
	private double mindelay = 3.0;
	private double maxdelay = 10.0;
	private double minperiod = 100.0;
	private double maxperiod = 100.0;
	private double minReliability = 0.005;
	private double maxReliability = 0.05;
	
	
	//Connectivity percentages
	private double sensorConnectivity = 1;
	private double actuatorConnectivity = 1;
	private double processorConnectivity = 1;
	
	Rand rand;
	
	@Inject
	public DefaultMapper(Rand rand,
			@Constant(value = "sensorConnectivity", namespace = DefaultMapper.class) double sensConn,
			@Constant(value = "actuatorConnectivity", namespace = DefaultMapper.class)double actConn,
			@Constant(value = "processorConnectivity", namespace = DefaultMapper.class) double procConn,
			@Constant(value = "minPower", namespace = DefaultMapper.class) double minPower,
			@Constant(value = "maxPower", namespace = DefaultMapper.class) double maxPower,
			@Constant(value = "mindelay", namespace = DefaultMapper.class) double mindelay,
			@Constant(value = "maxdelay", namespace = DefaultMapper.class) double maxdelay,
			@Constant(value = "minperiod", namespace = DefaultMapper.class) double minperiod,
			@Constant(value = "maxperiod", namespace = DefaultMapper.class) double maxperiod,
			@Constant(value = "minReliability", namespace = DefaultMapper.class) double minReliability,
			@Constant(value = "maxReliability", namespace = DefaultMapper.class) double maxReliability) {
		super();
		this.rand = rand;
		this.sensorConnectivity = sensConn;
		this.actuatorConnectivity = actConn;
		this.processorConnectivity = procConn;
		this.minPower = minPower;
		this.maxPower = maxPower;
		this.mindelay = mindelay;
		this.maxdelay = maxdelay;
		this.minperiod = minperiod;
		this.maxperiod = maxperiod;
		this.minReliability = minReliability;
		this.maxReliability = maxReliability;
		
	}
	/**
	 * Creates Mappings from application to architecture
	 * 
	 * @param architecture
	 * 					the architecture that should be mapped to
	 * @param application
	 * 					the application that should be mapped from
	 */
	public Mappings<Task,Resource> map(Architecture<Resource, Link> architecture, Application<Task, Dependency> application){
		resetCounters();
		// Filters resource types to individual ArrayLists
		ArrayList<Resource> resourceList = new ArrayList<Resource>(architecture.getVertices());
		ArrayList<Resource> cpus = new ArrayList<Resource>();
		ArrayList<Resource> actuators = new ArrayList<Resource>();
		ArrayList<Resource> sensors = new ArrayList<Resource>();
		for ( Resource resource: resourceList) {
			if(resource instanceof Cpu) cpus.add(resource);
			if(resource instanceof Actuator) actuators.add(resource);
			if(resource instanceof Sensor) sensors.add(resource);
		}
		
		ArrayList<Task> taskList = new ArrayList<Task>(application.getVertices());
		Mappings<Task,Resource> mappings  = new Mappings<Task,Resource>();
		//guarantees all tasks have at least one mapping first and then generates random mappings to Connectivity specifications
		for(Task task:taskList) { 
			if(task instanceof SensorTask) {
				//generates one mapping for the task
				Resource sensor = sensors.get(rand.nextInt(sensors.size()));
				Mapping<Task,Resource> map = new Mapping<Task,Resource>("m"+usemappingCounter(),task,sensor);
				setAttributes(map);
				mappings.add(map);
				//randomly sets the rest
				for ( Resource resource: sensors)
				{
					if(resource == sensor) continue;
					if(rand.nextDouble() <= sensorConnectivity) {
					Mapping<Task,Resource> mapping = new Mapping<Task,Resource>("m"+usemappingCounter(),task,resource);
					setAttributes(mapping);
					mappings.add(mapping);
					}
				}
			}
			if(task instanceof ActuatorTask) {
				//generates one mapping for the task
				Resource actuator = actuators.get(rand.nextInt(actuators.size()));
				Mapping<Task,Resource> map = new Mapping<Task,Resource>("m"+usemappingCounter(),task,actuator);
				setAttributes(map);
				mappings.add(map);
				//randomly sets the rest
				for ( Resource resource: actuators)
				{
					if(resource == actuator) continue;
					if(rand.nextDouble() <= actuatorConnectivity) {
					Mapping<Task,Resource> mapping = new Mapping<Task,Resource>("m"+usemappingCounter(),task,resource);
					setAttributes(mapping);
					mappings.add(mapping);
					}
				}
			}
			if(task instanceof CpuTask) {
				//generates one mapping for the task
				Resource cpu = cpus.get(rand.nextInt(cpus.size()));
				Mapping<Task,Resource> map = new Mapping<Task,Resource>("m"+usemappingCounter(),task,cpu);
				setAttributes(map);
				mappings.add(map);
				//randomly sets the rest
				for ( Resource resource: cpus)
				{
					if(resource == cpu) continue;
					if(rand.nextDouble() <= processorConnectivity) {
					Mapping<Task,Resource> mapping = new Mapping<Task,Resource>("m"+usemappingCounter(),task,resource);
					setAttributes(mapping);
					mappings.add(mapping);
					}
				}
			}
			
			
			
			
			
		}		
		return mappings;
	}
	public static int usemappingCounter() {
		mappingCounter++;
		return mappingCounter-1;	
	}
	
	public static void resetCounters() {
		mappingCounter = 1;
	}
	
	
	/**
	 * sets Attributes to the Mapping with value within Range
	 * 
	 * @param mapping
	 * 				the mapping the Attributes are set to
	 */
	public void setAttributes(Mapping<Task,Resource> mapping) {
		if(maxPower == minPower) {
			mapping.setAttribute("power", maxPower);
		}else {
			mapping.setAttribute("power", rand.nextDouble((maxPower-minPower))+minPower);
		}
		if(maxperiod == minperiod) {
			mapping.setAttribute("period", maxperiod);
		}else {
			mapping.setAttribute("period", rand.nextDouble((maxperiod-minperiod))+minperiod);
		}
		if(maxdelay == mindelay) {
			mapping.setAttribute("delay", maxdelay);
		}else {
			mapping.setAttribute("delay", rand.nextDouble((maxdelay-mindelay))+mindelay);
		}
		if(maxReliability == minReliability) {
			mapping.setAttribute("reliability", maxReliability);
		}else {
			mapping.setAttribute("reliability", rand.nextDouble((maxReliability-minReliability))+minReliability);
		}		
	}
}

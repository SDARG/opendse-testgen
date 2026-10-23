package org.opendse.testcaseGenerator.mappings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * The {@link LimitedMapper} creates mappings between tasks and resources.
 * The maximal number of possible mappings per tasks is limited by a configurable threshold.  
 */

public class LimitedMapper extends Mapper {
	
	//Attribute boundaries for all Attributes
	protected final double minPower;
	protected final double maxPower;
	protected final double mindelay;
	protected final double maxdelay;
	protected final double minperiod;
	protected final double maxperiod;
	protected final double minReliability;
	protected final double maxReliability;
	protected final int minCapacity;
	protected final int maxCapacity;
	protected final int mappingLimit;
	protected final Rand rand;
	
	@Inject
	public LimitedMapper(Rand rand,
			@Constant(value = "mappingLimit", namespace = LimitedMapper.class) int mappingLimit,
			@Constant(value = "minPower", namespace = LimitedMapper.class) double minPower,
			@Constant(value = "maxPower", namespace = LimitedMapper.class) double maxPower,
			@Constant(value = "mindelay", namespace = LimitedMapper.class) double mindelay,
			@Constant(value = "maxdelay", namespace = LimitedMapper.class) double maxdelay,
			@Constant(value = "minperiod", namespace = LimitedMapper.class) double minperiod,
			@Constant(value = "maxperiod", namespace = LimitedMapper.class) double maxperiod,
			@Constant(value = "minReliability", namespace = LimitedMapper.class) double minReliability,
			@Constant(value = "maxReliability", namespace = LimitedMapper.class) double maxReliability,
			@Constant(value = "minCapacity", namespace = LimitedMapper.class) int minCapacity,
			@Constant(value = "maxCapacity", namespace = LimitedMapper.class) int maxCapacity) {
		super();
		this.rand = rand;
		this.mappingLimit=mappingLimit;
		this.minPower = minPower;
		this.maxPower = maxPower;
		this.mindelay = mindelay;
		this.maxdelay = maxdelay;
		this.minperiod = minperiod;
		this.maxperiod = maxperiod;
		this.minReliability = minReliability;
		this.maxReliability = maxReliability;
		this.minCapacity = minCapacity;
		this.maxCapacity = maxCapacity;
		
	}

	/**
	 * Creates Mappings from application to architecture with a limited amount of mappings per task
	 * 
	 * @param architecture
	 * 					the architecture that should be mapped to
	 * @param application
	 * 					the application that should be mapped from
	 * @throws Exception 
	 */
	public Mappings<Task,Resource> map(Architecture<Resource, Link> architecture, Application<Task, Dependency> application) throws Exception{
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
		/*
		 * guarantees all tasks have at least one mapping and at most the given limit
		 */
		for(Task task:taskList) {
			/*
			 * choose number of mappings to create at random
			 */
			int mappingsToCreate = rand.nextInt(1, mappingLimit+1);
			/*
			 * if safety critical task and only one mappings should be created, increase the number to 2
			 */
			boolean safetyCritical = false;
			if(task.getAttribute("safetyCritical") != null) {
				safetyCritical = task.getAttribute("safetyCritical");
				if(safetyCritical) {
					if(mappingsToCreate < 2) {
						mappingsToCreate = 2;
					}
				}
			}
			if(task instanceof SensorTask) {
				mapOneTask(task, sensors, mappings, safetyCritical, mappingsToCreate);
			}
			else if(task instanceof ActuatorTask) {
				mapOneTask(task, actuators, mappings, safetyCritical, mappingsToCreate);
			}
			else if(task instanceof CpuTask) {
				mapOneTask(task, cpus, mappings, safetyCritical, mappingsToCreate);
			}
			else {
				throw new Exception("task of unknown type cannot be mapped");
			}
		}
		return mappings;
	}
	
	/**
	 * This function maps one given task to resources in the given list up to a given limit.
	 * The mappings are stored in the given {@link Mappings} object.
	 */
	private void mapOneTask(Task task, List<Resource> resources, Mappings<Task,Resource> mappings, boolean safetyCritical, int mappingsToCreate) {
		/*
		 * check if limit greater or equal to sensor number
		 * if so, just generate mapping to every sensor without random
		 */
		if(mappingsToCreate >= resources.size()) {
			for(Resource target: resources) {
				Mapping<Task,Resource> mapping = new Mapping<Task,Resource>("m"+usemappingCounter(),task,target);
				setAttributes(mapping);
				mappings.add(mapping);
			}
			return;
		}
		/*
		 * generates chosen number of mappings
		 */
		int mappingsCreated = 0;
		Set<Resource> mappingTargets = new HashSet<Resource>();
		while(mappingsCreated < mappingsToCreate) {
			Resource target = resources.get(rand.nextInt(resources.size()));
			/*
			 * Check if already a mapping target
			 * in that case choose a new target to ensure no double mappings
			 */
			if(mappingTargets.contains(target)) {
				continue;
			}
			Mapping<Task,Resource> map = new Mapping<Task,Resource>("m"+usemappingCounter(),task,target);
			setAttributes(map);
			mappings.add(map);
			mappingTargets.add(target);
			mappingsCreated++;
		}
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
		if(maxCapacity == minCapacity) {
			mapping.setAttribute("capacity", maxCapacity);
		}else {
			mapping.setAttribute("capacity", (int)(rand.nextInt((maxCapacity-minCapacity))+minCapacity));
		}
	}

}

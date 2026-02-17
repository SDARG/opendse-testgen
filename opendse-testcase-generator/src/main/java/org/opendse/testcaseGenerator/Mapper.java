package org.opendse.testcaseGenerator;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.modelextension.Actuator;
import org.opendse.testcaseGenerator.modelextension.ActuatorTask;
import org.opendse.testcaseGenerator.modelextension.CommunicationResource;
import org.opendse.testcaseGenerator.modelextension.Cpu;
import org.opendse.testcaseGenerator.modelextension.CpuTask;
import org.opendse.testcaseGenerator.modelextension.Sensor;
import org.opendse.testcaseGenerator.modelextension.SensorTask;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

@ImplementedBy(DefaultMapper.class)
public abstract class Mapper {

	
	@Inject
	public Mapper() {
	}
	
	public abstract Mappings<Task,Resource> map(Architecture<Resource, Link> architecture, Application<Task, Dependency> application);
	
}

package org.opendse.testcaseGenerator.mappings;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

@ImplementedBy(DefaultMapper.class)
public abstract class Mapper {

	private static int mappingCounter = 0;
	
	@Inject
	public Mapper() {
	}
	
	public abstract Mappings<Task,Resource> map(Architecture<Resource, Link> architecture, Application<Task, Dependency> application);
	
	public static int usemappingCounter() {
		mappingCounter++;
		return mappingCounter-1;	
	}
	
	public static void resetCounters() {
		mappingCounter = 0;
	}
	
}

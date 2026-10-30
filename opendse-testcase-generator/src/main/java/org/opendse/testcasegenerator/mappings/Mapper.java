package org.opendse.testcasegenerator.mappings;

import com.google.inject.ImplementedBy;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * The {@link Mapper} is an abstract class that defines the methods each concrete mapper needs to implement.
 * Additionally the index counters when naming stuff during the mapping process are managed here.
 */
@ImplementedBy(DefaultMapper.class)
public abstract class Mapper {

	private static int mappingCounter = 0;
	
	
	public abstract Mappings<Task,Resource> map(Architecture<Resource, Link> architecture, Application<Task, Dependency> application);
	
	public static int usemappingCounter() {
		mappingCounter++;
		return mappingCounter-1;	
	}
	
	public static void resetCounters() {
		mappingCounter = 0;
	}
	
}

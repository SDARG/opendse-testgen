package org.opendse.testcaseGenerator.application;

import com.google.inject.ImplementedBy;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;



@ImplementedBy(ParallelApplicationBuilder.class)
public abstract class ApplicationBuilder {
	
	protected Application<Task,Dependency> application;
	private static int taskCounter = 0;
	private static int dependencyCounter = 0;
	private static int commCounter = 0;
	
	public abstract Application<Task,Dependency> build();
	
	public ApplicationBuilder() {
		
	}
	
	public static int usetaskCounter() {
		taskCounter++;
		return taskCounter-1;
	}
	public static int usedependencyCounter() {
		dependencyCounter++;
		return dependencyCounter-1;	
	}
	
	public static int usecommCounter() {
		commCounter++;
		return commCounter-1;
	}
	
	public static void resetCounters() {
		 taskCounter = 0;
		 dependencyCounter = 0;
		 commCounter = 0;
	}
}

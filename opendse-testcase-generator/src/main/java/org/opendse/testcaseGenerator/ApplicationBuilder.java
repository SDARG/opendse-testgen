package org.opendse.testcaseGenerator;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.modelextension.ActuatorTask;
import org.opendse.testcaseGenerator.modelextension.CpuTask;
import org.opendse.testcaseGenerator.modelextension.SensorTask;
import org.opt4j.core.config.visualization.DefaultApplicationFrame;

import com.google.inject.ImplementedBy;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;



@ImplementedBy(DefaultApplicationBuilder.class)
public abstract class ApplicationBuilder {
	
	protected Application<Task,Dependency> application;
	private static int taskCounter = 1;
	private static int dependencyCounter = 1;
	private static int commCounter = 1;
	
	protected int startTasks = 5;
	protected int endTasks = 5;
	protected int maxwidth = 5;
	protected boolean addMessages = true;
	
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
		 taskCounter = 1;
		 dependencyCounter = 1;
		 commCounter = 1;
	}
}

package org.opendse.testcaseGenerator;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.modelextension.ActuatorTask;
import org.opendse.testcaseGenerator.modelextension.CpuTask;
import org.opendse.testcaseGenerator.modelextension.SensorTask;
import org.opendse.testcaseGenerator.modules.ApplicationModule.NumberOfTasks;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;
/**
 * Default implementation of an ApplicationBuilder.
 */
public class DefaultApplicationBuilder extends ApplicationBuilder{

	Rand rand;
	private double taskMult = 1;
	private double interDependency;
	
	@Inject
	public DefaultApplicationBuilder(Rand rand,
			@Constant(value = "amountOfTasks", namespace = DefaultApplicationBuilder.class) NumberOfTasks numTasks,
			@Constant(value = "startTasks", namespace = DefaultApplicationBuilder.class) int startTasks,
			@Constant(value = "endTasks", namespace = DefaultApplicationBuilder.class) int endTasks,
			@Constant(value = "maxwidth", namespace = DefaultApplicationBuilder.class) int maxwidth,
			@Constant(value = "taskDependencyProbability", namespace = DefaultApplicationBuilder.class) double interDependency,
			@Constant(value = "addMessages", namespace = DefaultApplicationBuilder.class) boolean addMessages){
		this.rand  = rand;
		this.startTasks = startTasks;
		this.endTasks = endTasks;
		this.maxwidth = maxwidth;
		this.interDependency = interDependency;
		this.addMessages = addMessages;
		switch(numTasks) {
			case NumberOfTasks.many:
					taskMult = 1.5;
					break;
			case NumberOfTasks.normal:
					taskMult = 1;
					break;
			default: //NumberOfTasks.few
					taskMult = 0.5;
					break;
		}
	}
	/**
	 * Creates an Application that is scaled in size with architecture size and taskMult value.
	 */
	public Application<Task,Dependency> build(){
		resetCounters();
		application = new Application<Task,Dependency>();
		ArrayList<Task> toContinue = new ArrayList<Task>();
		ArrayList<Task> openTasks = new ArrayList<Task>();
		
		/*
		 * Calculates cpuTaskCount from Architecture size and taskMult
		 */
		int cpuTaskCount = Math.max(1,((int)Math.ceil(ArchitectureBuilder.getCpuCounter() * taskMult )) - ( endTasks + startTasks ));
		/*
		 * Creates #startTasks Tasks to start the application.
		 * I generates sensor tasks in ratio of sensors to cpus if the architecture has sensors.
		 */
		for (int i = 0; i< startTasks;i++) {
			if((ArchitectureBuilder.getSensorCounter() != 1) && i < (2*ArchitectureBuilder.getSensorCounter()/ArchitectureBuilder.getCpuCounter()+1)) {
				Task sT = new SensorTask("t"+usetaskCounter());
				application.addVertex(sT);
				Task cT = new CpuTask("t"+usetaskCounter());
				application.addVertex(cT);
				if(addMessages) {
					Task m = new Communication("c"+usecommCounter());
					application.addVertex(m);
					application.addEdge(new Dependency("d"+usedependencyCounter()),sT,m);
					application.addEdge(new Dependency("d"+usedependencyCounter()),m,cT);
				}else {
					application.addEdge(new Dependency("d"+usedependencyCounter()),sT,cT);
				}
				if(addMessages) {
					Task comm = new Communication("c"+usecommCounter());
					application.addVertex(comm);
					application.addEdge(new Dependency("d"+usedependencyCounter()),cT,comm);
					toContinue.add(comm);
					} else {
						toContinue.add(cT);
					}
			} else {
				Task cT = new CpuTask("t"+usetaskCounter());
				application.addVertex(cT);
				if(addMessages) {
					Task comm = new Communication("c"+usecommCounter());
					application.addVertex(comm);
					application.addEdge(new Dependency("d"+usedependencyCounter()),cT,comm);
					toContinue.add(comm);
				} else {
					toContinue.add(cT);
				}
			}
		}
		/*
		 * generates a number of Tasks equal to the cpuTaskCount
		 */
		for (int i = 0; i< cpuTaskCount;i++) {
			Task t = new CpuTask("t"+usetaskCounter());
			application.addVertex(t);
			openTasks.add(t);
		}
		/*
		 * arranges all generated Tasks into a structured application
		 */
		while(!openTasks.isEmpty()) {
			ArrayList<Task> tempTask = new ArrayList<Task>();
			ArrayList<Task> tempComm = new ArrayList<Task>();
			for(int i = 0; i < rand.nextInt(Math.min( maxwidth,openTasks.size())+1);i++){
				Task t = openTasks.remove(0);
				tempTask.add(t);	
				if(addMessages) {
				Task comm  = new Communication("c"+usecommCounter());
				application.addVertex(comm);
				application.addEdge(new Dependency("d"+usedependencyCounter()), t, comm);	
				tempComm.add(comm);
				} else {
					tempComm.add(t);
				}
			}
			
			/*
			 * Connects all tasks that have been saved to be connected to the created tasks, sets connections depending on interDependency value
			 */
			boolean[] connectedTasks = new boolean[tempTask.size()];
			for(Task comm : toContinue) {
				boolean connected = false;
				for(Task task : tempTask) {
					if(rand.nextDouble() < interDependency) {
						application.addEdge(new Dependency("d"+usedependencyCounter()), comm, task);
						connected = true;
						connectedTasks[tempTask.indexOf(task)] = true;
					}
				}
				if(!connected) {
					tempComm.add(comm);
				}
			}
			for(int i = 0; i < connectedTasks.length;i++) {
				if(!connectedTasks[i]) {
					application.addEdge(new Dependency("d"+usedependencyCounter()),	toContinue.get(rand.nextInt(toContinue.size())) , tempTask.get(i));
				}
			}
			/*
			 * Saves the just created tasks to be connected to in the next iteration
			 */
			toContinue = tempComm;
		}
		/*
		 * Creates #endTasks Tasks to end the application.
		 * I generates actuator tasks in ratio of actuators to cpus if the architecture has actuators.
		 */
		for (int i = 0; i< endTasks;i++) {
			if((ArchitectureBuilder.getActuatorCounter() != 1) && i < (2*ArchitectureBuilder.getActuatorCounter()/ArchitectureBuilder.getCpuCounter()+1)) {
				Task cT = new CpuTask("t"+usetaskCounter());
				openTasks.add(cT);
				application.addVertex(cT);
				Task aT = new ActuatorTask("t"+usetaskCounter());
				application.addVertex(aT);
				if(addMessages) {
					Task m = new Communication("c"+usecommCounter());
					application.addVertex(m);
					application.addEdge(new Dependency("d"+usedependencyCounter()),cT,m);
					application.addEdge(new Dependency("d"+usedependencyCounter()),m,aT);
				}else {
					application.addEdge(new Dependency("d"+usedependencyCounter()),cT,aT);
				}	
			} else {
				Task cT = new CpuTask("t"+usetaskCounter());
				openTasks.add(cT);
				application.addVertex(cT);
			}
		}
		/*
		 * Connects the open Tasks to the created endTasks
		 */
		ArrayList<Task> tempTask = new ArrayList<Task>();
		ArrayList<Task> toContinueCopy = new ArrayList<Task>(toContinue);
		for(Task task : openTasks) {
			boolean taskConnected = false;
			for(Task comm : toContinue) {
				if(rand.nextDouble() < interDependency) {
					application.addEdge(new Dependency("d"+usedependencyCounter()), comm, task);
					taskConnected = true;
					toContinueCopy.remove(comm);
				}
			}
			if(!taskConnected) {
				tempTask.add(task);
			}
		}
		/*
		 * Prevents unwanted open ends by connecting remaining tasks
		 */
		for(Task task : toContinueCopy) {
			Task t;
			if(tempTask.size() > 0) {
				t = tempTask.remove(rand.nextInt(tempTask.size()));
			} else {
				t = openTasks.get(rand.nextInt(openTasks.size()));
			}
			application.addEdge(new Dependency("d"+usedependencyCounter()), task, t );
		}
		
		for(Task task : tempTask) {
			if(toContinueCopy.size() > 0) {
				application.addEdge(new Dependency("d"+usedependencyCounter()), toContinueCopy.get(rand.nextInt(toContinueCopy.size())), task);
			}else {
				application.addEdge(new Dependency("d"+usedependencyCounter()), toContinue.get(rand.nextInt(toContinue.size())), task);	
			}
		}

		
		
		return application;
	}
}

package org.opendse.testcaseGenerator.application;

import java.util.ArrayList;

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
import net.sf.opendse.model.Function;
import net.sf.opendse.model.Task;

/**
 * The {@link ParallelApplicationBuilder} builds several parallel applications with no connections to each other.
 */
public class ParallelApplicationBuilder extends ApplicationBuilder {
	
	protected final Rand rand;
	protected final int startTasks;
	protected final int endTasks;
	protected final int maxwidth ;
	protected final boolean addMessages;
	protected final double taskMult;
	protected final double interDependency;
	protected final int numberOfParallelFunctions;
	
	@Inject
	public ParallelApplicationBuilder(Rand rand,
			@Constant(value = "amountOfTasks", namespace = ParallelApplicationBuilder.class) NumberOfTasks numTasks,
			@Constant(value = "startTasks", namespace = ParallelApplicationBuilder.class) int startTasks,
			@Constant(value = "endTasks", namespace = ParallelApplicationBuilder.class) int endTasks,
			@Constant(value = "maxwidth", namespace = ParallelApplicationBuilder.class) int maxwidth,
			@Constant(value = "taskDependencyProbability", namespace = ParallelApplicationBuilder.class) double interDependency,
			@Constant(value = "addMessages", namespace = ParallelApplicationBuilder.class) boolean addMessages,
			@Constant(value = "numberOfParallelFunctions", namespace = ParallelApplicationBuilder.class) int numberOfParallelFunctions){
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
			case NumberOfTasks.few:
				taskMult = 0.7;
				break;
			default:
				taskMult = 0.7;
				break;
		}
		//ensure at least 1 parallel function, otherwise makes no sense
		if(numberOfParallelFunctions <= 0) {
			this.numberOfParallelFunctions=1;
		}
		else {
			this.numberOfParallelFunctions=numberOfParallelFunctions;
		}
		
	}

	/**
	 * Creates an Application that is scaled in size with architecture size and taskMult value with the specified amount of parallel functions.
	 */
	public Application<Task,Dependency> build(){
		resetCounters();
		application = new Application<Task,Dependency>();
		
		/*
		 * calculate number of sensor/actuator tasks per function to allow feasible specification
		 */
		int sensorTasksPerFunction = (int)Math.floor(ArchitectureBuilder.getSensorCounter()/numberOfParallelFunctions);
		int actuatorTasksPerFunction = (int)Math.floor(ArchitectureBuilder.getActuatorCounter()/numberOfParallelFunctions);
		
		/*
		 * build number of parallel functions specified
		 * each one gets specified start/end tasks and number of tasks
		 */
		for(int j=0;j<numberOfParallelFunctions;j++) {
			//create new function with unique id
			Function<Task, Dependency> function = new Function<Task, Dependency>("func"+j);
			
			//stores all tasks that have no output connection yet and should have one
			ArrayList<Task> openOutputs = new ArrayList<Task>();
			//stores all tasks that have no input connection yet and should have one
			ArrayList<Task> openInputs = new ArrayList<Task>();
			
			/*
			 * Calculates cpuTaskCount from Architecture size and taskMult
			 */
			int cpuTaskCount = Math.max(1,((int)Math.ceil(ArchitectureBuilder.getCpuCounter() * taskMult )) - ( endTasks + startTasks ));
			
			/*
			 * Creates #startTasks Tasks to start the application.
			 * generates sensor tasks if the architecture has enough sensors, otherwise CPU tasks.
			 */
			for (int i = 0; i < startTasks;i++) {
				if(ArchitectureBuilder.getSensorCounter() > 0 && i < sensorTasksPerFunction) {
					Task sT = new SensorTask("t"+usetaskCounter());
					function.addVertex(sT);
					if(addMessages) {
						Task c = new Communication("c"+usecommCounter());
						function.addVertex(c);
						function.addEdge(new Dependency("d"+usedependencyCounter()),sT,c);
						openOutputs.add(c);
					}
					else {
						openOutputs.add(sT);
					}
				} else {
					Task cT = new CpuTask("t"+usetaskCounter());
					function.addVertex(cT);
					if(addMessages) {
						Task comm = new Communication("c"+usecommCounter());
						function.addVertex(comm);
						function.addEdge(new Dependency("d"+usedependencyCounter()),cT,comm);
						openOutputs.add(comm);
					} else {
						openOutputs.add(cT);
					}
				}
				
			}
			/*
			 * generates a number of Tasks equal to the cpuTaskCount
			 */
			for (int i = 0; i < cpuTaskCount;i++) {
				Task t = new CpuTask("t"+usetaskCounter());
				function.addVertex(t);
				openInputs.add(t);
			}
			/*
			 * arranges all generated Tasks into a structured application
			 */
			while(!openInputs.isEmpty()) {
				//temp storage for tasks with open In-/Outputs
				ArrayList<Task> tempOpenInputs = new ArrayList<Task>();
				ArrayList<Task> tempOpenOutputs = new ArrayList<Task>();
				for(int i = 0; i < rand.nextInt(Math.min( maxwidth,openInputs.size())+1);i++){
					Task t = openInputs.remove(0);
					tempOpenInputs.add(t);	
					if(addMessages) {
						Task comm  = new Communication("c"+usecommCounter());
						function.addVertex(comm);
						function.addEdge(new Dependency("d"+usedependencyCounter()), t, comm);	
						tempOpenOutputs.add(comm);
					} else {
						tempOpenOutputs.add(t);
					}
				}
				
				/*
				 * Connects all tasks that have been saved to be connected to the created tasks, sets connections depending on interDependency value
				 */
				boolean[] connectedTasks = new boolean[tempOpenInputs.size()];
				for(Task comm : openOutputs) {
					boolean connected = false;
					for(Task task : tempOpenInputs) {
						if(rand.nextDouble() < interDependency) {
							function.addEdge(new Dependency("d"+usedependencyCounter()), comm, task);
							connected = true;
							connectedTasks[tempOpenInputs.indexOf(task)] = true;
						}
					}
					if(!connected) {
						tempOpenOutputs.add(comm);
					}
				}
				/*
				 * each newly created task gets at least one input connection
				 * so after current iteration every task in the function has at least one input
				 */
				for(int i = 0; i < connectedTasks.length;i++) {
					if(!connectedTasks[i]) {
						//choose one openOutput task at random, connect him to a task with missing input, and then remove openOutput from list
						Task openOutput = openOutputs.get(rand.nextInt(openOutputs.size()));
						function.addEdge(new Dependency("d"+usedependencyCounter()), openOutput, tempOpenInputs.get(i));
						tempOpenOutputs.remove(openOutput);
					}
				}
				/*
				 * Saves the just created tasks to be connected to in the next iteration
				 */
				openOutputs = tempOpenOutputs;
			}
			/*
			 * Creates #endTasks Tasks to end the application.
			 * generates actuator tasks if the architecture has actuators.
			 */
			for (int i = 0; i < endTasks;i++) {
				if(ArchitectureBuilder.getActuatorCounter() > 0 && i < actuatorTasksPerFunction) {
					Task aT = new ActuatorTask("t"+usetaskCounter());
					function.addVertex(aT);
					openInputs.add(aT);
				}
				else {
					Task cT = new CpuTask("t"+usetaskCounter());
					openInputs.add(cT);
					function.addVertex(cT);
				}
			}
			
			/*
			 * temp storage for tasks with open In-/Output
			 */
			ArrayList<Task> tempOpenInput = new ArrayList<Task>();
			ArrayList<Task> tempOpenOutput = new ArrayList<Task>(openOutputs);
			
			/*
			 * Connect the open Tasks to the created endTasks
			 */
			for(Task task : openInputs) {
				boolean taskConnected = false;
				for(Task comm : openOutputs) {
					if(rand.nextDouble() < interDependency) {
						function.addEdge(new Dependency("d"+usedependencyCounter()), comm, task);
						taskConnected = true;
						tempOpenOutput.remove(comm);
					}
				}
				if(!taskConnected) {
					tempOpenInput.add(task);
				}
			}
			
			/*
			 * Prevents unwanted open ends by connecting remaining tasks
			 */
			for(Task task : tempOpenOutput) {
				Task t;
				if(tempOpenInput.size() > 0) {
					t = tempOpenInput.remove(rand.nextInt(tempOpenInput.size()));
				} else {
					t = openInputs.get(rand.nextInt(openInputs.size()));
				}
				function.addEdge(new Dependency("d"+usedependencyCounter()), task, t );
			}
			
			for(Task task : tempOpenInput) {
				if(tempOpenOutput.size() > 0) {
					function.addEdge(new Dependency("d"+usedependencyCounter()), tempOpenOutput.get(rand.nextInt(tempOpenOutput.size())), task);
				}else {
					function.addEdge(new Dependency("d"+usedependencyCounter()), openOutputs.get(rand.nextInt(openOutputs.size())), task);	
				}
			}
			//add function to whole application
			application.add(function);
		}
		
		return application;
	}

}

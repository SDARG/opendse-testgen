package org.opendse.testcasegenerator.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opendse.testcasegenerator.architecture.ArchitectureBuilder;
import org.opendse.testcasegenerator.modelextension.ActuatorTask;
import org.opendse.testcasegenerator.modelextension.CpuTask;
import org.opendse.testcasegenerator.modelextension.SensorTask;
import org.opendse.testcasegenerator.modules.ApplicationModule.NumberOfTasks;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Function;
import net.sf.opendse.model.Task;

/**
 * The {@link MixedCriticalityApplicationBuilder} builds several parallel applications with no connections to each other.
 * A configurable amount of these applications will be annotated as safety critical (only their tasks, but not the messages).
 */
public class MixedCriticalityApplicationBuilder extends ApplicationBuilder {
	
	protected final Rand rand;
	protected final int startTasks;
	protected final int endTasks;
	protected final int maxwidth ;
	protected final boolean addMessages;
	protected final double taskMult;
	protected final double interDependency;
	protected final int numberOfParallelFunctions;
	protected final int numberOfCriticalFunctions;
	
	@Inject
	public MixedCriticalityApplicationBuilder(Rand rand,
			@Constant(value = "amountOfTasks", namespace = MixedCriticalityApplicationBuilder.class) NumberOfTasks numTasks,
			@Constant(value = "startTasks", namespace = MixedCriticalityApplicationBuilder.class) int startTasks,
			@Constant(value = "endTasks", namespace = MixedCriticalityApplicationBuilder.class) int endTasks,
			@Constant(value = "maxwidth", namespace = MixedCriticalityApplicationBuilder.class) int maxwidth,
			@Constant(value = "taskDependencyProbability", namespace = MixedCriticalityApplicationBuilder.class) double interDependency,
			@Constant(value = "addMessages", namespace = MixedCriticalityApplicationBuilder.class) boolean addMessages,
			@Constant(value = "numberOfParallelFunctions", namespace = MixedCriticalityApplicationBuilder.class) int numberOfParallelFunctions,
			@Constant(value = "numberOfCriticalFunctions", namespace = MixedCriticalityApplicationBuilder.class) int numberOfCriticalFunctions){
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
		//ensure at least 1 parallel function
		if(numberOfParallelFunctions <= 0) {
			this.numberOfParallelFunctions=1;
		}
		else {
			this.numberOfParallelFunctions=numberOfParallelFunctions;
		}
		//if more critical than absolute functions change the number accordingly, in this case all will be critical
		if(numberOfCriticalFunctions > this.numberOfParallelFunctions) {
			this.numberOfCriticalFunctions = this.numberOfParallelFunctions;
		}
		else {
			this.numberOfCriticalFunctions=numberOfCriticalFunctions;
		}
		
	}

	/**
	 * Builds an application with several disconnected functions of which a configurable amount will be safety critical (default is 50%).
	 */
	@Override
	public Application<Task, Dependency> build() {
		resetCounters();
		application = new Application<Task,Dependency>();
		
		/*
		 * calculate number of sensor/actuator tasks per function to allow feasible specification
		 */
		int sensorTasksPerFunction = (int)Math.floor(ArchitectureBuilder.getSensorCounter()/numberOfParallelFunctions);
		int actuatorTasksPerFunction = (int)Math.floor(ArchitectureBuilder.getActuatorCounter()/numberOfParallelFunctions);
		
		/*
		 * define which functions will be critical and which not
		 */
		Set<Integer> criticalFunctions = setCriticalFunctions();
		
		
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
			
			//check if this function index is critical
			boolean safetyCritical = criticalFunctions.contains(j);
			
			/*
			 * Calculates cpuTaskCount from Architecture size and taskMult
			 */
			int cpuTaskCount;
			//if safety critical only create half as many tasks to have enough capacity for required redundancy
			if(safetyCritical) {
				cpuTaskCount = (int) Math.ceil(Math.max(1,((int)Math.ceil(ArchitectureBuilder.getCpuCounter() * taskMult )) - ( endTasks + startTasks )) / 2);
			}
			else {
				cpuTaskCount = Math.max(1,((int)Math.ceil(ArchitectureBuilder.getCpuCounter() * taskMult )) - ( endTasks + startTasks ));
			}
			
			//generate the start tasks
			generateStartTasks(safetyCritical, sensorTasksPerFunction, function, openOutputs);
			
			
			/*
			 * generates a number of Tasks equal to the cpuTaskCount
			 */
			for (int i = 0; i < cpuTaskCount;i++) {
				Task t = new CpuTask("t"+usetaskCounter());
				if(safetyCritical) {
					t.setAttribute("safetyCritical", true);
				}
				else {
					t.setAttribute("safetyCritical", false);
				}
				function.addVertex(t);
				openInputs.add(t);
			}
			
			//connect start tasks to intermediate tasks
			connectIntermediateTasks(function, openInputs, openOutputs);
			
			/*
			 * Creates #endTasks Tasks to end the application.
			 * generates actuator tasks if the architecture has actuators.
			 */
			for (int i = 0; i < endTasks;i++) {
				if(ArchitectureBuilder.getActuatorCounter() > 0 && i < actuatorTasksPerFunction) {
					Task aT = new ActuatorTask("t"+usetaskCounter());
					if(safetyCritical) {
						aT.setAttribute("safetyCritical", true);
					}
					else {
						aT.setAttribute("safetyCritical", false);
					}
					function.addVertex(aT);
					openInputs.add(aT);
				}
				else {
					Task cT = new CpuTask("t"+usetaskCounter());
					if(safetyCritical) {
						cT.setAttribute("safetyCritical", true);
					}
					else {
						cT.setAttribute("safetyCritical", false);
					}
					openInputs.add(cT);
					function.addVertex(cT);
				}
			}
			
			//connect intermediate tasks without outgoing edges to end tasks
			connectEndTasks(function, openInputs, openOutputs);
			
			//add function to whole application
			application.add(function);
		}
		return application;
	}
	
	/*
	 * defines which functions are critical and which not
	 */
	private Set<Integer> setCriticalFunctions() {
		/*
		 * define which functions will be critical and which not
		 */
		Set<Integer> criticalFunctions = new HashSet<Integer>();
		//all are critical, just add each function index to set
		if(numberOfCriticalFunctions == numberOfParallelFunctions) {
			for(int i = 0;i<numberOfParallelFunctions;i++) {
				criticalFunctions.add(i);
			}
		}
		//otherwise randomly choose function indices in the given interval
		else {
			rand.ints(0, numberOfParallelFunctions)
			.distinct()
			.limit(numberOfCriticalFunctions)
			.forEach(n -> criticalFunctions.add(n));
		}
		return criticalFunctions;
	}
	
	
	/*
	 * generates the start tasks
	 */
	private void generateStartTasks(boolean safetyCritical, int sensorTasksPerFunction, Function<Task, Dependency> function, List<Task> openOutputs) {
		/*
		 * Creates #startTasks Tasks to start the application.
		 * generates sensor tasks if the architecture has enough sensors, otherwise CPU tasks.
		 */
		for (int i = 0; i < startTasks;i++) {
			if(ArchitectureBuilder.getSensorCounter() > 0 && i < sensorTasksPerFunction) {
				Task sT = new SensorTask("t"+usetaskCounter());
				if(safetyCritical) {
					sT.setAttribute("safetyCritical", true);
				}
				else {
					sT.setAttribute("safetyCritical", false);
				}
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
				if(safetyCritical) {
					cT.setAttribute("safetyCritical", true);
				}
				else {
					cT.setAttribute("safetyCritical", false);
				}
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
	}
	
	
	/*
	 * connects start tasks to intermediate tasks and those with each other
	 */
	private void connectIntermediateTasks(Function<Task, Dependency> function, List<Task> openInputs, List<Task> openOutputs) {
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
			openOutputs.clear();
			openOutputs.addAll(tempOpenOutputs);
		}
	}
	
	/*
	 * connect all intermediate tasks without outgoing edges to end tasks
	 */
	private void connectEndTasks(Function<Task, Dependency> function, List<Task> openInputs, List<Task> openOutputs) {
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
		
		//check if function fully connected, if not then add additional edges
		handleUnconnectedFunction(function);
	}

}

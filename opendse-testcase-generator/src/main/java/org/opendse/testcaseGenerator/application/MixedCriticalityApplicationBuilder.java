package org.opendse.testcaseGenerator.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.modelextension.ActuatorTask;
import org.opendse.testcaseGenerator.modelextension.CpuTask;
import org.opendse.testcaseGenerator.modelextension.SensorTask;
import org.opendse.testcaseGenerator.modules.ApplicationModule.NumberOfTasks;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

import edu.uci.ics.jung.graph.util.Pair;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Edge;
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
			Map<Integer, List<Task>> connectionsMap = checkForConnectedFunction(function);
			if(connectionsMap != null) {
				//connect one task from list i to one task from list i+1 for all i
				for(int i = 0; i < connectionsMap.keySet().size()-1;i++) {
					List<Task> firstList = connectionsMap.get(i);
					List<Task> secondList = connectionsMap.get(i+1);
					Task source = firstList.getFirst();
					int index = 1;
					while(true) {
						if(!(source instanceof Communication) && (function.getSuccessorCount(source) > 0)) {
							break;
						}
						else {
							source = firstList.get(index);
							index++;
						}
					}
					Task destination = secondList.getFirst();
					index=1;
					while(true) {
						if(!(destination instanceof Communication) && (function.getPredecessorCount(destination) > 0)) {
							break;
						}
						else {
							destination = secondList.get(index);
							index++;
						}
					}
					//connect source and destination via a new message
					Task comm  = new Communication("c"+usecommCounter());
					function.addVertex(comm);
					function.addEdge(new Dependency("d"+usedependencyCounter()), source, comm);
					function.addEdge(new Dependency("d"+usedependencyCounter()), comm, destination);
				}
			}
			//add function to whole application
			application.add(function);
		}
		return application;
	}
	
	/*
	 * Checks if the given function is fully connected.
	 * If so, returns null.
	 * If not, returns a map of lists, where each list contains the tasks that are fully connected to each other.
	 */
	private Map<Integer, List<Task>> checkForConnectedFunction(Function< Task, Dependency> function){
		Map<Integer, List<Task>> connectionsMap = new HashMap<Integer, List<Task>>();
		int currentIndex = 0;
		List<Task> tasksRemaining = new ArrayList<Task>(function.getVertices());
		Task start = tasksRemaining.getFirst();
		Set<Task> visited = new HashSet<Task>();
		visited.add(start);
		Set<Task> newlyVistited = new HashSet<Task>();
		newlyVistited.add(start);
		tasksRemaining.remove(start);
		int debugIteration = 0;
		boolean notFullyConnected = false;
		while(!(tasksRemaining.isEmpty())) {
			//go over all new tasks and find their neighbors
			Set<Task> temp = new HashSet<Task>();
			for(Task t : newlyVistited) {
				temp.addAll(function.getNeighbors(t));
			}
			
			//remove already visited tasks form temp set
			temp.removeAll(visited);
			
			/*
			 * if temp is empty, no new neighbors were found and tasksRemaining was not empty
			 * therefore add the currently visited tasks to a list for the return map and
			 * choose a new starting task from the remaining ones, add it to visited and newly visited
			 */
			if(temp.isEmpty()) {
				//add visited to map
				notFullyConnected = true;
				List<Task> connected = new ArrayList<Task>(visited);
				connectionsMap.put(currentIndex, connected);
				currentIndex++;
				//clear all sets
				visited.clear();
				newlyVistited.clear();
				temp.clear();
				//define new start
				start = tasksRemaining.getFirst();
				visited.add(start);
				newlyVistited.add(start);
				tasksRemaining.remove(start);
			}
			/*
			 * new neighbors were found, set them as newly visited set and also add them to visited set
			 */
			else {
				//add neighbors to visited set
				visited.addAll(temp);
				//reset newly visited set
				newlyVistited.clear();
				newlyVistited.addAll(temp);
				//remove temp also form remainingTasks
				tasksRemaining.removeAll(temp);
			}
		}
		//put the last visited set also in the map, otherwise the last part of a non fully connected graph would not be counted
		if(notFullyConnected) {
			List<Task> connected = new ArrayList<Task>(visited);
			connectionsMap.put(currentIndex, connected);
		}
		//if more than one list in the map the function is not fully connected, so return the map
		if(connectionsMap.keySet().size() >1) {
			return connectionsMap;
		}
		else {
			return null;
		}
	}

}

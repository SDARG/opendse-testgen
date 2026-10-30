package org.opendse.testcasegenerator.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.ImplementedBy;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Function;
import net.sf.opendse.model.Task;


/**
 * The {@link ApplicationBuilder} is an abstract class that defines the methods each concrete application builder needs to implement.
 * Additionally the index counters when naming stuff during the application building process are managed here.
 */
@ImplementedBy(ParallelApplicationBuilder.class)
public abstract class ApplicationBuilder {
	
	protected Application<Task,Dependency> application;
	private static int taskCounter = 0;
	private static int dependencyCounter = 0;
	private static int commCounter = 0;
	
	public abstract Application<Task,Dependency> build();
	
	
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
	
	 /**
	 * Checks if the given function is fully connected.
	 * If so, returns null.
	 * If not, returns a map of lists, where each list contains the tasks that are fully connected to each other.
	 */
	protected Map<Integer, List<Task>> checkForConnectedFunction(Function< Task, Dependency> function){
		Map<Integer, List<Task>> connectionsMap = new HashMap<Integer, List<Task>>();
		int currentIndex = 0;
		List<Task> tasksRemaining = new ArrayList<Task>(function.getVertices());
		Task start = tasksRemaining.getFirst();
		Set<Task> visited = new HashSet<Task>();
		visited.add(start);
		Set<Task> newlyVistited = new HashSet<Task>();
		newlyVistited.add(start);
		tasksRemaining.remove(start);
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
	
	/*
	 * handles the case when the tasks in a function are not fully connected as this would results in several parallel functions instead of one
	 */
	protected void handleUnconnectedFunction(Function<Task, Dependency> function, boolean addMessages) {
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
				if(addMessages) {
					//connect source and destination via a new message
					Task comm  = new Communication("c"+usecommCounter());
					function.addVertex(comm);
					function.addEdge(new Dependency("d"+usedependencyCounter()), source, comm);
					function.addEdge(new Dependency("d"+usedependencyCounter()), comm, destination);
				}
				else {
					//connect source and destination directly
					function.addEdge(new Dependency("d"+usedependencyCounter()), source, destination);
				}
				
			}
		}
	}
}

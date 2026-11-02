package org.opendse.testcasegenerator.application;

import java.util.HashSet;
import java.util.Set;

import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Function;
import net.sf.opendse.model.Task;

/*
 * abstract class to provide methods for concrete application builder test classes
 */
public abstract class ApplicationBuilderTest {

	/*
	 * method to get all start tasks of a function
	 */
	protected Set<Task> getStartTasks(Function<Task, Dependency> function){
		Set<Task> startTasks = new HashSet<Task>();
		for(Task task : function.getVertices()) {
			//if no incoming edges then it is a start task
			if(function.getInEdges(task).size() == 0) {
				startTasks.add(task);
			}
		}
		return startTasks;
	}
	
	/*
	 * method to get all end tasks of a function
	 */
	protected Set<Task> getEndTasks(Function<Task, Dependency> function){
		Set<Task> endTasks = new HashSet<Task>();
		for(Task task : function.getVertices()) {
			//if no incoming edges then it is a start task
			if(function.getOutEdges(task).size() == 0) {
				endTasks.add(task);
			}
		}
		return endTasks;
	}
	
}

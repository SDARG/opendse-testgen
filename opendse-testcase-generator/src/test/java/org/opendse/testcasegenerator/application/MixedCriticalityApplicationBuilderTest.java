package org.opendse.testcasegenerator.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opendse.testcasegenerator.modules.ApplicationModule.NumberOfTasks;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.common.random.RandomMersenneTwister;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Function;
import net.sf.opendse.model.Task;

public class MixedCriticalityApplicationBuilderTest {

	private MixedCriticalityApplicationBuilder builder;
	private Rand rand = new RandomMersenneTwister(0);
	
	@BeforeEach
	void setup() {
		//build a default application with 3 start/end tasks and 3 parallel functions of which 1 is safety critical
		this.builder = new MixedCriticalityApplicationBuilder(rand, NumberOfTasks.normal, 3, 3, 3, 0.5, true, 3,1);
	}
	
	/*
	 * checks if messages are correctly added to application
	 */
	@Test
	void testMessagesAddedCorrectly() {
		Application<Task, Dependency> application = builder.build();
		//each function should contain messages
		Set<Function<Task,Dependency>> functions = application.getFunctions();
		for(Function<Task,Dependency> function : functions) {
			boolean hasMessages = function.getVertices().stream().filter((Task t) -> t instanceof Communication).count() > 0;
			assertTrue(hasMessages,"function has no messages but it should have");
		}
		//builder without messages
		ParallelApplicationBuilder temp = new ParallelApplicationBuilder(rand, NumberOfTasks.normal, 3, 3, 3, 0.5, false, 3);
		application = temp.build();
		//no function should contain messages
		functions = application.getFunctions();
		for(Function<Task,Dependency> function : functions) {
			boolean hasMessages = function.getVertices().stream().filter((Task t) -> t instanceof Communication).count() == 0;
			assertTrue(hasMessages,"function has messages but it should have none");
		}
	}
	
	/*
	 * checks if the number of parallel functions is correct
	 */
	@Test
	void testCorrectNumberAndTypeOfFunctions() {
		Application<Task, Dependency> application = builder.build();
		//should be 3 parallel functions
		assertEquals(3,application.getFunctions().size(),"application does not hat 3 required parallel functions");
		//should be one critical function
		int numberOfCriticalFunctions = 0;
		for(Function<Task, Dependency> function : application.getFunctions()) {
			if(isCriticalFunction(function)) {
				numberOfCriticalFunctions++;
			}
		}
		assertEquals(1, numberOfCriticalFunctions,"application should contain 1 safety critcal function");
	}
	
	
	//method for checking if number of start/end tasks correct
	@Test
	void testNumberOfStartEndTasks() {
		Application<Task, Dependency> application = builder.build();
		//check per function that it has 3 start/end tasks
		Set<Function<Task,Dependency>> functions = application.getFunctions();
		for(Function<Task,Dependency> function : functions) {
			assertEquals(3, getStartTasks(function).size(),"function does not have 3 start tasks");
			assertEquals(3, getEndTasks(function).size(),"function does not have 3 end tasks");
		}
	}
	
	/*
	 * checks if all tasks (not messages) in the given function have the safety critical annotation
	 * if so, returns true, otherwise false
	 */
	private boolean isCriticalFunction(Function<Task, Dependency> function) {
		for(Task task : function.getVertices()) {
			if(!(task instanceof Communication)) {
				if(task.getAttribute("safetyCritical") != null && !(boolean)task.getAttribute("safetyCritical")) {
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * method to get all start tasks of a function
	 */
	private Set<Task> getStartTasks(Function<Task, Dependency> function){
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
	private Set<Task> getEndTasks(Function<Task, Dependency> function){
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

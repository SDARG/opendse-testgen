package org.opendse.testcasegenerator.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class ParallelApplicationBuilderTest extends ApplicationBuilderTest{

	private ParallelApplicationBuilder builder;
	private Rand rand = new RandomMersenneTwister(0);
	
	@BeforeEach
	void setup() {
		//build a default application with 3 start/end tasks and 3 parallel functions
		this.builder = new ParallelApplicationBuilder(rand, NumberOfTasks.normal, 3, 3, 3, 0.5, true, 3);
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
	void testCorrectNumberOfParallelFunctions() {
		Application<Task, Dependency> application = builder.build();
		//should be 3 parallel functions
		assertEquals(3,application.getFunctions().size(),"application does not hat 3 required parallel functions");
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
	
	
	
}

package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.application.ApplicationBuilder;
import org.opendse.testcaseGenerator.application.MixedCriticalityApplicationBuilder;
import org.opendse.testcaseGenerator.application.ParallelApplicationBuilder;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Parent;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;

/**
 * Module to configure which type of application will be build and configure their shape.
 * Currently parallel functions and mixed-criticality functions are supported.
 */

@Parent(SpecBuilderModule.class)
public class ApplicationModule extends GeneratorModule{

	public enum NumberOfTasks {
		few, normal, many
	}
	
	@Info("How many Tasks do you want in your System. This will be a multiplier of the number of resources you have. Few is 0.7x taks, normal is 1x tasks and many is 1.5x tasks.")
	@Order(1)
	public NumberOfTasks amountOfTasks = NumberOfTasks.few;
	
	@Info("How many starttasks do you want in your System.")
	@Order(2)
	public int startTasks = 3;
	
	@Info("How many endtasks do you want in your System.")
	@Order(3)
	public int endTasks = 3;
	
	@Info("How wide you want your tasksgraph to be. This limits the amount of tasks that are paralell in the application.")
	@Order(4)
	public int maxwidth = 3;
	
	@Info("The ratio of how many previous tasks a new task depends on.")
	@Order(5)
	public double taskDependencyProbability = 0.5;
	
	@Info("Do you want messages to be created between your tasks. For default architectures setting this to false might result in unsolvable configurations.")
	@Order(6)
	public boolean addMessages = true;
	
	@Info("How many parallel functions are generated.")
	@Order(7)
	public int numberOfParallelFunctions = 3;
	
	@Info("Defines if a number of applications will be safety critical.")
	@Order(8)
	public boolean mixedCriticality = true;
	
	@Required(property = "mixedCriticality", elements = { "true" })
	@Info("Defines number of safety critical functions, can not be higher than number of total functions.")
	@Order(9)
	@Constant(value = "numberOfCriticalFunctions", namespace = MixedCriticalityApplicationBuilder.class)
	public int numberOfCriticalFunctions = 1;
	

	public int getNumberOfParallelFunctions() {
		return numberOfParallelFunctions;
	}

	public void setNumberOfParallelFunctions(int numberOfParallelFunctions) {
		this.numberOfParallelFunctions = numberOfParallelFunctions;
	}

	public boolean isMixedCriticality() {
		return mixedCriticality;
	}

	public void setMixedCriticality(boolean mixedCriticality) {
		this.mixedCriticality = mixedCriticality;
	}

	public int getNumberOfCriticalFunctions() {
		return numberOfCriticalFunctions;
	}

	public void setNumberOfCriticalFunctions(int numberOfCriticalFunctions) {
		this.numberOfCriticalFunctions = numberOfCriticalFunctions;
	}

	public int getStartTasks() {
		return startTasks;
	}

	public void setStartTasks(int startTasks) {
		this.startTasks = startTasks;
	}

	public int getEndTasks() {
		return endTasks;
	}

	public void setEndTasks(int endTasks) {
		this.endTasks = endTasks;
	}

	public double getTaskDependencyProbability() {
		return taskDependencyProbability;
	}

	public void setTaskDependencyProbability(double taskDependencyProbability) {
		this.taskDependencyProbability = taskDependencyProbability;
	}

	public int getMaxwidth() {
		return maxwidth;
	}

	public void setMaxwidth(int maxwidth) {
		this.maxwidth = maxwidth;
	}

	public NumberOfTasks getAmountOfTasks() {
		return amountOfTasks;
	}

	public void setAmountOfTasks(NumberOfTasks amountOfTasks) {
		this.amountOfTasks = amountOfTasks;
	}

	public boolean isAddMessages() {
		return addMessages;
	}

	public void setAddMessages(boolean addMessages) {
		this.addMessages = addMessages;
	}

	@Override
	protected void config() {
		if(mixedCriticality) {
			bind(ApplicationBuilder.class).to(MixedCriticalityApplicationBuilder.class);
			bindConstant("amountOfTasks", MixedCriticalityApplicationBuilder.class).to(amountOfTasks);
			bindConstant("startTasks", MixedCriticalityApplicationBuilder.class).to(startTasks);
			bindConstant("endTasks", MixedCriticalityApplicationBuilder.class).to(endTasks);
			bindConstant("maxwidth", MixedCriticalityApplicationBuilder.class).to(maxwidth);
			bindConstant("taskDependencyProbability", MixedCriticalityApplicationBuilder.class).to(taskDependencyProbability);
			bindConstant("addMessages", MixedCriticalityApplicationBuilder.class).to(addMessages);
			bindConstant("numberOfParallelFunctions", MixedCriticalityApplicationBuilder.class).to(numberOfParallelFunctions);
		}
		else {
			bind(ApplicationBuilder.class).to(ParallelApplicationBuilder.class);
			bindConstant("amountOfTasks", ParallelApplicationBuilder.class).to(amountOfTasks);
			bindConstant("startTasks", ParallelApplicationBuilder.class).to(startTasks);
			bindConstant("endTasks", ParallelApplicationBuilder.class).to(endTasks);
			bindConstant("maxwidth", ParallelApplicationBuilder.class).to(maxwidth);
			bindConstant("taskDependencyProbability", ParallelApplicationBuilder.class).to(taskDependencyProbability);
			bindConstant("addMessages", ParallelApplicationBuilder.class).to(addMessages);
			bindConstant("numberOfParallelFunctions", ParallelApplicationBuilder.class).to(numberOfParallelFunctions);
		}
	}


}

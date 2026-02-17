package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.ApplicationBuilder;
import org.opendse.testcaseGenerator.DefaultApplicationBuilder;
import org.opendse.testcaseGenerator.DefaultMapper;
import org.opendse.testcaseGenerator.modules.ArchitectureModule.ArchitectureType;
import org.opt4j.core.config.annotations.Category;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Parent;
import org.opt4j.core.start.Constant;

import com.google.inject.name.Named;

/*
 * Module to use DefaultApplicationBuilder and changing its settings 
 */

@Parent(SpecBuilderModule.class)
public class ApplicationModule extends GeneratorModule{

	public enum NumberOfTasks {
		few, normal, many
	}
	
	@Info("How many Tasks do you want in your System. This will be a multiplier of the number of resources you have. Few is 1x taks, normal is 1.5x tasks and many is 2x tasks.")
	@Order(1)
	@Constant(value = "amountOfTasks", namespace = DefaultApplicationBuilder.class)
	public NumberOfTasks amountOfTasks = NumberOfTasks.few;
	
	@Info("How many starttasks do you want in your System.")
	@Order(2)
	@Constant(value = "startTasks", namespace = DefaultApplicationBuilder.class)
	public int startTasks = 3;
	
	@Info("How many endtasks do you want in your System.")
	@Order(3)
	@Constant(value = "endTasks", namespace = DefaultApplicationBuilder.class)
	public int endTasks = 3;
	
	@Info("How wide you want your tasksgraph to be. This limits the amount of tasks that are paralell in the application.")
	@Order(4)
	@Constant(value = "maxwidth", namespace = DefaultApplicationBuilder.class)
	public int maxwidth = 3;
	
	@Info("The ratio of how many previous tasks a new task depends on.")
	@Order(5)
	@Constant(value = "taskDependencyProbability", namespace = DefaultApplicationBuilder.class)
	public double taskDependencyProbability = 0.5;
	
	@Info("Do you want messages to be created between your tasks. For default architectures setting this to false might result in unsolvable configurations.")
	@Order(6)
	@Constant(value = "addMessages", namespace = DefaultApplicationBuilder.class)
	public boolean addMessages = true;
	
	
	
	

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
		bind(ApplicationBuilder.class).to(DefaultApplicationBuilder.class);
	}


}

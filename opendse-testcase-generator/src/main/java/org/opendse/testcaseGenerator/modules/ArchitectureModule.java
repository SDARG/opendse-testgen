package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.DefaultApplicationBuilder;
import org.opendse.testcaseGenerator.TestcaseGenerator;
import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.GatewayBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.NoCBuilder;
import org.opendse.testcaseGenerator.modelextension.Actuator;
import org.opendse.testcaseGenerator.modelextension.Bus;
import org.opendse.testcaseGenerator.modelextension.CANBus;
import org.opendse.testcaseGenerator.modelextension.CommInterface;
import org.opendse.testcaseGenerator.modelextension.Cpu;
import org.opendse.testcaseGenerator.modelextension.EthernetBus;
import org.opendse.testcaseGenerator.modelextension.Gateway;
import org.opendse.testcaseGenerator.modelextension.NoCRouter;
import org.opendse.testcaseGenerator.modelextension.Sensor;
import org.opt4j.core.config.annotations.Category;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Parent;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;

import com.google.inject.name.Named;

/*
 * Module to select ArchitectureType and changing architecture generation settings
 */
@Parent(SpecBuilderModule.class)
public class ArchitectureModule extends GeneratorModule{

	public enum ArchitectureType {
		Gateway,NoC
	}
	
	@Info("The basic type of the architecture.")
	@Order(1)
	@Constant(value = "type", namespace = TestcaseGenerator.class)
	public ArchitectureType type = ArchitectureType.Gateway;
	
	@Required(property = "type", elements = { "Gateway" })
	@Order(2)
	@Info("Should the gateway architecture come with a multibus layout.")
	@Constant(value = "multibus", namespace = GatewayBuilder.class)
	public boolean multibus = true;
	
	@Required(property = "type", elements = { "Gateway" })
	@Order(3)
	@Info("How many buses should the gateway connect.")
	@Constant(value = "busNumber", namespace = GatewayBuilder.class)
	public int numberOfBuses = 2;
	
	@Required(property = "type", elements = { "Gateway" })
	@Info("How many resources should each bus have at maximum.")
	@Constant(value = "resourceNumber", namespace = GatewayBuilder.class)
	public int maxNumberofResources = 2;
	
	
	@Required(property = "type", elements = { "NoC" })
	@Order(2)
	@Info("The x size of the NoC. Minimum 2.")
	@Constant(value = "xsize", namespace = NoCBuilder.class)
	public int xsize = 2;
	
	@Required(property = "type", elements = { "NoC" })
	@Order(3)
	@Info("The y size of the NoC. Minimum 2.")
	@Constant(value = "ysize", namespace = NoCBuilder.class)
	public int ysize = 2;
	
	@Order(4)
	@Info("Extended settings for parameter ranges of resources.")
	public boolean configureResourceParameter = false;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(5)
	private double minPower = 10.0;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(6)
	private double maxPower = 20.0;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(7)
	private double minArea = 10.0;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(8)
	private double maxArea = 20.0;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(9)
	private double minFailureProbability = 0.005;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(10)
	private double maxFailureProbability = 0.05;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(11)
	private double minThroughput = 50;
	
	@Required(property = "configureResourceParameter", elements = { "true" })
	@Order(12)
	private double maxThroughput = 100;
		
	public double getMinPower() {
		return minPower;
	}

	public void setMinPower(double minPower) {
		this.minPower = minPower;
	}

	public double getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(double maxPower) {
		this.maxPower = maxPower;
	}

	public double getMinArea() {
		return minArea;
	}

	public void setMinArea(double minArea) {
		this.minArea = minArea;
	}

	public double getMaxArea() {
		return maxArea;
	}

	public void setMaxArea(double maxArea) {
		this.maxArea = maxArea;
	}

	public double getMinFailureProbability() {
		return minFailureProbability;
	}

	public void setMinFailureProbability(double minFailureProbability) {
		this.minFailureProbability = minFailureProbability;
	}

	public double getMaxFailureProbability() {
		return maxFailureProbability;
	}

	public void setMaxFailureProbability(double maxFailureProbability) {
		this.maxFailureProbability = maxFailureProbability;
	}

	public double getMinThroughput() {
		return minThroughput;
	}

	public void setMinThroughput(double minThroughput) {
		this.minThroughput = minThroughput;
	}

	public double getMaxThroughput() {
		return maxThroughput;
	}

	public void setMaxThroughput(double maxThroughput) {
		this.maxThroughput = maxThroughput;
	}

	public boolean isConfigureResourceParameter() {
		return configureResourceParameter;
	}

	public void setConfigureResourceParameter(boolean configureResourceParameter) {
		this.configureResourceParameter = configureResourceParameter;
	}

	public ArchitectureType getType() {
		return type;
	}

	public void setType(ArchitectureType type) {
		this.type = type;
	}

	public boolean isMultibus() {
		return multibus;
	}

	public void setMultibus(boolean multibus) {
		this.multibus = multibus;
	}

	public int getNumberOfBuses() {
		return numberOfBuses;
	}

	public void setNumberOfBuses(int numberOfBuses) {
		this.numberOfBuses = numberOfBuses;
	}

	public int getMaxNumberofResources() {
		return maxNumberofResources;
	}

	public void setMaxNumberofResources(int maxNumberofResources) {
		this.maxNumberofResources = maxNumberofResources;
	}

	public int getXsize() {
		return xsize;
	}

	public void setXsize(int xsize) {
		this.xsize = xsize;
	}

	public int getYsize() {
		return ysize;
	}

	public void setYsize(int ysize) {
		this.ysize = ysize;
	}

	@Override
	protected void config() {
		switch(type) {
		case ArchitectureType.Gateway:
			bind(ArchitectureBuilder.class).to(GatewayBuilder.class);
			break;
		case ArchitectureType.NoC:
			bind(ArchitectureBuilder.class).to(NoCBuilder.class);
			break;
		default:
			bind(ArchitectureBuilder.class).to(GatewayBuilder.class);
		break;
		}
		setResourceRanges();
	}
	
	private void setResourceRanges(){
		CANBus.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability, minThroughput, maxThroughput);
		Bus.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability, minThroughput, maxThroughput);
		EthernetBus.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability, minThroughput, maxThroughput);
		CommInterface.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability, minThroughput, maxThroughput);
		Gateway.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability, minThroughput, maxThroughput);
		NoCRouter.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability, minThroughput, maxThroughput);
		Actuator.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability);
		Sensor.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability);
		Cpu.setAttributeBoundaries(minPower, maxPower, minArea, maxArea, minFailureProbability, maxFailureProbability);
	}
}

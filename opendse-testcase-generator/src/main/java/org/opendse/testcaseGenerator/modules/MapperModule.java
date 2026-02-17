package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.DefaultMapper;
import org.opendse.testcaseGenerator.Mapper;
import org.opt4j.core.config.annotations.Category;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Parent;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;

import com.google.inject.name.Named;

/*
 * Module to use DefaultMapper and changing its settings 
 */
@Parent(SpecBuilderModule.class)
public class MapperModule extends GeneratorModule{

	
	@Info("The percentage of sensors every sensortask is mapped to.")
	@Order(2)
	@Constant(value = "sensorConnectivity", namespace = DefaultMapper.class)
	public double sensorConnectivity = 0.8;
	
	@Info("The percentage of actuators every actuatortask is mapped to.")
	@Order(3)
	@Constant(value = "actuatorConnectivity", namespace = DefaultMapper.class)
	public double actuatorConnectivity = 0.8;
	
	@Info("The percentage of processors every processtask is mapped to.")
	@Order(1)
	@Constant(value = "processorConnectivity", namespace = DefaultMapper.class)
	public double processorConnectivity = 0.3;
	
	@Order(4)
	@Info("Extended settings for parameter ranges of mappings.")
	public boolean configureMappingParameter = false;
	
	@Info("The minimum power every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(4)
	@Constant(value = "minPower", namespace = DefaultMapper.class)
	public double minPower = 1.0;
	
	@Info("The maximum power every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(5)
	@Constant(value = "maxPower", namespace = DefaultMapper.class)
	public double maxPower = 5.0;
	
	@Info("The minimum delay every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(6)
	@Constant(value = "mindelay", namespace = DefaultMapper.class)
	public double mindelay = 3.0;
	
	@Info("The maximum delay every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(7)
	@Constant(value = "maxdelay", namespace = DefaultMapper.class)
	public double maxdelay = 10.0;
	
	@Info("The minimum period every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(8)
	@Constant(value = "minperiod", namespace = DefaultMapper.class)
	public double minperiod = 100.0;
	
	@Info("The maximum period every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(9)
	@Constant(value = "maxperiod", namespace = DefaultMapper.class)
	public double maxperiod = 100.0;
	
	@Info("The minimum reliability every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(10)
	@Constant(value = "minReliability", namespace = DefaultMapper.class)
	public double minReliability = 0.005;
	
	@Info("The maximum reliability every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(11)
	@Constant(value = "maxReliability", namespace = DefaultMapper.class)
	public double maxReliability = 0.05;
	
	
	
	



	public boolean isConfigureMappingParameter() {
		return configureMappingParameter;
	}



	public void setConfigureMappingParameter(boolean configureMappingParameter) {
		this.configureMappingParameter = configureMappingParameter;
	}



	public double getSensorConnectivity() {
		return sensorConnectivity;
	}



	public void setSensorConnectivity(double sensorConnectivity) {
		this.sensorConnectivity = sensorConnectivity;
	}



	public double getActuatorConnectivity() {
		return actuatorConnectivity;
	}



	public void setActuatorConnectivity(double actuatorConnectivity) {
		this.actuatorConnectivity = actuatorConnectivity;
	}



	public double getProcessorConnectivity() {
		return processorConnectivity;
	}



	public void setProcessorConnectivity(double processorConnectivity) {
		this.processorConnectivity = processorConnectivity;
	}



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



	public double getMindelay() {
		return mindelay;
	}



	public void setMindelay(double mindelay) {
		this.mindelay = mindelay;
	}



	public double getMaxdelay() {
		return maxdelay;
	}



	public void setMaxdelay(double maxdelay) {
		this.maxdelay = maxdelay;
	}



	public double getMinperiod() {
		return minperiod;
	}



	public void setMinperiod(double minperiod) {
		this.minperiod = minperiod;
	}



	public double getMaxperiod() {
		return maxperiod;
	}



	public void setMaxperiod(double maxperiod) {
		this.maxperiod = maxperiod;
	}



	public double getMinReliability() {
		return minReliability;
	}



	public void setMinReliability(double minReliability) {
		this.minReliability = minReliability;
	}



	public double getMaxReliability() {
		return maxReliability;
	}



	public void setMaxReliability(double maxReliability) {
		this.maxReliability = maxReliability;
	}



	@Override
	protected void config() {
		bind(Mapper.class).to(DefaultMapper.class);	
	}
	
}

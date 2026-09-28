package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.mappings.DefaultMapper;
import org.opendse.testcaseGenerator.mappings.LimitedMapper;
import org.opendse.testcaseGenerator.mappings.Mapper;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Parent;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;

/**
 * Module to define how mappings between tasks and resources will be created.
 */
@Parent(SpecBuilderModule.class)
public class MapperModule extends GeneratorModule{
	
	public enum MappingType{
		CONNECTIVITY, LIMIT
	}
	
	@Info("How number of mappings per task is determined. Connectivity sets a percentage that defines number of mappings in relation to number of resources."
			+ "Limited defines an absolute upper bound for number of mappings per task.")
	@Order(1)
	public MappingType type = MappingType.LIMIT;
	
	@Required(property = "type", elements = { "CONNECTIVITY" })
	@Info("The percentage of processors every processtask is mapped to.")
	@Order(2)
	@Constant(value = "processorConnectivity", namespace = DefaultMapper.class)
	public double processorConnectivity = 0.3;

	@Required(property = "type", elements = { "CONNECTIVITY" })
	@Info("The percentage of sensors every sensortask is mapped to.")
	@Order(3)
	@Constant(value = "sensorConnectivity", namespace = DefaultMapper.class)
	public double sensorConnectivity = 0.8;
	
	@Required(property = "type", elements = { "CONNECTIVITY" })
	@Info("The percentage of actuators every actuatortask is mapped to.")
	@Order(4)
	@Constant(value = "actuatorConnectivity", namespace = DefaultMapper.class)
	public double actuatorConnectivity = 0.8;
	
	@Required(property = "type", elements = { "LIMIT" })
	@Info("The limit for mappings per task.")
	@Order(5)
	@Constant(value = "mappingLimit", namespace = LimitedMapper.class)
	public int mappingLimit = 3;
	
	@Order(6)
	@Info("Extended settings for parameter ranges of mappings.")
	public boolean configureMappingParameter = false;
	
	@Info("The minimum power every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(7)
	public double minPower = 1.0;
	
	@Info("The maximum power every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(8)
	public double maxPower = 5.0;
	
	@Info("The minimum delay every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(9)
	public double mindelay = 3.0;
	
	@Info("The maximum delay every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(10)
	public double maxdelay = 10.0;
	
	@Info("The minimum period every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(11)
	public double minperiod = 100.0;
	
	@Info("The maximum period every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(12)
	public double maxperiod = 100.0;
	
	@Info("The minimum reliability every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(13)
	public double minReliability = 0.005;
	
	@Info("The maximum reliability every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(14)
	public double maxReliability = 0.05;
	
	@Info("The minimum capacity every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(15)
	public int minCapacity = 1;
	
	@Info("The maximum capacity every mapping gets assigned.")
	@Required(property = "configureMappingParameter", elements = { "true" })
	@Order(16)
	public int maxCapacity = 1;
	
	public int getMinCapacity() {
		return minCapacity;
	}

	public void setMinCapacity(int minCapacity) {
		this.minCapacity = minCapacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public MappingType getType() {
		return type;
	}

	public void setType(MappingType type) {
		this.type = type;
	}

	public int getMappingLimit() {
		return mappingLimit;
	}

	public void setMappingLimit(int mappingLimit) {
		this.mappingLimit = mappingLimit;
	}

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
		switch(type) {
		case CONNECTIVITY:
			bind(Mapper.class).to(DefaultMapper.class);	
			bindConstant("minPower", DefaultMapper.class).to(minPower);
			bindConstant("maxPower", DefaultMapper.class).to(maxPower);
			bindConstant("mindelay", DefaultMapper.class).to(mindelay);
			bindConstant("maxdelay", DefaultMapper.class).to(maxdelay);
			bindConstant("minperiod", DefaultMapper.class).to(minperiod);
			bindConstant("maxperiod", DefaultMapper.class).to(maxperiod);
			bindConstant("minReliability", DefaultMapper.class).to(minReliability);
			bindConstant("maxReliability", DefaultMapper.class).to(maxReliability);
			bindConstant("minCapacity", DefaultMapper.class).to(minCapacity);
			bindConstant("maxCapacity", DefaultMapper.class).to(maxCapacity);
			break;
		case LIMIT:
			bind(Mapper.class).to(LimitedMapper.class);	
			bindConstant("minPower", LimitedMapper.class).to(minPower);
			bindConstant("maxPower", LimitedMapper.class).to(maxPower);
			bindConstant("mindelay", LimitedMapper.class).to(mindelay);
			bindConstant("maxdelay", LimitedMapper.class).to(maxdelay);
			bindConstant("minperiod", LimitedMapper.class).to(minperiod);
			bindConstant("maxperiod", LimitedMapper.class).to(maxperiod);
			bindConstant("minReliability", LimitedMapper.class).to(minReliability);
			bindConstant("maxReliability", LimitedMapper.class).to(maxReliability);
			bindConstant("minCapacity", LimitedMapper.class).to(minCapacity);
			bindConstant("maxCapacity", LimitedMapper.class).to(maxCapacity);
			break;
		default:
			bind(Mapper.class).to(LimitedMapper.class);
			bindConstant("minPower", LimitedMapper.class).to(minPower);
			bindConstant("maxPower", LimitedMapper.class).to(maxPower);
			bindConstant("mindelay", LimitedMapper.class).to(mindelay);
			bindConstant("maxdelay", LimitedMapper.class).to(maxdelay);
			bindConstant("minperiod", LimitedMapper.class).to(minperiod);
			bindConstant("maxperiod", LimitedMapper.class).to(maxperiod);
			bindConstant("minReliability", LimitedMapper.class).to(minReliability);
			bindConstant("maxReliability", LimitedMapper.class).to(maxReliability);
			bindConstant("minCapacity", LimitedMapper.class).to(minCapacity);
			bindConstant("maxCapacity", LimitedMapper.class).to(maxCapacity);
			break;
		}
		
	}
	
}

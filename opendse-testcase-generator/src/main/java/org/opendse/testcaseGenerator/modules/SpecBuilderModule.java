package org.opendse.testcaseGenerator.modules;


import org.opendse.testcaseGenerator.TestcaseGenerator;
import org.opt4j.core.config.annotations.File;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;

import com.google.inject.name.Named;

/*
 * Module to change basic specification settings
 */
public class SpecBuilderModule extends GeneratorModule {

	@Info("The number of Specifications to be generated.")
	@Order(0)
	@Constant(namespace = TestcaseGenerator.class, value = "numberOfGeneratedSpecs")
	public int numberOfGeneratedSpecs = 1;
	
	@Info("Do you want to see your Specifications.")
	@Order(1)
	@Constant(namespace = TestcaseGenerator.class, value = "showSpecs")
	public boolean showSpecs = false;
	
	@File(folder = true, file = false)
	@Order(2)
	@Constant(namespace = TestcaseGenerator.class, value = "outputFolder")
	protected String outputFolder = "resources";

	@Info("Do you want to set a custom naming scheme.")
	@Order(3)
	@Constant(namespace = TestcaseGenerator.class, value = "custom")
	public boolean customName = false;
	
	@Required(property = "customName", elements = { "true" })
	@Order(4)
	@Constant(namespace = TestcaseGenerator.class, value = "customName")
	public String customFileName = "customName";
	
	public boolean isCustomName() {
		return customName;
	}


	public void setCustomName(boolean customName) {
		this.customName = customName;
	}


	public String getCustomFileName() {
		return customFileName;
	}


	public void setCustomFileName(String customFileName) {
		this.customFileName = customFileName;
	}
		
	public boolean isShowSpecs() {
		return showSpecs;
	}


	public String getOutputFolder() {
		return outputFolder;
	}


	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}


	public void setShowSpecs(boolean showSpecs) {
		this.showSpecs = showSpecs;
	}


	public int getNumberOfGeneratedSpecs() {
		return numberOfGeneratedSpecs;
	}


	public void setNumberOfGeneratedSpecs(@Named("numberSpecs") int numberOfGeneratedSpecs) {
		this.numberOfGeneratedSpecs = numberOfGeneratedSpecs;
	}
	
	@Override
	protected void config() {
		bind(TestcaseGenerator.class);
	}
}

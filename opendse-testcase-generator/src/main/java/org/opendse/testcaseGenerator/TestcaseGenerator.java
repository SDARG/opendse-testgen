package org.opendse.testcaseGenerator;

import org.opendse.testcaseGenerator.modules.ArchitectureModule.ArchitectureType;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import net.sf.opendse.io.SpecificationReader;
import net.sf.opendse.io.SpecificationWriter;
import net.sf.opendse.model.Specification;
import net.sf.opendse.visualization.SpecificationViewer;

/**
 *  The TestcaseGenerator class manages the creation, naming and saving of specifications
 */
public class TestcaseGenerator {

	SpecificationBuilder builder;
	SpecificationWriter writer;
	SpecificationReader reader;
	String filename;
	int numberOfSpecs;
	boolean showSpecs;
	
	
	@Inject
	public TestcaseGenerator(
			@Constant(namespace = TestcaseGenerator.class, value = "numberOfGeneratedSpecs") int numberOfGeneratedSpecs,
			@Constant(namespace = TestcaseGenerator.class, value = "showSpecs") boolean showSpecs,
			@Constant(namespace = TestcaseGenerator.class, value = "outputFolder") String outputFolder,
			@Constant(value = "type", namespace = TestcaseGenerator.class) ArchitectureType type,
			@Constant(namespace = TestcaseGenerator.class, value = "custom")boolean custom ,
			@Constant(namespace = TestcaseGenerator.class, value = "customName") String customName,
			Injector injector) {	
	builder = injector.getInstance(SpecificationBuilder.class);
	writer = new SpecificationWriter();
	reader = new SpecificationReader();
	numberOfSpecs = numberOfGeneratedSpecs;
	this.showSpecs = showSpecs;
	if(custom) {
	this.filename = outputFolder+"/"+customName;
	}else {
	this.filename = outputFolder+"/"+type.toString();
	}
	
	}
	
	/*
	 * Generates am amount of {@link Specifications} as specified by numberOfSpecs
	 */
	public void GenerateTestcases() {
		for(int i = 1; i <= numberOfSpecs; i++) {
			GenerateTestcase(i);
		}
	}

	/*
	 * Generates a single  {@link Specifications} and writes it to the file specified by filename
	 * 	@param number
	 * 				the number added at the end of the filename to differentiate
	 */
	public void GenerateTestcase(int number) {
	
	Specification specification = builder.build();	
	writer.write(specification, filename+number+".xml");
	if(showSpecs) SpecificationViewer.view(specification,false);
	}
	
	
}

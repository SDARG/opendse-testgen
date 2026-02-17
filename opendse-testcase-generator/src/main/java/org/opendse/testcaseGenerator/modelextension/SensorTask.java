package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Task;
/**
 * The SensorTask is a class to model a simple sensor function 
 */
public class SensorTask extends Function{

	public SensorTask(Element parent) {
		super(parent);		
		
	}
	public SensorTask(String id) {
		super(id);
		
	}
}

package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;
/**
 * The ActuatorTask is a class to model a simple actuator function 
 */
public class ActuatorTask extends Function{

	public ActuatorTask(Element parent) {
		super(parent);		
		
	}
	public ActuatorTask(String id) {
		super(id);
		
	}
}

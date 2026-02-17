package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Task;

/**
 * The Function is a class to model a Task that is supposed to have mappings 
 */
public abstract class Function extends Task{

	public Function(Element parent) {
		super(parent);		
		
	}
	public Function(String id) {
		super(id);
		
	}
}

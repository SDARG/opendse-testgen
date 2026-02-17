package org.opendse.testcaseGenerator.modelextension;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Task;
/**
 * The CpuTask is a class to model a simple processing function 
 */
public class CpuTask extends Function{

	public CpuTask(Element parent) {
		super(parent);		
		
	}
	public CpuTask(String id) {
		super(id);
		
	}
}

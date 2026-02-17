package org.opendse.testcaseGenerator.modelextension;

import org.opt4j.core.common.random.Rand;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Resource;

/**
 * The FunctionalResource is a class to model a resource that is supposed to have mappings
 */
public abstract class FunctionalResource extends Resource{

	public static Rand rand;
	
	public FunctionalResource(Element parent) {
		super(parent);
				
	}
	public FunctionalResource(String id) {
		super(id);
	}
	
	
	

}


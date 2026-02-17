package org.opendse.testcaseGenerator.modelextension;

import org.opt4j.core.common.random.Rand;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Resource;


/**
 * The CommunicationResource is a class to model a Resource that is supposed to have no mapping to 
 */
public abstract class CommunicationResource extends Resource{

	public static Rand rand;
	
	public CommunicationResource(Element parent) {
		super(parent);
				
	}
	public CommunicationResource(String id) {
		super(id);
	}
	
	
	

}

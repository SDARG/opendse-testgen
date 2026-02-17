package org.opendse.testcaseGenerator.utils;

import java.util.ArrayList;

import net.sf.opendse.io.SpecificationReader;
import net.sf.opendse.model.Specification;
import net.sf.opendse.visualization.SpecificationViewer;

public class Viewer {

	public static void main(String[] args) {
	
		
		String[] specs = {"resources/Specification1","resources/Specification2"};
		SpecificationReader reader = new SpecificationReader();
		ArrayList<Specification> specifications = new ArrayList<Specification>();
		for(String spec : specs) {
			specifications.add(reader.read(spec+".xml"));
		}
		for(Specification spec : specifications) {
			SpecificationViewer.view(spec);
		}
	}

}

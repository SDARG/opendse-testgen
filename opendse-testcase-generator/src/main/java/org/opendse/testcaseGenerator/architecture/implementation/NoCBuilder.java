package org.opendse.testcaseGenerator.architecture.implementation;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.architecture.implementation.TileBuilder.TileType;
import org.opendse.testcaseGenerator.modelextension.CommunicationResource;
import org.opendse.testcaseGenerator.modelextension.NoCRouter;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

/*
 * The NoCBuilder is used to create a ordered grid of NoCRouters
 */
public class NoCBuilder extends ArchitectureBuilder{

	private int xsize;
	private int ysize;
	Rand rand;
	
	@Inject
	public NoCBuilder(
			@Constant(value = "xsize", namespace = NoCBuilder.class) int xsize,
			@Constant(value = "ysize", namespace = NoCBuilder.class) int ysize,
			Rand rand) {
		
		this.xsize = xsize;
		this.ysize = ysize;
		this.rand = rand;
		
		//guarantees a minimum size
		if(ysize < 2) this.ysize = 2;
		if(xsize < 2) this.xsize = 2;
	}
	
	
	
	/*
	 * Builds the NoCRouter Structure with sizes according to xsize and ysize
	 */
@Override
	public void build() {
		/*
		 * reset counters for the architecture
		 */
		ArchitectureBuilder.resetCounters();
		this.architecture = new Architecture<Resource, Link>();		
		ArrayList<Resource> resourceList = new ArrayList<Resource>();
		ArrayList<Link> linkList = new ArrayList<Link>();
		NoCRouter temp;
		int linkCount = ysize*(xsize-1) + (ysize-1)*xsize;
		
		/**
		 * generates all resources as specified by parameters
		 */
		for(int i = 1; i<= ysize; i++)
		{
			for(int j = 1; j<= xsize;j++ )
			{
				temp = new NoCRouter("NoC-"+j+"-"+i,j,i);
				temp.setAttributes();
				resourceList.add(temp);
				
			}
		}
		/**
		 * adds all generated resources to the architecture
		 */
		for(Resource NoC : resourceList) {
			architecture.addVertex(NoC);
		}
		/**
		 * generates number of Links required to connect the grid
		 */
		for(int i = 1; i <= linkCount;i++) {
			linkList.add(new Link("l"+i));
		}
		/**
		 * adds all edges to the architecture
		 */
		int linkCounter = 0;
		for(int i = 0; i< ysize; i++)
		{
			for(int j = 0; j< xsize-1;j++ )
			{
				architecture.addEdge(linkList.get(linkCounter), resourceList.get(j+(i*xsize)), resourceList.get(j+(i*xsize)+1));		
				linkCounter++;
			}
		}
		for(int i = 0; i< ysize-1; i++)
		{
			for(int j = 0; j< xsize;j++ )
			{
				architecture.addEdge(linkList.get(linkCounter), resourceList.get(j+(i*xsize)), resourceList.get(j+((i+1)*xsize)));		
				linkCounter++;
			}
		}
		fillTiles();


		
		
		
	}
	/*
	 * Uses a TileBuilder to attach a substructure with FunctionalResources to each NoCRouter
	 */
	public void fillTiles() {
		ArrayList<Resource> resourceList= new ArrayList<Resource>(architecture.getVertices());
		TileBuilder builder = new TileBuilder(TileType.CORE,architecture,resourceList.get(0),rand);
		for(Resource NoC : resourceList) {
			
			
			builder.setConnector(NoC);
			builder.build();
			
			
		}
				
	}

}

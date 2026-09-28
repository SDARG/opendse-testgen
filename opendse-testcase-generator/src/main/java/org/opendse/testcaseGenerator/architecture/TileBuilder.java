package org.opendse.testcaseGenerator.architecture;

import java.util.ArrayList;
import java.util.Random;

import org.opendse.testcaseGenerator.modelextension.CommInterface;
import org.opendse.testcaseGenerator.modelextension.Cpu;
import org.opt4j.core.common.random.Rand;

import com.google.inject.Inject;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;


/*
 * The TileBuilder creates and attaches a substructure with FunctionalResources to a given Resource
 */
public class TileBuilder  extends ArchitectureBuilder{

	public  enum TileType{
		RAM,
		CORE,
		DATA
	}
	protected TileType currentType;
	protected Resource connector;
	protected final Rand rand;
	
	public TileBuilder(TileType type,Architecture<Resource,Link> architecture,Resource connector, Rand rand){
		currentType = type;
		this.architecture = architecture;
		this.connector = connector;
		this.rand = rand;
	}
	
	
	/*
	 * Builds a Tile with the currently active Type to the connector
	 */
	@Override
	public Architecture<Resource, Link> build() {
		switch(currentType) {
		case RAM: 
			buildRAMTile();
			break;
		case CORE: 
			buildCoreTile();
			break;
		case DATA: 
			buildDATATile();
			break;
		default: 
			buildCoreTile();
			break;
		}	
		return architecture;
	}
	/*
	 * currently not used by the NoCBuilder
	 * application doesnt generate tasks for these resources, mapper wont map anything to them
	 */
	private void buildDATATile() {
		ArrayList<Resource> ResourceList = new ArrayList<Resource>();
		CommInterface cInterface = new CommInterface("CommInterface"+usecInterfaceCounter());
		cInterface.setAttributes();
		architecture.addVertex(cInterface);
		Resource tempResource;
		Link tempLink;
		tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
		architecture.addEdge(tempLink, connector,cInterface);
		for(int i = 0; i <= rand.nextInt(3)+1; i++){
			tempResource = new Resource("dataStorage"+usedataCounter());
			ResourceList.add(tempResource);
			tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(tempLink, cInterface, tempResource);
		}
		
	}

	/*
	 * Creates up to 8 Cpus and connects them to the connector with a CommInterface
	 */
	private void buildCoreTile() {
		ArrayList<Resource> ResourceList = new ArrayList<Resource>();
		CommInterface cInterface = new CommInterface("CommInterface"+usecInterfaceCounter());
		cInterface.setAttributes();
		architecture.addVertex(cInterface);
		Cpu tempResource;
		Link tempLink;
		tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
		architecture.addEdge(tempLink, connector,cInterface);
		for(int i = 0; i <= rand.nextInt(7)+1; i++){
			tempResource = new Cpu("core"+usecpuCounter());
			tempResource.setAttributes();
			ResourceList.add(tempResource);
			tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(tempLink, cInterface, tempResource);
		}
		
	}

	/*
	 * currently not used by the NoCBuilder
	 * application doesnt generate tasks for these resources, mapper wont map anything to them
	 */
	private void buildRAMTile() {
		ArrayList<Resource> ResourceList = new ArrayList<Resource>();
		CommInterface cInterface = new CommInterface("CommInterface"+usecInterfaceCounter());
		cInterface.setAttributes();
		architecture.addVertex(cInterface);
		Resource tempResource;
		Link tempLink;
		tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
		architecture.addEdge(tempLink, connector,cInterface);
		for(int i = 0; i <= rand.nextInt(3)+1; i++){
			tempResource = new Resource("ram"+useramCounter());
			ResourceList.add(tempResource);
			tempLink = new Link("link"+ArchitectureBuilder.useedgeCounter());
			architecture.addEdge(tempLink, cInterface, tempResource);
		}
	}

	/*
	 * changes the Type of the Tile that should be created
	 */
	public void setType(TileType type) {
		currentType = type;
	}
	
	/*
	 * sets the Resource to which the tile should be connected
	 */
	public void setConnector(Resource connector) {
		this.connector = connector;
	}
	

	
}

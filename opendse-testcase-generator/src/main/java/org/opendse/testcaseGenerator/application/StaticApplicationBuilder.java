package org.opendse.testcaseGenerator.application;

import org.opendse.testcaseGenerator.architecture.ArchitectureBuilder;
import org.opendse.testcaseGenerator.modelextension.ActuatorTask;
import org.opendse.testcaseGenerator.modelextension.CpuTask;
import org.opendse.testcaseGenerator.modelextension.SensorTask;

import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;


/**
 * Version of ApplicationBuilder that builds a small 5 Task Application with Messages
 */
public class StaticApplicationBuilder extends ApplicationBuilder{

	@Override
	public Application<Task, Dependency> build() {
		resetCounters();
		application = new Application<Task,Dependency>();
		Task t1;
		if(ArchitectureBuilder.getSensorCounter() != 1) {
			t1 = new SensorTask("t"+usetaskCounter());
			application.addVertex(t1);
			
		}else {
			t1 = new CpuTask("t"+usetaskCounter());
			application.addVertex(t1);
		}
		Task t2 = new CpuTask("t"+usetaskCounter());
		Task t3 = new CpuTask("t"+usetaskCounter());
		Task t4 = new CpuTask("t"+usetaskCounter());
		Task m1 = new Communication("c"+usecommCounter());
		Task m2 = new Communication("c"+usecommCounter());
		Task m3 = new Communication("c"+usecommCounter());
		Task m4 = new Communication("c"+usecommCounter());
		
		Task t5;
		if(ArchitectureBuilder.getActuatorCounter() != 1) {
			t5 = new ActuatorTask("t"+usetaskCounter());
			application.addVertex(t5);
			
		}else {
			t5 = new CpuTask("t"+usetaskCounter());
			application.addVertex(t5);
		}
		application.addEdge(new Dependency("d"+usedependencyCounter()),t1,m1);
		application.addEdge(new Dependency("d"+usedependencyCounter()),m1,t2);
		application.addEdge(new Dependency("d"+usedependencyCounter()),m1,t3);
		application.addEdge(new Dependency("d"+usedependencyCounter()),t2,m2);
		application.addEdge(new Dependency("d"+usedependencyCounter()),t3,m3);
		application.addEdge(new Dependency("d"+usedependencyCounter()),m2,t4);
		application.addEdge(new Dependency("d"+usedependencyCounter()),m3,t4);
		application.addEdge(new Dependency("d"+usedependencyCounter()),t4,m4);
		application.addEdge(new Dependency("d"+usedependencyCounter()),m4,t5);
		return application;
	}

}

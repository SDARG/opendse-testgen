package org.opendse.testcasegenerator.mappings;

import java.util.Set;

import org.opendse.testcasegenerator.modelextension.Actuator;
import org.opendse.testcasegenerator.modelextension.ActuatorTask;
import org.opendse.testcasegenerator.modelextension.Cpu;
import org.opendse.testcasegenerator.modelextension.Sensor;
import org.opendse.testcasegenerator.modelextension.SensorTask;

import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/*
 * abstract class to provide methods for different concrete mapper test classes
 */
public abstract class MapperTest {
	
	/*
	 * returns true if task is mapped to correct type of resources
	 */
	protected boolean mappedToCorrectResourceType(Task task, Set<Resource> targets) {
		/*
		 * just remove all resources of correct type from given set
		 * if size of set afterwards is 0, mappings are correct
		 */
		if(task instanceof SensorTask) {
			targets.removeIf((Resource r) -> r instanceof Sensor);
			return targets.size() == 0;
		}
		else if(task instanceof ActuatorTask) {
			targets.removeIf((Resource r) -> r instanceof Actuator);
			return targets.size() == 0;
		}
		else {
			targets.removeIf((Resource r) -> r instanceof Cpu);
			return targets.size() == 0;
		}
	}

}

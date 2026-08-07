package org.opendse.testcaseGenerator.visualization;

import org.opendse.testcaseGenerator.GeneratorTask;
import org.opt4j.core.config.Starter;

/*
 * Starter that uses GeneratorTask
 */

public class GeneratorStarter extends Starter {

	/**
	 * Starts the configuration files.
	 * 
	 * @param args
	 *            the files
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Starter starter = new GeneratorStarter();
		starter.execute(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opt4j.config.Starter#execute(java.lang.String[])
	 */
	@Override
	public void execute(String[] args) throws Exception {
		addPlugins();
		execute(GeneratorTask.class, args);
	}
}

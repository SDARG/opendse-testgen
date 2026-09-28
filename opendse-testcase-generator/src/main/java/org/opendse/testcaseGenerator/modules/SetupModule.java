package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.visualization.GeneratorApplicationFrame;
import org.opendse.testcaseGenerator.visualization.GeneratorExecutionEnvironment;
import org.opendse.testcaseGenerator.visualization.GeneratorModuleRegister;
import org.opendse.testcaseGenerator.visualization.GeneratorToolBar;
import org.opt4j.core.config.ExecutionEnvironment;
import org.opt4j.core.config.ModuleRegister;
import org.opt4j.core.config.visualization.ApplicationFrame;
import org.opt4j.core.config.visualization.ToolBar;
import org.opt4j.core.start.Opt4JModule;

/*
 * Module to override default bindings, makes the generator to show instead of Opt4J
 */
public class SetupModule extends Opt4JModule{

	@Override
	protected void config() {
		bind(ApplicationFrame.class).to(GeneratorApplicationFrame.class);
		bind(ToolBar.class).to(GeneratorToolBar.class);
		bind(ModuleRegister.class).to(GeneratorModuleRegister.class);
		bind(ExecutionEnvironment.class).to(GeneratorExecutionEnvironment.class);
	}

}

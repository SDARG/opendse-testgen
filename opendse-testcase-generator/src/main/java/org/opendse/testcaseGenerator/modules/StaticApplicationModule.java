package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.ApplicationBuilder;
import org.opendse.testcaseGenerator.DefaultApplicationBuilder;
import org.opendse.testcaseGenerator.StaticApplicationBuilder;
import org.opt4j.core.config.annotations.Parent;

/*
 * Module to use StaticApplicationBuilder
 */

@Parent(SpecBuilderModule.class)
public class StaticApplicationModule extends GeneratorModule{

	@Override
	protected void config() {
		bind(ApplicationBuilder.class).to(StaticApplicationBuilder.class);
	}
}

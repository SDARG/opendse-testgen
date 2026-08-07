package org.opendse.testcaseGenerator.modules;

import org.opendse.testcaseGenerator.application.ApplicationBuilder;
import org.opendse.testcaseGenerator.application.StaticApplicationBuilder;
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

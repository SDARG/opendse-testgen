package org.opendse.testcasegenerator.modules;

import org.opendse.testcasegenerator.application.ApplicationBuilder;
import org.opendse.testcasegenerator.application.StaticApplicationBuilder;
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

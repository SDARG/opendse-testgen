package org.opendse.testcaseGenerator.visualization;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.opendse.testcaseGenerator.modules.GeneratorModule;
import org.opt4j.core.common.random.RandomModule;
import org.opt4j.core.config.ModuleAutoFinder;
import org.opt4j.core.config.Starter;
import org.opt4j.core.config.annotations.Ignore;
import org.opt4j.core.start.Opt4JModule;

import com.google.inject.Module;


/*
 * ModuleAutofinder that is used to get all Modules needed for the Generator
 */
public class GeneratorModuleAutoFinder extends ModuleAutoFinder{
	
	public Collection<Class<? extends Module>> getModules() {
		return getGeneratorModules();
	}

	/*
	 * returns all Modules that inherit GeneratorModule or RandomModule
	 */
	public Collection<Class<? extends Module>> getGeneratorModules() {
	
			Starter starter = new Starter();
			Collection<File> files = starter.addPlugins();

			classLoader = ClassLoader.getSystemClassLoader();

			files.addAll(getFilesFromClasspath());

			List<Class<?>> classes = new ArrayList<>();

			for (File file : files) {

				if (isJar(file)) {

					try {
						classes.addAll(getAllClasses(new ZipFile(file)));
					} catch (ZipException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (UnsupportedClassVersionError e) {
						System.err.println(file + " not supported: bad version number");
					}
				} else {
					classes.addAll(getAllClasses(file));
				}
			}

			List<Class<? extends Module>> modules = new ArrayList<>();

			for (Class<?> clazz : classes) {
				if (GeneratorModule.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers()) || RandomModule.class.isAssignableFrom(clazz)) {
						Class<? extends Module> module = clazz.asSubclass(Module.class);
						Ignore i = module.getAnnotation(Ignore.class);

						if (i == null && !module.isAnonymousClass() && accept.transform(module) && !ignore.transform(module)) {
							modules.add(module);
							invokeOut("Add module: " + module.toString());
						}
					}
				
			}

			return modules;

		}
}

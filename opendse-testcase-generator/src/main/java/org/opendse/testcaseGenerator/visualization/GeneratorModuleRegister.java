package org.opendse.testcaseGenerator.visualization;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.opt4j.core.config.ModuleRegister;

import com.google.inject.Inject;
import com.google.inject.Module;

public class GeneratorModuleRegister extends ModuleRegister{

	@Inject
	public GeneratorModuleRegister(GeneratorModuleAutoFinder finder) {
		super(finder);
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public synchronized Iterator<Class<? extends Module>> iterator() {
		checkInit();

		return set.iterator();
	}
	
	/**
	 * Returns the number of found modules.
	 * 
	 * @see java.util.HashSet#size()
	 * @return the number of found modules
	 */
	public synchronized int size() {
		checkInit();
		return set.size();
	}
	
	@SuppressWarnings("unused")
	private synchronized void checkInit() {
		while (!isInit) {
			System.out.print("Searching Modules ... ");
			for (Class<? extends Module> clazz : finder.getModules()) {
				set.add(clazz);
				try {
					// try to initialize static code of the module classes while
					// searching modules and not later
					clazz.getDeclaredConstructor().newInstance();
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				} catch (IllegalArgumentException e) {
				} catch (InvocationTargetException e) {
				} catch (NoSuchMethodException e) {
				} catch (SecurityException e) {
				}
			}
			System.out.println("Done");

			isInit = true;
		}
	}
}

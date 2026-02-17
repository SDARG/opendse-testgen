package org.opendse.testcaseGenerator;

import java.lang.reflect.Type;
import java.util.Set;

import org.opt4j.core.Genotype;
import org.opt4j.core.config.Task;
import org.opt4j.core.config.TaskStateListener;
import org.opt4j.core.optimizer.Control;
import org.opt4j.core.optimizer.ControlListener;
import org.opt4j.core.optimizer.Optimizer;
import org.opt4j.core.optimizer.OptimizerIterationListener;
import org.opt4j.core.problem.Creator;
import org.opt4j.core.problem.Decoder;
import org.opt4j.core.problem.Evaluator;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.core.start.Parameters;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;


/**
 * The {@link GeneratorTask} executes one generation process.
 * @see Task
 * 
 */


public class GeneratorTask extends Task{
	
	protected Injector injector = null;

	protected Injector parentInjector = null;

	protected final boolean closeOnStop;

	protected boolean isClosed = false;

	/**
	 * Constructs a {@link GeneratorTask}.
	 * 
	 */
	@Inject
	public GeneratorTask() {
		this(true);
	}

	/**
	 * Constructs a {@link GeneratorTask}.
	 * 
	 * @param closeOnStop
	 *            close automatically after optimization
	 */
	public GeneratorTask(boolean closeOnStop) {
		this.closeOnStop = closeOnStop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opt4j.config.Task#execute()
	 */
	@Override
	public void execute() throws Exception {
		open();
		TestcaseGenerator generator = injector.getInstance(TestcaseGenerator.class);
		generator.GenerateTestcases();
		if (closeOnStop) {
			close();
		}
	}
	
		

	/**
	 * Initialize with a parent {@link Injector}.
	 * 
	 * @param injector
	 *            the parent injector
	 */
	public void init(Injector injector) {
		this.parentInjector = injector;
	}

	/**
	 * Close the task.
	 */
	public synchronized void close() {
		injector = null;
		isClosed = true;
	}

	/**
	 * Initialize a task manually before executing it. This enables to get
	 * instances of classes before the generation starts.
	 */
	public synchronized void open() {
		if (injector == null && !isClosed) {
			if (!isInit) {
				throw new IllegalStateException("Task is not initialized. Call method init(modules) first.");
			}
			if (parentInjector == null) {
				injector = Guice.createInjector(modules);
			} else {
				injector = parentInjector.createChildInjector(modules);
			}
		}
	}


	/**
	 * Returns the instance of the given class.
	 * 
	 * @param <O>
	 *            the type of class
	 * @param type
	 *            the class
	 * @return the instance of the class
	 */
	public <O> O getInstance(Class<O> type) {
		Injector injector = getInjector();
		if (injector == null) {
			return null;
		}
		return injector.getInstance(type);
	}

	/**
	 * Returns the {@link Injector} of the task.
	 * 
	 * @return the injector
	 */
	protected Injector getInjector() {
		return injector;
	}

}


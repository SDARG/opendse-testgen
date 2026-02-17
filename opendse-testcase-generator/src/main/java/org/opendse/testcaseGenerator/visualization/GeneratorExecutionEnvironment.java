package org.opendse.testcaseGenerator.visualization;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.opt4j.core.config.ExecutionEnvironment;
import org.opt4j.core.config.Task;
import org.opt4j.core.config.TaskListener;
import org.opt4j.core.config.TaskStateListener;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/*
 * Environment to execute GeneratorTask
 */
@Singleton
public class GeneratorExecutionEnvironment extends ExecutionEnvironment{
	

		protected final Provider<Task> taskProvider;

		protected final ExecutorService executor;

		protected final List<Task> tasks = new ArrayList<>();

		protected final Set<TaskListener> listeners = new CopyOnWriteArraySet<>();

		/**
		 * Constructs a {@link ExecutionEnvironment}.
		 * 
		 * @param taskProvider
		 *            the task provider
		 */
		@Inject
		public GeneratorExecutionEnvironment(Provider<Task> taskProvider) {
			super(taskProvider);
			this.taskProvider = taskProvider;
			this.executor = Executors.newFixedThreadPool(1);
		}

		/**
		 * Executes the specified modules: A {@link Task} is created and submitted.
		 * 
		 * @param modules
		 *            the collection of modules for a {@link Task}
		 */
		public void execute(Collection<Module> modules) {

			final Task task = taskProvider.get();
			task.init(modules);
			
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					Future<Void> future = executor.submit(task);
					try {
						future.get();
					} catch (InterruptedException e) {
					} catch (ExecutionException e) {
					}
				}
			};
			thread.start();
		}

		
	

	}


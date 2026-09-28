package org.opendse.testcaseGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.SplashScreen;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.opendse.testcaseGenerator.modules.SetupModule;
import org.opendse.testcaseGenerator.visualization.GeneratorStarter;
import org.opt4j.core.config.ModuleAutoFinder;
import org.opt4j.core.config.ModuleAutoFinderListener;
import org.opt4j.core.config.visualization.ApplicationFrame;
import org.opt4j.core.config.visualization.Configurator;
import org.opt4j.core.config.visualization.DelayTask;
import org.opt4j.core.start.Opt4J;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * The {@link Generator} configuration GUI.
 * based on {@link Opt4J}
 * 
 */ class Generator extends Opt4J{
	
	
	public static void main(String[] args) throws Exception{
				
		System.out.println("Starting Opt4J " + getVersion() + " (Build " + getDateISO() + ")");
		if (args.length > 0 && args[0].equalsIgnoreCase("-s")) {
			SplashScreen splash = null;
			try {
				splash = SplashScreen.getSplashScreen();
			} catch (HeadlessException e) {
				// ignore
			}
			if (splash != null) {
				splash.close();
			}
			String[] a = new String[args.length - 1];
			System.arraycopy(args, 1, a, 0, a.length);
			GeneratorStarter.main(a);
		} else {
			SplashScreen splash = SplashScreen.getSplashScreen();
			SplashDecorator decorator = null;
			if (splash != null) {
				decorateVersionDate(splash);
				decorator = new SplashDecorator(splash);
			}
		searchModules(decorator);
		Configurator configurator = new Opt4J();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		Module module = configurator.getModule(GeneratorTask.class);
		final Injector injector = Guice.createInjector(module,new SetupModule());
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ApplicationFrame frame = injector.getInstance(ApplicationFrame.class);
					frame.startup();
					
					
				}});		
		}
		
	}
	
	
	static class SplashDecorator {

		protected DelayTask delay = new DelayTask(1);

		protected SplashScreen splash;

		public SplashDecorator(SplashScreen splash) {
			this.splash = splash;
		}

		public SplashScreen getSplash() {
			return splash;
		}

		public void print(final String message, final Color color) {
			delay.execute(new Runnable() {
				@Override
				public void run() {
					Graphics2D g = splash.createGraphics();
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.setColor(Color.WHITE);
					g.setBackground(new Color(252, 230, 212));
					g.clearRect(10, 175, 280, 20);
					g.setClip(10, 175, 280, 20);
					g.setColor(color);
					g.setFont(new Font("SansSerif", Font.BOLD, 9));
					g.drawString(message, 12, 188);
					splash.update();
				}
			});
		}
	}
	
	
	private static void searchModules(SplashDecorator splash) {

		class SplashSearchDecorator implements ModuleAutoFinderListener {

			protected final SplashDecorator splash;

			SplashSearchDecorator(SplashDecorator splash) {
				this.splash = splash;
			}

			@Override
			public void err(String message) {
				splash.print(message, Color.RED);
			}

			@Override
			public void out(final String message) {
				splash.print(message, Color.GRAY.darker());
			}
		}

		ModuleAutoFinder finder = new ModuleAutoFinder();
		if (splash != null) {
			SplashSearchDecorator deco = new SplashSearchDecorator(splash);
			finder.addListener(deco);
		}

		for (Class<? extends Module> module : finder.getModules()) {
			moduleList.add(module);
		}

		if (splash != null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
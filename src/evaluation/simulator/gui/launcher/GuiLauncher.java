package evaluation.simulator.gui.launcher;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;

import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.gui.service.GuiService;

public class GuiLauncher {

	private static Logger logger = Logger.getLogger(GuiLauncher.class);

	public static void main(String[] args) {
		logger.debug("simGUI start.");
		@SuppressWarnings("unused")
		SimPropRegistry simPropRegistry;

		class simPropInitializer implements Callable<SimPropRegistry>
		{
			@Override
			public SimPropRegistry call()
			{
				SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
				// initial dependency-check for per plugin configurations
				DependencyChecker.checkAll(simPropRegistry);

				return simPropRegistry;
			}
		}

		final ExecutorService service;
		final Future<SimPropRegistry>  task;

		service = Executors.newFixedThreadPool(1);
		task    = service.submit(new simPropInitializer());

		try
		{
			// block until finished
			simPropRegistry = task.get();
			
		}
		catch(final InterruptedException ex)
		{
			ex.printStackTrace();
		}
		catch(final ExecutionException ex)
		{
			ex.printStackTrace();
		}

		service.shutdownNow();

		// Change Look and Feel to GTK
		/*
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
				.getInstalledLookAndFeels()) {
			if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info
					.getClassName())) {
				try {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		*/
		
		
		/*
		 * Mogliche Look an feels:
		 * Nimbus: "javax.swing.plaf.nimbus.NimbusLookAndFeel"
		 * Cross Platform aka. Metal: "javax.swing.plaf.metal.MetalLookAndFeel" oder UIManager.getCrossPlatformLookAndFeelClassName()
		 * 
		 */
		
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	        } 
		catch (Exception e) {
	          System.out.println("failed to set look and feel");
	          e.printStackTrace();
	    }
		
		//Show installed look and feels
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
				.getInstalledLookAndFeels()) {
			logger.debug(info);
		}
		
		GuiService.getInstance();

	}
}

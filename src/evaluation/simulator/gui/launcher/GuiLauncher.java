package evaluation.simulator.gui.launcher;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.gui.service.GuiService;

/**
 * Launches the gMix GUI
 * 
 * @author nachkonvention
 * 
 */
public class GuiLauncher {

	private static Logger logger = Logger.getLogger(GuiLauncher.class);

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		logger.debug("simGUI start.");
		@SuppressWarnings("unused")
		SimPropRegistry simPropRegistry;

		class simPropInitializer implements Callable<SimPropRegistry> {
			@Override
			public SimPropRegistry call() {
				// initial creation of the simulaton property registry
				SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();

				// initial dependency-check for per plugin configurations
				DependencyChecker.checkAll(simPropRegistry);

				return simPropRegistry;
			}
		}

		final ExecutorService service;
		final Future<SimPropRegistry> task;

		service = Executors.newFixedThreadPool(1);
		task = service.submit(new simPropInitializer());

		try {
			// block until finished
			simPropRegistry = task.get();
			// loading of default values (template config)

		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}

		service.shutdownNow();

		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.out.println("failed to set look and feel");
			e.printStackTrace();
		}

		// Show installed look and feels
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			logger.debug(info);

		}

		GuiService.getInstance();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// loading initial experiment configuration (template)
				SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
				File file = new File("etc/conf/experiment_template.cfg");
				SimulationConfigService simulationConfigService = new SimulationConfigService(simPropRegistry);
				simulationConfigService.loadConfig(file);
			}
		});

	}
}

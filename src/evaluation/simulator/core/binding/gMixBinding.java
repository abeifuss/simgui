package evaluation.simulator.core.binding;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.Simulator;
import evaluation.simulator.core.statistics.ResultSet;
import evaluation.simulator.core.statistics.Statistics;
import evaluation.simulator.gui.layout.SimulationTab;
import evaluation.simulator.gui.results.ResultPanelFactory;
import framework.core.launcher.CommandLineParameters;

/**
 * @author alex
 *
 */
public class gMixBinding extends Thread {

	private final Logger logger = Logger.getLogger(gMixBinding.class);

	private static gMixBinding instance = null;
	private CommandLineParameters params;
	private String resultsFileName;
	Statistics stats;
	private int experimentsPerformed = 0;

	/**
	 * Default constructor
	 */
	private gMixBinding() {

	}

	/**
	 * @param configFile
	 * 		String with config file content
	 */
	public void setParams(String[] configFile) {
		this.params = new CommandLineParameters(configFile);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		try {
			Simulator.reset();
			Simulator gMixSim = new Simulator(this.params);
			ResultSet results = gMixSim.results;
			if (results != null) {
				this.logger.log(Level.INFO, "Finished simulator with results");
			}

			// for (PlotType plotType:results.getDesiredPlotTypes()) {
			// plotType.plot(results);
			// }

			final JPanel resultPlot = ResultPanelFactory.getGnuplotResultPanel(this.getGnuplotConsoleOutputFileName());

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SimulationTab.getInstance().getResultsPanel()
							.addTab("Experiment " + gMixBinding.this.experimentsPerformed, resultPlot);
					resultPlot.updateUI();
					resultPlot.repaint();
					gMixBinding.this.experimentsPerformed++;
				}
			});

			// TODO: The tab has to be refreshed!!!

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Singleton
	 * 
	 * @return
	 * 		reference of {@link gMixBinding}
	 */
	public static gMixBinding getInstance() {
		if (instance == null) {
			instance = new gMixBinding();
		}
		return instance;
	}

	/**
	 * @param gnuplotConsoleOutputFileName
	 */
	public void setGnuplotConsoleOutputFileName(String gnuplotConsoleOutputFileName) {
		this.resultsFileName = gnuplotConsoleOutputFileName;
	}

	/**
	 * Resets the number of performed experiments
	 */
	public void resetExperiments() {
		this.experimentsPerformed = 0;
	}

	/**
	 * @return
	 * 		file name of the resulting picture / plot
	 */
	public String getGnuplotConsoleOutputFileName() {
		return this.resultsFileName;
	}

}

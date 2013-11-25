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



public class gMixBinding extends Thread {

	private final Logger logger = Logger.getLogger(gMixBinding.class);

	private static gMixBinding instance = null;
	private CommandLineParameters params;
	private String resultsFileName;
	Statistics stats;

	public gMixBinding() {

	}

	public void setParams(String[] configFile) {
		this.params = new CommandLineParameters(configFile);
	}

	@Override
	public void run() {

		try {
			Simulator.reset();
			Simulator gMixSim = new Simulator(this.params);
			ResultSet results = gMixSim.results;
			if (results != null){
				this.logger.log(Level.INFO, "Finished simulator with results");
			}

			//			for (PlotType plotType:results.getDesiredPlotTypes()) {
			//				plotType.plot(results);
			//			}

			final JPanel resultPlot = ResultPanelFactory.getGnuplotResultPanel(this.getGnuplotConsoleOutputFileName());


			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SimulationTab.getInstance().getResultsPanel().addTab(gMixBinding.this.getGnuplotConsoleOutputFileName(), resultPlot);
					resultPlot.updateUI();
					resultPlot.repaint();
				}
			});

			// TODO: The tab has to be refreshed!!!

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static gMixBinding getInstance() {
		if (instance == null) {
			instance = new gMixBinding();
		}
		return instance;
	}

	public void setGnuplotConsoleOutputFileName(
			String gnuplotConsoleOutputFileName) {
		this.resultsFileName = gnuplotConsoleOutputFileName;
	}

	public String getGnuplotConsoleOutputFileName() {
		return this.resultsFileName;
	}


}

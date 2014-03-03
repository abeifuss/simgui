package evaluation.simulator.core.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.Simulator;
import evaluation.simulator.core.statistics.ResultSet;
import evaluation.simulator.core.statistics.Statistics;
import evaluation.simulator.gui.layout.SimulationTab;
import evaluation.simulator.gui.layout.frames.GraphFrame;
import evaluation.simulator.gui.results.GnuplotPanel;
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
	 *            String with config file content
	 */
	public void setParams(String[] configFile) {
		this.params = new CommandLineParameters(configFile);
	}

	/*
	 * (non-Javadoc)
	 * 
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
					JTabbedPane resultsTabs = SimulationTab.getInstance().getResultsPanel();
					resultsTabs.addTab("Experiment " + gMixBinding.this.experimentsPerformed, resultPlot);
					resultsTabs.setSelectedComponent(resultPlot);
					setMaximizeButton(resultsTabs);
					resultPlot.updateUI();
					resultPlot.repaint();
					gMixBinding.this.experimentsPerformed++;
				}

				private void setMaximizeButton(JTabbedPane resultsTabs) {
					int tabIndex = resultsTabs.getSelectedIndex();
					JPanel tabPanel = new JPanel();
					JLabel tabLabel = new JLabel("Experiment " + gMixBinding.getInstance().experimentsPerformed);
					tabPanel.add(tabLabel);
					final GnuplotPanel tmpGnuplotPanel = (GnuplotPanel) resultPlot;
					JButton maximizeButton = new JButton(new ImageIcon("etc/img/icons/maximize.png"));
					maximizeButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							GraphFrame.getInstance(tmpGnuplotPanel.svgCanvas.getURI(),
									tmpGnuplotPanel.gnuplotResultFileName);

						}
					});
					tabPanel.add(maximizeButton);
					tabPanel.setOpaque(false);
					resultsTabs.setTabComponentAt(tabIndex, tabPanel);
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
	 * @return reference of {@link gMixBinding}
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
	 * @return file name of the resulting picture / plot
	 */
	public String getGnuplotConsoleOutputFileName() {
		return this.resultsFileName;
	}

}

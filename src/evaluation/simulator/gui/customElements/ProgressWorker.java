package evaluation.simulator.gui.customElements;

import java.io.File;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import evaluation.simulator.core.binding.gMixBinding;
import evaluation.simulator.gui.layout.SimulationTab;
import evaluation.simulator.gui.service.ConfigParser;

/**
 * {@link SwingWorker} implementing a simple {@link JProgressBar} for displaying
 * that the Simulator is running and generating output.
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("rawtypes")
public class ProgressWorker extends SwingWorker {
	protected String doInBackground() {
		ConfigChooserPanel.getProgressBar().setVisible(true);
		ConfigChooserPanel.getProgressBar().setIndeterminate(true);

		SimulationTab.getInstance().getResultsPanel().remove(SimulationTab.getInstance().homeTab);

		ConfigParser configParser = new ConfigParser();

		String[][] params = new String[ConfigChooserPanel.getInstance().getConfigList().getSelectedValuesList().size()][1];

		int i = 0;
		for (File file : ConfigChooserPanel.getInstance().getConfigList().getSelectedValuesList()) {
			params[i][0] = configParser.cleanupConfigurationForSimulator(file);

			ConfigChooserPanel.setCallSimulation(gMixBinding.getInstance());
			ConfigChooserPanel.getCallSimulation().setParams(params[i]);
			ConfigChooserPanel.getCallSimulation().run();
			final int j = i;

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ConfigChooserPanel.getProgressBar().setValue(j);
				}
			});
			i++;
		}
		return null;
	}

	protected void done() {
		ConfigChooserPanel.getProgressBar().setVisible(false);
	}
}
package evaluation.simulator.gui.customElements;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import evaluation.simulator.core.binding.gMixBinding;
import evaluation.simulator.gui.layout.SimulationTab;
import evaluation.simulator.gui.service.ConfigParser;

public class ProgressWorker extends SwingWorker {
	protected String doInBackground() {
		ConfigChooserPanel.getInstance().progressBar.setVisible(true);
		ConfigChooserPanel.getInstance().progressBar.setIndeterminate(true);

		SimulationTab.getInstance().getResultsPanel().remove(SimulationTab.getInstance().homeTab);

		ConfigParser configParser = new ConfigParser();

		String[][] params = new String[ConfigChooserPanel.getInstance().configList.getSelectedValuesList().size()][1];

		int i = 0;
		for (File file : ConfigChooserPanel.getInstance().configList.getSelectedValuesList()) {
			params[i][0] = configParser.cleanupConfigurationForSimulator(file);

			ConfigChooserPanel.getInstance().callSimulation = gMixBinding.getInstance();
			ConfigChooserPanel.getInstance().callSimulation.setParams(params[i]);
			ConfigChooserPanel.getInstance().callSimulation.run();
			final int j = i;

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ConfigChooserPanel.getInstance().progressBar.setValue(j);
				}
			});
			i++;
		}
		return null;
	}

	protected void done() {
		ConfigChooserPanel.getInstance().progressBar.setVisible(false);
	}
}
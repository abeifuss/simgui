package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import evaluation.simulator.core.binding.gMixBinding;
import evaluation.simulator.gui.customElements.ConfigChooserPanel;
import evaluation.simulator.gui.helper.IOActions;
import evaluation.simulator.gui.layout.SimulationTab;
import evaluation.simulator.gui.results.GnuplotPanel;

public class ClearButtonAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SimulationTab.getInstance().getResultsPanel().removeAll();
		gMixBinding.getInstance().resetExperiments();
		SimulationTab.getInstance().getResultsPanel().add("Welcome", SimulationTab.getInstance().homeTab);
		try {
			IOActions.cleanOutputFolder();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Could not clean up Output directory " + GnuplotPanel.outputFolder,
					"Cleanup Error", JOptionPane.ERROR_MESSAGE);
		}
		ConfigChooserPanel.getInstance().exportPictureButton.setEnabled(false);
		ConfigChooserPanel.getInstance().updateConfigDirectory();
	}

}
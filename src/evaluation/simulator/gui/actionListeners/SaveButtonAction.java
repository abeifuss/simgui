package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import evaluation.simulator.conf.service.SimulationConfigService;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SaveButtonAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int state = fc.showOpenDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
			SimulationConfigService simulationConfigService = new SimulationConfigService(
					simPropRegistry);
			simulationConfigService.writeConfig(file);
		}
	}

}

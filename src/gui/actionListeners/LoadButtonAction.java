package gui.actionListeners;

import gui.pluginRegistry.SimPropRegistry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import conf.service.SimulationConfigService;

public class LoadButtonAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// Open FileChooser and load config file. Check whether config file is
		// of expected type.
		JFileChooser fc = new JFileChooser();
		int state = fc.showOpenDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
			SimulationConfigService simulationConfigService = new SimulationConfigService(
					simPropRegistry);
			simulationConfigService.loadConfig(file);
		}
	}
}

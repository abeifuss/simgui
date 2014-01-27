package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;
import evaluation.simulator.gui.customElements.ConfigChooserPanel;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class LoadButtonAction implements ActionListener {
	private static Logger logger = Logger.getLogger(LoadButtonAction.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.log(Level.DEBUG, "Load config");

		// Open FileChooser and load config file. Check whether config file is
		// of expected type.
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("etc/experiments/"));
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Config File";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".cfg");
			}
		});
		int state = fc.showOpenDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
			SimulationConfigService simulationConfigService = new SimulationConfigService(simPropRegistry);
			simulationConfigService.loadConfig(file);
		}
		ConfigChooserPanel.getInstance().updateConfigDirectory();
	}
}

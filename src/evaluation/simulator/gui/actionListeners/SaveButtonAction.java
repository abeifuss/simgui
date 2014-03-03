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
import evaluation.simulator.gui.customElements.PluginPanel;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

/**
 * implements the ActionListener for saving the configuration into a .cfg file
 * from the {@link PluginPanel}.
 * 
 * @author nachkonvention
 */
public class SaveButtonAction implements ActionListener {
	private static Logger logger = Logger.getLogger(SaveButtonAction.class);

	@Override
	public void actionPerformed(ActionEvent e) {

		logger.log(Level.DEBUG, "Save config");

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

		int state = fc.showSaveDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!fc.getSelectedFile().getAbsolutePath().endsWith(".cfg")) {
				file = new File (fc.getSelectedFile() +".cfg" );
			}
		
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
			SimulationConfigService simulationConfigService = new SimulationConfigService(simPropRegistry);
			simulationConfigService.writeConfig(file);

		}
		ConfigChooserPanel.getInstance().updateConfigDirectory();
	}

}

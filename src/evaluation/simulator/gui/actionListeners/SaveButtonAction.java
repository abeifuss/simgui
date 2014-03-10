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
			File f = new File (SimPropRegistry.getInstance().getCurrentConfigFile());
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
			SimulationConfigService simulationConfigService = new SimulationConfigService(simPropRegistry);
			simulationConfigService.writeConfig(f);
			// Now there are no unsaved changes
			SimPropRegistry.getInstance().setUnsavedChanges(false);
			logger.log(Level.DEBUG, "Saved config");
	}

}

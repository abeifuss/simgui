package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.gui.customElements.ConfigChooserPanel;
import evaluation.simulator.gui.customElements.PluginPanel;

/**
 * @author nachkonvention
 * 
 *         implements the ActionListener for resetting the {@link PluginPanel}.
 *         By now it is just updating the config file directory.
 * 
 */
public class ResetButtonAction implements ActionListener {

	private static Logger logger = Logger.getLogger(ResetButtonAction.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.log(Level.DEBUG, "Reset config");
		ConfigChooserPanel.getInstance().updateConfigDirectory();

	}

}

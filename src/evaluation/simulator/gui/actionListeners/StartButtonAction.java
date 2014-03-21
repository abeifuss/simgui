package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JList;
import javax.swing.JOptionPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.core.binding.gMixBinding;
import evaluation.simulator.gui.customElements.ProgressWorker;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.service.ProgressRegistry;

/**
 * implements the ActionListener for starting the Simulator with the previosuly
 * chosen config from {@link SimConfigPanel}.
 * 
 * @author nachkonvention
 */
public class StartButtonAction implements ActionListener {

	JList<File> configList;
	private final Logger logger = Logger.getLogger(gMixBinding.class);

	/**
	 * @param configList
	 *            the list of config files selected in {@link SimConfigPanel}
	 */
	public StartButtonAction(JList<File> configList) {
		this.configList = configList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (configList.getSelectedIndices().length == 0) {
			JOptionPane.showMessageDialog(null, "Please select at least one config!");
		} else {
			this.logger.log(Level.INFO, "Start simulator");
			
			ProgressWorker progress = new ProgressWorker();
			ProgressRegistry.getInstance().setProgressWorker(progress);
			progress.enable();
			progress.execute();
		}
	}

}

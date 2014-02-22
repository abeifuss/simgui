package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JList;
import javax.swing.JOptionPane;

import evaluation.simulator.gui.customElements.ProgressWorker;
import evaluation.simulator.gui.customElements.SimConfigPanel;

/**
 * implements the ActionListener for starting the Simulator with the previosuly
 * chosen config from {@link SimConfigPanel}.
 * 
 * @author nachkonvention
 */
public class StartButtonAction implements ActionListener {

	JList<File> configList;

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
			new ProgressWorker().execute();
		}
	}

}

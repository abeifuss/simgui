package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JList;
import javax.swing.JOptionPane;

import evaluation.simulator.gui.customElements.ProgressWorker;

public class StartButtonAction implements ActionListener {

	JList<File> configList;

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

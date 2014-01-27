package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import evaluation.simulator.gui.customElements.ConfigChooserPanel;

public class StopButtonAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigChooserPanel.getInstance().callSimulation.interrupt();
	}

}

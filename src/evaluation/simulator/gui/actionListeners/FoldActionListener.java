package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import evaluation.simulator.gui.customElements.SimConfigPanel;

public class FoldActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SimConfigPanel.getInstance().foldAccordions();
	}

}

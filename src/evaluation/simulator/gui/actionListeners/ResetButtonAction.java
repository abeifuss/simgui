package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class ResetButtonAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Logger.Log( LogLevel.DEBUG , "Reset config");

	}

}

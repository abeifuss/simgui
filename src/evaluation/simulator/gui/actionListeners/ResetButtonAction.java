package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import evaluation.simulator.conf.service.SimulationConfigService;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ResetButtonAction implements ActionListener {
	
	private static Logger logger = Logger.getLogger(ResetButtonAction.class);
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.log( Level.DEBUG , "Reset config");

	}

}

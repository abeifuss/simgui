package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import evaluation.simulator.gui.customElements.ConfigChooserPanel;
import evaluation.simulator.gui.customElements.ProgressWorker;
import evaluation.simulator.gui.service.ProgressRegistry;

/**
 * implements the ActionListener for stopping the currently running Simulator.
 * 
 * @author nachkonvention
 */
public class StopButtonAction implements ActionListener {
	
	private static Logger logger = Logger.getLogger(StopButtonAction.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigChooserPanel.getInstance();
		logger.info("Interrupt simulator");
		
		ProgressWorker progress = ProgressRegistry.getInstance().getProgressWorker();
		progress.requestStop();
		
		ConfigChooserPanel.getCallSimulation().requestStop();
	}

}

package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

/**
 * {@link JPanel} putting together {@link SimHelpContentPanel} and
 * {@link SimHelpMenuPanel}
 * 
 * @author nachkonvention
 * 
 */
public class SimHelpPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static SimHelpPanel instance;

	/**
	 * Singleton
	 * 
	 * @return an instance of {@link SimHelpPanel}
	 */
	public static SimHelpPanel getInstance() {
		if (instance == null) {
			instance = new SimHelpPanel();
		}
		return instance;
	}

	private SimHelpPanel() {
		this.initialize();
	}

	private void initialize() {

		this.setLayout(new BorderLayout());
		JSplitPane splitPlane = new JSplitPane();

		SimHelpContentPanel content = SimHelpContentPanel.getInstance();
		SimHelpMenuPanel navigation = SimHelpMenuPanel.getInstance();

		splitPlane.setLeftComponent(navigation);
		splitPlane.setRightComponent(content);
		this.add(splitPlane, BorderLayout.CENTER);
	}

}

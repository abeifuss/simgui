package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import evaluation.simulator.gui.actionListeners.LoadButtonAction;
import evaluation.simulator.gui.actionListeners.ResetButtonAction;
import evaluation.simulator.gui.actionListeners.SaveButtonAction;
import evaluation.simulator.gui.customElements.accordion.AccordionEntry;

/**
 * Encapsules the whole configuration of the Simulator. Offers the possibility
 * to load, save and reset the config. Holds the {@link AccordionEntry}
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("serial")
public class SimConfigPanel extends JPanel {

	private static SimConfigPanel instance = null;

	public static SimConfigPanel getInstance() {
		if (instance == null) {
			instance = new SimConfigPanel();
		}
		return instance;
	}

	/**
	 * @param enabled
	 *            enables the button
	 */
	public static void setStatusofSaveButton(Boolean enabled) {
		getInstance().saveButton.setEnabled(enabled);
	}

	// private Accordion _accordian;
	private JPanel buttonBar;
	private JButton loadButton;

	private PluginPanel pluginPanel;

	private JButton resetButton;
	private JButton saveButton;

	private SimConfigPanel() {
		this.initialize();
		this.resize(this.pluginPanel.getWidth(), this.pluginPanel.getHeight());
	}

	private void initialize() {

		// this._accordian = new Accordion();
		// this.plugInSelection = new PlugInSelection();

		this.pluginPanel = new PluginPanel();

		this.buttonBar = new JPanel();

		this.loadButton = new JButton("Load");
		this.loadButton.addActionListener(new LoadButtonAction());
		this.buttonBar.add(this.loadButton, BorderLayout.SOUTH);
		this.saveButton = new JButton("Save");
		this.saveButton.addActionListener(new SaveButtonAction());
		this.buttonBar.add(this.saveButton, BorderLayout.SOUTH);

		this.resetButton = new JButton("Reset");
		this.resetButton.addActionListener(new ResetButtonAction());
		this.buttonBar.add(this.resetButton, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		// this.add(this.plugInSelection, BorderLayout.NORTH);
		// this.add(this._accordian, BorderLayout.CENTER);

		this.add(this.pluginPanel, BorderLayout.CENTER);
		this.add(this.buttonBar, BorderLayout.SOUTH);

	}

	/**
	 * updates the GUI
	 */
	public void update() {
		this.pluginPanel.update();
		this.updateUI();
	}

	public void foldAccordions() {
		this.pluginPanel.toggleFoldAccordions();
	}

	public void unfoldAccordions() {
		this.pluginPanel.toggleUnfoldAccordions();
	}
}

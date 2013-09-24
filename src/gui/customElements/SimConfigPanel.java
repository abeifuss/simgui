package gui.customElements;

import gui.actionListeners.LoadButtonAction;
import gui.actionListeners.ResetButtonAction;
import gui.actionListeners.SaveButtonAction;
import gui.customElements.accordion.Accordion;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SimConfigPanel extends JPanel {

	private static SimConfigPanel _instance = null;

	public static SimConfigPanel getInstance() {
		if (_instance == null) {
			_instance = new SimConfigPanel();
		}
		return _instance;
	}

	private Accordion _accordian;

	private SimConfigPanel() {
		this.init();
	}

	private void init() {

		this._accordian = new Accordion();
		PlugInSelection plugInSelection = new PlugInSelection();

		JPanel buttonBar = new JPanel();

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new LoadButtonAction());
		buttonBar.add(loadButton, BorderLayout.SOUTH);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveButtonAction());
		buttonBar.add(saveButton, BorderLayout.SOUTH);

		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ResetButtonAction());
		buttonBar.add(resetButton, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(plugInSelection, BorderLayout.NORTH);
		this.add(this._accordian, BorderLayout.CENTER);
		this.add(buttonBar, BorderLayout.SOUTH);
	};
}

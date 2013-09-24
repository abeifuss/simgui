package gui.customElements;

import java.awt.BorderLayout;

import gui.customElements.accordion.Accordion;

import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SimConfigPanel extends JPanel {

	private Accordion _accordian;

	private static SimConfigPanel _instance = null;
	
	private SimConfigPanel(){
		init();
	}
	
	public static SimConfigPanel getInstance() {
		if (_instance == null) {
			_instance = new SimConfigPanel();
		}
		return _instance;
	}
	
	private void init(){
	
		_accordian = new Accordion();
		PlugInSelection plugInSelection = new PlugInSelection();

		JPanel buttonBar = new JPanel();
		buttonBar.add(new JButton("Load"), BorderLayout.SOUTH);
		buttonBar.add(new JButton("Save"), BorderLayout.SOUTH);
		buttonBar.add(new JButton("Reset"), BorderLayout.SOUTH);
		
		this.setLayout(new BorderLayout());
		this.add(plugInSelection, BorderLayout.NORTH);
		this.add(_accordian, BorderLayout.CENTER);
		this.add(buttonBar, BorderLayout.SOUTH); 
	};
}

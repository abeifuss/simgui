package gui.customElements;

import java.awt.BorderLayout;

import gui.customElements.accordion.Accordion;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SimConfigPanel extends JPanel {

	private Accordion accordian;
	
	private static SimConfigPanel instance = null;
	
	private SimConfigPanel(){
		init();
	}
	
	public static SimConfigPanel getInstance() {
		if (instance == null) {
			instance = new SimConfigPanel();
		}
		return instance;
	}
	
	private void init(){
	
		accordian = new Accordion();
		PlugInSelection plugInSelection = new PlugInSelection();

		JPanel buttonBar = new JPanel();
		buttonBar.add(new JButton("Load"), BorderLayout.SOUTH);
		buttonBar.add(new JButton("Save"), BorderLayout.SOUTH);
		buttonBar.add(new JButton("Reset"), BorderLayout.SOUTH);
		
		this.setLayout(new BorderLayout());
		this.add(plugInSelection, BorderLayout.NORTH);
		this.add(accordian, BorderLayout.CENTER);
		this.add(buttonBar, BorderLayout.SOUTH); 
	};
}

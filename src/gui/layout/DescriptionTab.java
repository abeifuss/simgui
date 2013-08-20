package gui.layout;

import gui.service.DescriptionService;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DescriptionTab extends JTextArea implements Observer{

	public DescriptionTab() {
		super();
		DescriptionService ds = DescriptionService.getInstance();
		ds.addObserver(this);
		setEditable(false);
		
		// TODO: find correct bg color!
		// or use another component than JTextArea
		setBackground(new Color(220,218,213));
	}

	@Override
	public void update(Observable o, Object arg) {
		setText((String)arg);		
	}
}

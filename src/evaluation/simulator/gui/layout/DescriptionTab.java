package evaluation.simulator.gui.layout;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;

import evaluation.simulator.gui.service.DescriptionService;

/**
 * @author alex
 *
 */
@SuppressWarnings("serial")
public class DescriptionTab extends JTextArea implements Observer {

	/**
	 *  Default constructor
	 */
	public DescriptionTab() {
		super();
		DescriptionService ds = DescriptionService.getInstance();
		ds.addObserver(this);
		this.setEditable(false);

		// TODO: find correct bg color!
		// or use another component than JTextArea
		this.setBackground(new Color(220, 218, 213));
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.setText((String) arg);
	}
}

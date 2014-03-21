package evaluation.simulator.gui.layout;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Welcome Tab of gMix GUI
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("serial")
public class HomeTab extends JPanel {

	/**
	 * Default Constructor
	 */
	public HomeTab() {

		JLabel welcomeLabel = new JLabel("Welcome to gMixSim");
		welcomeLabel.setFont(new Font("arial", 1, 35));

		JLabel logosLabel = new JLabel(new ImageIcon("etc/img/icons/logoc.png"));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(this, gbc);
		this.setLayout(gbl);

		this.add(welcomeLabel, gbc);
		this.add(logosLabel, gbc);

	}

}

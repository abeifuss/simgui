package evaluation.simulator.gui.layout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HomeTab extends JPanel {


	public HomeTab() {

		Color bg = this.getBackground();

		JLabel wellcomeLabel = new JLabel("Wellcome to gMixSim");
		wellcomeLabel.setFont(new Font("arial", 1, 35));

		JLabel introduction = new JLabel("gMixSim is a Simulator for Mixes");

		JLabel logosLabel = new JLabel(new ImageIcon("etc/img/icons/logoc.png"));

		JTextArea tutorialText = new JTextArea(
				"If you are new to gMixSim, it might be helpful to inspect our [Tutorial] section. There you will find video-guides explaining how to use this software.");
		tutorialText.setLineWrap(true);
		tutorialText.setWrapStyleWord(true);
		tutorialText.setBackground(bg);

		JTextArea simulatorText = new JTextArea(
				"The [Simulator] section is where you find the experiment configurations. You can select a set of experiments and run them.");
		simulatorText.setLineWrap(true);
		simulatorText.setWrapStyleWord(true);
		simulatorText.setBackground(bg);

		JTextArea resultsText = new JTextArea(
				"The results of the experiment series are presented in the [Results] section.");
		resultsText.setLineWrap(true);
		resultsText.setWrapStyleWord(true);
		resultsText.setBackground(bg);

		JTextArea HelpText = new JTextArea(
				"If you have touble with the configuration of an experiment, you will propably find helpful information in the [Help] section.");
		HelpText.setLineWrap(true);
		HelpText.setWrapStyleWord(true);
		HelpText.setBackground(bg);


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

		this.add(wellcomeLabel, gbc);
		this.add(introduction, gbc);
		this.add(logosLabel, gbc);

		this.add(tutorialText, gbc);
		this.add(simulatorText, gbc);
		this.add(resultsText, gbc);
		this.add(HelpText, gbc);

	}

}

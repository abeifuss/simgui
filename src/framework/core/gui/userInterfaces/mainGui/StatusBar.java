package framework.core.gui.userInterfaces.mainGui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * Singleton for displaying any state in an status panel.
 * 
 * @author Marius Fink
 * @version 28.08.2010
 */
public class StatusBar {

	private static StatusBar statusPanel;
	private final ImageIcon lightOnIcon = ImageLoader.loadIcon("lightOn", 16);
	private final ImageIcon lightOffIcon = ImageLoader.loadIcon("lightOff", 16);
	private final SimpleDateFormat format;
	private final JLabel iconLabel;
	private JPanel mainPanel = new JPanel();
	private JLabel timeLabel;
	private JLabel textLabel;

	private StatusBar() {
		format = new SimpleDateFormat("HH:mm:ss");
		iconLabel = new JLabel(lightOffIcon);
		timeLabel = new JLabel("");
		timeLabel.setForeground(new Color(150, 150, 150));
		textLabel = new JLabel("");

		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 1));
		mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		mainPanel.add(iconLabel);
		mainPanel.add(timeLabel);
		mainPanel.add(textLabel);
	}

	/**
	 * Changes the state of the label. Displays the light-on-icon for 1 second. Replaces all
	 * prevously existing stati,
	 * 
	 * @param text
	 *            the text to display
	 */
	public void showStatus(String text) {
		iconLabel.setIcon(lightOnIcon);
		timeLabel.setText(format.format(new Date()));
		textLabel.setText(text);

		Timer t = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				iconLabel.setIcon(lightOffIcon);
			}
		});
		t.setRepeats(false);
		t.start();
	}

	/**
	 * @return the instance of this singleton
	 */
	public static StatusBar getInstance() {
		if (statusPanel != null) {
			return statusPanel;
		}

		statusPanel = new StatusBar();
		return statusPanel;
	}

	/**
	 * @return returns the status panel
	 */
	public JPanel getPanel() {
		return mainPanel;
	}
}

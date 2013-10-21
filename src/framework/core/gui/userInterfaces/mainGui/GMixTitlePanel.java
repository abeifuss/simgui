package framework.core.gui.userInterfaces.mainGui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * TitlePanel creates a standard title JPanel with title images and the headlines
 * 
 * @author Marius Fink
 * @version 06/09/2012
 */
public class GMixTitlePanel extends JPanel {

	private static final long serialVersionUID = -8626046004984201553L;
	private String additional;

	/**
	 * Creates the standard title panel
	 */
	public GMixTitlePanel() {
		new GMixTitlePanel("");
	}

	/**
	 * Creates a title panel with additional String in headline. The String should be very short.
	 * 
	 * @param additional
	 *            the String to be shown in title
	 */
	public GMixTitlePanel(String additional) {
		assert additional != null : "Precondition failed: additional != null";
		this.additional = additional;
		generateTitlePanel();
		repaint();
	}

	/**
	 * Builds the Title Panel with the logo and the title
	 */
	private void generateTitlePanel() {
		setLayout(new BorderLayout());

		JPanel titleTextPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 15, 10));
		titleTextPanel.setOpaque(false);
		String s = GUIText.getText("gMix");
		if (!additional.equals("")) {
			s += " " + additional;
		}

		JLabel gMixLabel = new JLabel(s);
		gMixLabel.setOpaque(false);
		gMixLabel.setFont(gMixLabel.getFont().deriveFont(30f));
		titleTextPanel.add(gMixLabel);

		JPanel subtitlePanel = new JPanel(new GridLayout(2, 1));
		subtitlePanel.setOpaque(false);
		JLabel subtitleLabel = new JLabel(GUIText.getText("gMixSubtitle"));
		subtitleLabel.setOpaque(false);
		subtitleLabel.setFont(gMixLabel.getFont().deriveFont(10f));
		subtitlePanel.add(subtitleLabel);
		JLabel versionLabel = new JLabel(GUIText.getText("version") + " - " + GUIText.getText("copyright"));
		versionLabel.setOpaque(false);
		versionLabel.setFont(gMixLabel.getFont().deriveFont(10f));
		subtitlePanel.add(versionLabel);

		titleTextPanel.add(subtitlePanel);

		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		logoPanel.setOpaque(false);
		logoPanel.add(new JLabel(ImageLoader.loadIconNoRescale("logo")));

		add(titleTextPanel, BorderLayout.CENTER);
		add(logoPanel, BorderLayout.EAST);
		setBorder(BorderFactory.createLoweredBevelBorder());

	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(ImageLoader.loadIconNoRescale("background").getImage(), 0, 0, this);
	}

}

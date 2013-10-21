package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import framework.core.gui.util.visualUtilities.GUIColors;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * DescriptionPanel is a panel that displays a given description
 * 
 * @author Marius Fink
 * @version 18.09.2012
 */
public class DescriptionPanel extends JPanel {
	private static final long serialVersionUID = -1833375046607590828L;
	private JTextArea textArea;

	/**
	 * Initializes this DescriptionPanel whit the final description.
	 * 
	 * @param description
	 *            the description to display
	 */
	public DescriptionPanel(String description) {
		setLayout(new BorderLayout(10, 0));
		JPanel iconPanel = new JPanel(new BorderLayout());
		iconPanel.add(new JLabel(ImageLoader.loadIcon("info")), BorderLayout.NORTH);
		iconPanel.setOpaque(false);

		add(iconPanel, BorderLayout.WEST);

		Color exBgColor = getBackground();
		setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, exBgColor));

		textArea = new JTextArea();
		textArea.setText(description);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(GUIColors.getColorByName("descriptionTextColor"));
		textArea.setBackground(GUIColors.getColorByName("descriptionBackgroundColor"));
		textArea.setBorder(null);
		textArea.setFont(new JLabel().getFont());
		textArea.setEditable(false);
		textArea.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);

		add(scrollPane, BorderLayout.CENTER);
		setBackground(GUIColors.getColorByName("descriptionBackgroundColor"));
	}
}
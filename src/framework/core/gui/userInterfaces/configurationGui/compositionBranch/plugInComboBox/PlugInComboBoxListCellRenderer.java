package framework.core.gui.userInterfaces.configurationGui.compositionBranch.plugInComboBox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.GUIColors;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * A cell renderer that is able to display StafefulPlugInBeans especially the filtered and forbidden
 * flags.
 * 
 * @author Marius Fink
 * 
 */
public class PlugInComboBoxListCellRenderer implements ListCellRenderer<StatefulViewPlugIn> {

	/**
	 * Styles the labels of the corresponding PlugIn in the JList according to its filtered and
	 * illegal state
	 * 
	 * @param plugIn
	 *            the plugIn
	 * @param labels
	 *            the corresponding labels
	 */
	private void updateCell(StatefulViewPlugIn plugIn, JLabel plugInIdLabel, JPanel iconPanel) {

		// reset font
		plugInIdLabel.setFont(new JLabel().getFont());
		// reset color
		plugInIdLabel.setForeground(GUIColors.getColorByName("standardForeground"));

		// states:
		// SIF (SOURCE ILLEGAL FILTERED)
		// +++ X
		// ++- X
		// +-+ filtered source "->"
		// +-- source ">"
		// -++ illegal filtered "x-"
		// -+- illegal "x"
		// --+ filtered "-"
		// --- ok "v"

		if (plugIn.isSourceIllegal()) {
			addIcon(iconPanel, "source");
		}
		if (plugIn.isFiltered()) {
			addIcon(iconPanel, "filter");
			strikeThrough(plugInIdLabel);
		}
		if (plugIn.isIllegal()) {
			addIcon(iconPanel, "invalid");
			makeRed(plugInIdLabel);
		}

		if (!plugIn.isSourceIllegal() && !plugIn.isFiltered() && !plugIn.isIllegal()) {
			addIcon(iconPanel, "valid");
		}
	}

	/**
	 * adds an icon
	 * 
	 * @param iconPanel
	 *            the panel to add an icon
	 * @param string
	 *            the identifier of the icon
	 */
	private void addIcon(JPanel iconPanel, String string) {
		iconPanel.add(new JLabel(ImageLoader.loadIcon(string, 10)));
	}

	/**
	 * makes the foreground the "illegalPlugin"-color
	 * 
	 * @param plugInIdLabel
	 *            the label to set the foreground
	 */
	private void makeRed(JLabel plugInIdLabel) {
		plugInIdLabel.setForeground(GUIColors.getColorByName("illegalPlugIn"));
	}

	/**
	 * changes the font adds attribute 'strike-through'
	 * 
	 * @param label
	 *            the label to strike through
	 */
	@SuppressWarnings("unchecked")
	private void strikeThrough(JLabel label) {
		Font f = label.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = f.getAttributes();
		attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		label.setFont(f.deriveFont(attributes));
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends StatefulViewPlugIn> list,
			final StatefulViewPlugIn value, int index, boolean isSelected, boolean cellHasFocus) {
		JPanel cellPanel = new JPanel(new BorderLayout(3, 0));

		if (value != null) {
			if (isSelected) {
				cellPanel.setBackground(list.getSelectionBackground());
				cellPanel.setForeground(list.getSelectionForeground());
			} else {
				cellPanel.setBackground(list.getBackground());
				cellPanel.setForeground(list.getForeground());
			}

			final JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			final JLabel plugInIdLabel = new JLabel();
			plugInIdLabel.setText(value.getId());

			cellPanel.add(plugInIdLabel, BorderLayout.CENTER);
			cellPanel.add(iconPanel, BorderLayout.WEST);

			// update cell whenever the state changes
			value.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateCell(value, plugInIdLabel, iconPanel);
				}
			});

			// style
			updateCell(value, plugInIdLabel, iconPanel);

			return cellPanel;
		} else {
			return new JLabel(GUIText.getText("noMatchingPlugin"));
		}
	}
}

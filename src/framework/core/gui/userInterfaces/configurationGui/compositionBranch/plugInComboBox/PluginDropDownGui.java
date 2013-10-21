package framework.core.gui.userInterfaces.configurationGui.compositionBranch.plugInComboBox;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * A small component with a textfield a navigator button and a dropdown box.
 * 
 * @author Marius Fink
 * 
 */
public class PluginDropDownGui {

	private JPanel mainPanel;
	private JComboBox<StatefulViewPlugIn> box;
	private JButton editBtn;
	private String text;

	/**
	 * Create a new DropDown GUI
	 * 
	 * @param type
	 *            the title that will be displayed next to the dropdown box
	 */
	public PluginDropDownGui(String type) {
		this.text = type;
		init();
	}

	/**
	 * Initilizes this component.
	 */
	private void init() {
		mainPanel = new JPanel(new BorderLayout(2, 0));

		PlugInComboBoxListCellRenderer plugInComboBoxListCellRenderer = new PlugInComboBoxListCellRenderer();

		box = new JComboBox<StatefulViewPlugIn>();
		box.setRenderer(plugInComboBoxListCellRenderer);

		editBtn = new JButton(ImageLoader.loadIcon("edit", 14));
		editBtn.setMargin(new Insets(0, 0, 0, 0));

		JLabel cm = new JLabel(text);
		mainPanel.add(cm, BorderLayout.WEST);
		mainPanel.add(box, BorderLayout.CENTER);
		mainPanel.add(editBtn, BorderLayout.EAST);
	}

	/**
	 * @return the edit (navigation) button
	 */
	public JButton getEditButton() {
		return editBtn;
	}

	/**
	 * @return the combobox
	 */
	public JComboBox<StatefulViewPlugIn> getComboBox() {
		return box;
	}

	/**
	 * @return the component
	 */
	public JPanel getPanel() {
		return mainPanel;
	}
}

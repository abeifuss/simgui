package framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.staticFunctionBranches;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.util.visualUtilities.GUIColors;
import framework.core.gui.util.visualUtilities.ImageLoader;

public class MultipleStaticFunctionBoxGui {

	private JPanel mainPanel;
	private String title;
	private DefaultListModel<StaticFunctionBean> listModel;
	private JButton addButton;
	private JButton deleteButton;
	private JList<StaticFunctionBean> list;

	public MultipleStaticFunctionBoxGui(String title) {
		this.title = title;
		init();
	}

	private void init() {
		mainPanel = new JPanel(new BorderLayout(3, 0));
		mainPanel.setBorder(BorderFactory.createLineBorder(GUIColors.getColorByName("metagray")));

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(new JLabel(title));
		mainPanel.add(titlePanel, BorderLayout.NORTH);

		listModel = new DefaultListModel<StaticFunctionBean>();

		list = new JList<StaticFunctionBean>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel borderWrapper = new JPanel(new BorderLayout());
		borderWrapper.add(new JScrollPane(list), BorderLayout.CENTER);
		borderWrapper.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		mainPanel.add(borderWrapper, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addButton = new JButton(ImageLoader.loadIcon("add", 10));
		addButton.setMargin(new Insets(0, 5, 0, 5));

		deleteButton = new JButton(ImageLoader.loadIcon("delete", 10));
		deleteButton.setMargin(new Insets(0, 0, 0, 0));

		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	public JComponent getComponent() {
		return mainPanel;
	}

	public DefaultListModel<StaticFunctionBean> getListModel() {
		return listModel;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public JButton getRemoveButton() {
		return deleteButton;
	}
	
	/**
	 * @return the selected index of the list or -1 if there is nothing selected
	 */
	public JList<StaticFunctionBean> getList() {
		return list;
	}
}

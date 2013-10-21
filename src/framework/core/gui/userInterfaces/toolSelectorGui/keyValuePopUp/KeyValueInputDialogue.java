package framework.core.gui.userInterfaces.toolSelectorGui.keyValuePopUp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * This is a small table in a modal dialog frame that can handle Lists of pairs
 * 
 * @author Marius Fink
 * @version 01.06.2012
 */
public class KeyValueInputDialogue {
	private List<KeyValuePair<String, String>> data;
	private DefaultTableModel tableModel;
	private static final Dimension DIMENSION = new Dimension(600, 200);

	/**
	 * The constructor of KeyValueInputDialogue converts the String initially to a KeyValue List for
	 * internal use.
	 * 
	 * @param initial
	 *            the initial String to be parsed
	 * @param keyValueSeparator
	 *            the separator for key and value
	 * @param pairSeparator
	 *            the separator for key-value pairs
	 */
	private KeyValueInputDialogue(String title, List<KeyValuePair<String, String>> current) {
		this.data = current;
	}

	/**
	 * @return creates the root pane
	 */
	protected JPanel getRootPanel() {
		JPanel entryPanel = new JPanel(new BorderLayout(0, 0));
		JPanel spacingPanel = new JPanel(new BorderLayout(0, 0));

		tableModel = new DefaultTableModel();
		JTable tablePanel = new JTable(tableModel);

		// Headlines
		tableModel.addColumn(GUIText.getText("key"));
		tableModel.addColumn(GUIText.getText("value"));

		for (KeyValuePair<String, String> pair : data) {
			tableModel.insertRow(tableModel.getRowCount(), new Object[] { pair.getKey(), pair.getValue() });
		}

		JScrollPane jScrollPane = new JScrollPane(tablePanel);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spacingPanel.add(jScrollPane, BorderLayout.NORTH);
		spacingPanel.add(new JPanel(), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton addRowBtn = new JButton(GUIText.getText("addRow"), ImageLoader.loadIcon("add"));
		addRowBtn.setMargin(new Insets(2, 4, 2, 4));
		addRowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addRow();
			}
		});
		buttonPanel.add(addRowBtn);

		spacingPanel.add(buttonPanel, BorderLayout.SOUTH);

		entryPanel.add(spacingPanel, BorderLayout.CENTER);

		jScrollPane.setPreferredSize(DIMENSION);
		jScrollPane.setMaximumSize(DIMENSION);
		jScrollPane.setMinimumSize(DIMENSION);
		jScrollPane.setSize(DIMENSION);
		return entryPanel;
	}

	/**
	 * Dynamically adds a new row
	 */
	private void addRow() {
		tableModel.insertRow(tableModel.getRowCount(), new Object[] { "", "" });
	}

	/**
	 * Changes the internal data set
	 */
	private void changeData() {
		data = new LinkedList<KeyValuePair<String, String>>();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (tableModel.getValueAt(i, 0) != null && tableModel.getValueAt(i, 1) != null) {
				if (!tableModel.getValueAt(i, 0).toString().matches(" *"))
					data.add(new KeyValuePair<String, String>(tableModel.getValueAt(i, 0).toString(), tableModel
							.getValueAt(i, 1).toString()));
			}
		}
	}

	/**
	 * Stops the GUI thread and shows an input dialogue for key-value-pairs. Returns the new String
	 * of key-value-pairs on confirmation or the original String on abortion.
	 * 
	 * @param title
	 *            the title of the window
	 * @param keyValueString
	 *            the to-be-processed key-value-String
	 * @param kvSep
	 *            the separator for key-value, e.g. "=" in key=val;key2=val2
	 * @param pairSep
	 *            the separator for key-value-pairs, e.g. ";" in key=val;key2=val2
	 * @return the new String of key-value-pairs on confirmation or the original String on abortion,
	 *         null never
	 */
	public static List<KeyValuePair<String, String>> editKeyValueOptions(String title, List<KeyValuePair<String, String>> current) {
		KeyValueInputDialogue dialog = new KeyValueInputDialogue(title, current);
		int showConfirmDialog = JOptionPane.showConfirmDialog(null, dialog.getRootPanel(), title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		if (showConfirmDialog == JOptionPane.OK_OPTION) {
			dialog.changeData();
			return dialog.data;
		} else {
			return current;
		}
	}
}
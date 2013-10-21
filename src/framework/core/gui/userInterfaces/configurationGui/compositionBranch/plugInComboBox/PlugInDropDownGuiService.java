package framework.core.gui.userInterfaces.configurationGui.compositionBranch.plugInComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import framework.core.gui.model.PlugInBean;
import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.util.GuiNavigator;

/**
 * The service for the dropdown combobox for pugin compositions.
 * 
 * @author Marius Fink
 * 
 */
public class PlugInDropDownGuiService {

	private PluginDropDownGui gui;
	private List<StatefulViewPlugIn> toDisplay;
	private ActionListener currentEditButtonActionListener = null;
	private String typeString;

	/**
	 * Initialize it with MIX/CLIENT as title and the initial values to display
	 * 
	 * @param title
	 *            the title
	 * @param toDisplay
	 *            the plugins
	 */
	public PlugInDropDownGuiService(String title, List<StatefulViewPlugIn> toDisplay) {
		this.toDisplay = toDisplay;
		this.typeString = title;
		init();
	}

	/**
	 * Initializes the component
	 */
	private void init() {
		gui = new PluginDropDownGui(typeString);
		gui.getComboBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleEditButtonState();
				notifyListeners();
			}
		});

		for (StatefulViewPlugIn p : toDisplay) {
			gui.getComboBox().addItem(p);
		}
		handleEditButtonState();
	}

	protected void handleEditButtonState() {
		gui.getEditButton().removeActionListener(currentEditButtonActionListener);
		if (gui.getComboBox().getSelectedItem() instanceof PlugInBean && gui.getComboBox().getSelectedItem() != null) {
			final PlugInBean selected = (PlugInBean) gui.getComboBox().getSelectedItem();
			currentEditButtonActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GuiNavigator.getInstance().navigateToPluginSettings(selected);
				}
			};
			gui.getEditButton().addActionListener(currentEditButtonActionListener);
			gui.getEditButton().setEnabled(true);
		} else {
			currentEditButtonActionListener = null;
			gui.getEditButton().setEnabled(false);
		}

	}

	/**
	 * @return (as you might expect) the panel of this gui component.
	 */
	public JPanel getPanel() {
		return gui.getPanel();
	}

	private List<ActionListener> actionListeners = new LinkedList<ActionListener>();

	/**
	 * Adds an actionlistener that is called on every change
	 * 
	 * @param al
	 *            the actionlistener to bind to the tree
	 */
	public void addActionListener(ActionListener al) {
		actionListeners.add(al);
	}

	/**
	 * Notifies the registered Listeners.
	 */
	private void notifyListeners() {
		ActionEvent ae = new ActionEvent(this, 0, "Node selected");
		for (ActionListener al : actionListeners) {
			al.actionPerformed(ae);
		}
	}

	/**
	 * @return the currently chosen plug-in or null, if there is no matching plug-in available.
	 *         Attention: Also returns illegal and filtered plug-ins.
	 */
	public StatefulViewPlugIn getCurrentlyChosenPlugIn() {
		int i = gui.getComboBox().getSelectedIndex();
		if (i != -1) {
			return gui.getComboBox().getItemAt(i);
		} else {
			return null;
		}
	}

	/**
	 * Automatically switch to a plug-in, identified by an id. If there is no plug-in with this id,
	 * nothing (if poosible) or the first item will be choosen.
	 * 
	 * @param id
	 *            the id of the plug-in to choose
	 */
	public void choosePlugin(String id) {
		for (StatefulViewPlugIn plugin : toDisplay) {
			if (plugin.getId().equals(id)) {
				gui.getComboBox().setSelectedItem(plugin);
			}
		}
	}

	/**
	 * Updates the UI of the DropDown Menu (after filtering)
	 */
	public void updateUI() {
		gui.getComboBox().updateUI();
	}

}

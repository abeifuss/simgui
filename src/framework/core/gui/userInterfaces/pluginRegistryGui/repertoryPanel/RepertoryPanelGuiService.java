package framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import framework.core.gui.model.Treeable;
import framework.core.gui.services.TreeServiceInputSource;
import framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayerTreeService;
import framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayeredResourceTreeService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * This class is the service for any "repertory" - those are panels with add, delete and refresh
 * capability, a headline and a PluginLayeredTree.
 * 
 * @author Marius Fink
 * @version 16.10.2012
 */
public class RepertoryPanelGuiService<T extends Treeable> {

	private static final Logger LOGGER = Logger.getLogger(RepertoryPanelGuiService.class);
	private RepertoryPanelGui repertoryPanelGui;
	private List<ActionListener> actionListeners = new LinkedList<ActionListener>();
	private JButton addButton;
	private JButton deleteButton;
	private JButton refreshButton;
	private LayerTreeService<?> pluginLayerTreeSvc;
	private TreeServiceInputSource<T> treeInputSource;

	/**
	 * Constructor of RepertoryPanelGuiService initializes the component.
	 * 
	 * @param title
	 *            the title of this repertory
	 * @param icon
	 *            the corresponding icon
	 * @param treeInputSource
	 *            the input source for the tree in this repertory
	 */
	public RepertoryPanelGuiService(String title, ImageIcon icon,
			TreeServiceInputSource<T> treeInputSource) {
		repertoryPanelGui = new RepertoryPanelGui(title, icon);
		this.treeInputSource = treeInputSource;
		init();
	}

	/**
	 * Initializes the GUI
	 */
	private void init() {
		pluginLayerTreeSvc = new LayeredResourceTreeService<T>(treeInputSource);
		pluginLayerTreeSvc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handlePluginRepertoryButtonStates();
				notifyListeners();
			}
		});

		addButton = repertoryPanelGui.addButton(GUIText.getText("addPlugin"),
				ImageLoader.loadIcon("add", 12));
		deleteButton = repertoryPanelGui.addButton(GUIText.getText("deletePlugin"),
				ImageLoader.loadIcon("delete", 12));
		refreshButton = repertoryPanelGui.addButton(GUIText.getText("refreshPluginRegistry"),
				ImageLoader.loadIcon("refresh", 12));
		repertoryPanelGui.setContent(pluginLayerTreeSvc.getTree());

		handlePluginRepertoryButtonStates();
	}

	/**
	 * Forces the Tree Service to rescan its values.
	 */
	public void refreshValues() {
		pluginLayerTreeSvc.refresh();
	}

	/**
	 * Handles the enabling and disabling of the add and delette buttons.
	 */
	private void handlePluginRepertoryButtonStates() {
		addButton.setEnabled(pluginLayerTreeSvc.isAddButtonActive());
		deleteButton.setEnabled(pluginLayerTreeSvc.isDeleteButtonActive());
	}

	/**
	 * @return the GUI
	 */
	public RepertoryPanelGui getRepertoryPanel() {
		return repertoryPanelGui;
	}

	/**
	 * Adds an actionlistener that is called on every change of the selected item in the tree
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
	 * Clears the selection
	 */
	public void clearSelection() {
		pluginLayerTreeSvc.getTree().clearSelection();
	}

	/**
	 * @return the add (+) button
	 */
	public JButton getAddButton() {
		return addButton;
	}

	/**
	 * @return the delete (trash, -) button
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * @return the refresh (reload) button
	 */
	public JButton getRefreshButton() {
		return refreshButton;
	}

	/**
	 * @return the selected user (!) object or null if no user object is selected
	 */
	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		try {
			return (T) pluginLayerTreeSvc.getSelectedItem();
		} catch (Exception e) {
			LOGGER.error("Could not select Tree Element: ", e);
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionListeners == null) ? 0 : actionListeners.hashCode());
		result = prime * result + ((addButton == null) ? 0 : addButton.hashCode());
		result = prime * result + ((deleteButton == null) ? 0 : deleteButton.hashCode());
		result = prime * result
				+ ((pluginLayerTreeSvc == null) ? 0 : pluginLayerTreeSvc.hashCode());
		result = prime * result + ((refreshButton == null) ? 0 : refreshButton.hashCode());
		result = prime * result + ((repertoryPanelGui == null) ? 0 : repertoryPanelGui.hashCode());
		result = prime * result + ((treeInputSource == null) ? 0 : treeInputSource.hashCode());
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RepertoryPanelGuiService))
			return false;
		RepertoryPanelGuiService<?> other = (RepertoryPanelGuiService<?>) obj;
		if (actionListeners == null) {
			if (other.actionListeners != null)
				return false;
		} else if (!actionListeners.equals(other.actionListeners))
			return false;
		if (addButton == null) {
			if (other.addButton != null)
				return false;
		} else if (!addButton.equals(other.addButton))
			return false;
		if (deleteButton == null) {
			if (other.deleteButton != null)
				return false;
		} else if (!deleteButton.equals(other.deleteButton))
			return false;
		if (pluginLayerTreeSvc == null) {
			if (other.pluginLayerTreeSvc != null)
				return false;
		} else if (!pluginLayerTreeSvc.equals(other.pluginLayerTreeSvc))
			return false;
		if (refreshButton == null) {
			if (other.refreshButton != null)
				return false;
		} else if (!refreshButton.equals(other.refreshButton))
			return false;
		if (repertoryPanelGui == null) {
			if (other.repertoryPanelGui != null)
				return false;
		} else if (!repertoryPanelGui.equals(other.repertoryPanelGui))
			return false;
		if (treeInputSource == null) {
			if (other.treeInputSource != null)
				return false;
		} else if (!treeInputSource.equals(other.treeInputSource))
			return false;
		return true;
	}

}

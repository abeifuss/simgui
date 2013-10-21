package framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision;

import javax.swing.tree.DefaultMutableTreeNode;

import framework.core.gui.model.Treeable;
import framework.core.gui.services.TreeServiceInputSource;
import framework.core.gui.util.GUIText;

/**
 * PluginLayeredTreeService is the service for the GUI of a JPanel with a JTree that
 * contains data from the given datasource. All the objects of this tree can be returned
 * but must implement framework.core.gui.model.Treeable !
 * 
 * @author Marius Fink
 * @version 23.10.2012
 * @param <T>
 *            The Type of Objects to put in the tree leafs.
 */
public class LayeredResourceTreeService<T extends Treeable> extends LayerTreeService<T> {
    private TreeServiceInputSource<T> inputSource;

    /**
     * Construktor of class PluginLayeredTreeService initializes the component and calls
     * refresh to draw the GUI
     * 
     * @param inputSource
     *            the source to gather the to be displayed data from
     */
    public LayeredResourceTreeService(TreeServiceInputSource<T> inputSource) {
	this.inputSource = inputSource;
	rootNode = new DefaultMutableTreeNode("Plug-ins");
	refresh();

	// IMPORTANT!
	super.init();
    }

    /**
     * (non-Javadoc)
     * @see framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayerTreeService#refresh()
     */
    @Override
    public void refresh() {
	rootNode.removeAllChildren();
	for (int i = 1; i <= 5; i++) {
	    DefaultMutableTreeNode layerNode = new DefaultMutableTreeNode(GUIText.getText("layer"
		    + i));
	    for (T userObject : inputSource.getTreeObjectsForLayer(i)) {
		DefaultMutableTreeNode objectNode = new DefaultMutableTreeNode(userObject);
		layerNode.add(objectNode);
	    }
	    rootNode.add(layerNode);
	}
	super.updateTreeGui();
    }

    /**
     * @return true if any user object is selected, false on rootnode or layer nodes.
     */
    private boolean isUserObjectSelected() {
	if (tree.getLastSelectedPathComponent() != null) {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		    .getLastSelectedPathComponent();
	    return (node.getUserObject() instanceof Treeable);
	} else {
	    return false;
	}
    }

    /**
     * @return true, if any layer node is selected, false on rootnode or user object nodes
     */
    @SuppressWarnings("unused")
    private boolean isLayerSelected() {
	if (tree.getLastSelectedPathComponent() != null) {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		    .getLastSelectedPathComponent();
	    return (!node.equals(rootNode)) && !isUserObjectSelected();
	} else {
	    return false;
	}
    }

    /**
     * (non-Javadoc)
     * @see framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayerTreeService#getSelectedItem()
     */
    @SuppressWarnings("unchecked")
    public T getSelectedItem() {
	if (isUserObjectSelected()) {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		    .getLastSelectedPathComponent();
	    if (node.getUserObject() instanceof Treeable) {
		return (T) node.getUserObject();
	    } else {
		return null;
	    }
	} else {
	    return null;
	}
    }

    /**
     * (non-Javadoc)
     * @see framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayerTreeService#isAddButtonActive()
     */
    @Override
    public boolean isAddButtonActive() {
	//return isLayerSelected();
	return true;
    }

    /**
     * (non-Javadoc)
     * @see framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayerTreeService#isDeleteButtonActive()
     */
    @Override
    public boolean isDeleteButtonActive() {
	return isUserObjectSelected();
    }

    /**
     * (non-Javadoc)
     * @see framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision.LayerTreeService#getRootNode()
     */
    @Override
    public DefaultMutableTreeNode getRootNode() {
	return rootNode;
    }
}

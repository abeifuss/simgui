package framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.treeVision;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import framework.core.gui.model.Treeable;

public abstract class LayerTreeService<T extends Treeable> {

    protected JTree tree;
    protected DefaultMutableTreeNode rootNode;
    private List<ActionListener> actionListeners = new LinkedList<ActionListener>();

    protected void init() {
	tree = new JTree(rootNode);
	tree.setRootVisible(true);
	tree.setModel(new DefaultTreeModel(getRootNode()));
	tree.setShowsRootHandles(true);
	tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.addTreeSelectionListener(new TreeSelectionListener() {
	    @Override
	    public void valueChanged(TreeSelectionEvent arg0) {
		notifyListeners();
	    }
	});

    }

    /**
     * @return the tree
     */
    public JTree getTree() {
	return tree;
    }

    /**
     * @param tree
     *            the tree to set
     */
    public void setTree(JTree tree) {
	this.tree = tree;
    }

    public void addActionListener(ActionListener al) {
	actionListeners.add(al);
    }

    private void notifyListeners() {
	ActionEvent ae = new ActionEvent(this, 0, "Node selected");
	for (ActionListener al : actionListeners) {
	    al.actionPerformed(ae);
	}
    }
    
    protected void updateTreeGui() {
	if (tree != null) {
	    tree.updateUI();
	}
    }

    public abstract DefaultMutableTreeNode getRootNode();

    public abstract boolean isAddButtonActive();

    public abstract boolean isDeleteButtonActive();

    public abstract void refresh();

    public abstract T getSelectedItem();
}
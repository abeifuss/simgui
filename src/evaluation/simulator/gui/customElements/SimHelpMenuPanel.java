package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.customElements.structure.HelpTreeNode;
import evaluation.simulator.gui.helper.ValueComparator;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

/**
 * Builds the menu for the {@link SimHelpContentPanel}
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("serial")
public class SimHelpMenuPanel extends JPanel implements TreeSelectionListener {

	private static Logger logger = Logger.getLogger(SimHelpMenuPanel.class);

	private static SimHelpMenuPanel instance = null;

	private static String path = "etc/html/plugins/";
	private static Map<String, String> layerMapDisplayNameToConfigName;
	private static Map<String, SimProp> propertyMap;
	private static Map<String, String> registeredPlugins;

	private static JTree tree;

	/**
	 * Singleton
	 * 
	 * @return an instance of {@link SimHelpMenuPanel}
	 */
	public static SimHelpMenuPanel getInstance() {
		if (instance == null) {
			instance = new SimHelpMenuPanel();
		}
		return instance;
	}

	private SimHelpMenuPanel() {
		this.initialize();
	}

	private void initialize() {

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("gMixSim Help");
		createNodes(top);
		tree = new JTree(top);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(this);

		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);

		this.add(new JScrollPane(tree), BorderLayout.CENTER);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode node = null;

		layerMapDisplayNameToConfigName = SimPropRegistry.getInstance().getLayerMapDisplayNameToConfigName();
		propertyMap = SimPropRegistry.getInstance().getProperties();
		registeredPlugins = SimPropRegistry.getInstance().getRegisteredPlugins();

		Map<String, Integer> layerMap = SimPropRegistry.getInstance().getLayerMapDisplayNameToOrder();
		ValueComparator comperatorLayer = new ValueComparator(layerMap);
		TreeMap<String, Integer> sortedLayerMap = new TreeMap<String, Integer>(comperatorLayer);
		sortedLayerMap.putAll(layerMap);

		for (String layer : sortedLayerMap.keySet()) {
			category = new DefaultMutableTreeNode(layer);
			top.add(category);

			for (String prop : propertyMap.keySet()) {
				if (propertyMap.get(prop).getPluginID().equals("")
						&& (propertyMap.get(prop).isSuperclass() || propertyMap.get(prop).isGlobal())
						&& propertyMap.get(prop).getPluginLayerID().equals(layerMapDisplayNameToConfigName.get(layer))) {
					System.out.println(path + prop + ".html");
					node = new DefaultMutableTreeNode(new HelpTreeNode(prop, path + prop + ".html"));

					category.add(node);
				}
			}
			for (String plugin : registeredPlugins.keySet()) {
				if (registeredPlugins.get(plugin).equals(layerMapDisplayNameToConfigName.get(layer))) {

					node = new DefaultMutableTreeNode(new HelpTreeNode(plugin, path + plugin + ".html"));
					category.add(node);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		// Returns the last path element of the selection.
		// This method is useful only when the selection model allows a single
		// selection.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (node == null) {
			return;
		}
		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			HelpTreeNode helpTreeNode = (HelpTreeNode) nodeInfo;
			System.err.println(helpTreeNode.getHelpTreeNodeName() + " " + helpTreeNode.getHelpTreeNodeURL());
			displayURL(helpTreeNode.getHelpTreeNodeURL());
		}
	}

	/**
	 * Loads a given url into the SimHelpContentPanel
	 * @param helpTreeNodeURL is the url which should be loaded
	 */
	private void displayURL(String helpTreeNodeURL) {
		SimHelpContentPanel p = SimHelpContentPanel.getInstance();
		String urlString = helpTreeNodeURL.toString();
		p.loadURL(urlString);
		p.repaint();
	}

}
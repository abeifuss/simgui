package evaluation.simulator.gui.customElements;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.customElements.accordion.AccordionEntry;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

@SuppressWarnings("serial")
public class PluginPanel extends JScrollPane {

	private JPanel _panel;
	HashMap<String, JComboBox<String>> pluginListsMap = new HashMap<>();

	SimPropRegistry simPropRegistry;

	public PluginPanel() {
		this.initPanel();
	}

	private List<SimProp> getSections() {
		List<SimProp> list = new LinkedList<SimProp>();

		Set<Entry<String, SimProp>> e = this.simPropRegistry.getAllSimProps();

		Iterator<Entry<String, SimProp>> iter = e.iterator();
		while (iter.hasNext()) {
			Entry<?, ?> entry = iter.next();
			list.add((SimProp) entry.getValue());
		}

		return list;
	}

	private void initPanel() {
		// Start Layout
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.getVerticalScrollBar().setUnitIncrement(16);

		this._panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this._panel, gridBagConstraints);
		this._panel.setLayout(gridBagLayout);

		this.setViewportView(this._panel);

		// End Layout

		this.simPropRegistry = SimPropRegistry.getInstance();

		Map<String, String>[] PluginLayerMap = this.simPropRegistry
				.getPlugIns();

		List<String> pluginOrder = this.simPropRegistry.getPluginLevels();

		String[] levelStrings[] = {
				PluginLayerMap[0].keySet().toArray(
						new String[PluginLayerMap[0].size()]),
				PluginLayerMap[1].keySet().toArray(
						new String[PluginLayerMap[1].size()]),
				PluginLayerMap[2].keySet().toArray(
						new String[PluginLayerMap[2].size()]),
				PluginLayerMap[3].keySet().toArray(
						new String[PluginLayerMap[3].size()]),
				PluginLayerMap[4].keySet().toArray(
						new String[PluginLayerMap[4].size()]),
				PluginLayerMap[5].keySet().toArray(
						new String[PluginLayerMap[5].size()]),
				PluginLayerMap[6].keySet().toArray(
						new String[PluginLayerMap[6].size()]) };

		// CLIENT_SEND_STYLE
		JComboBox<String> plugInLevel1List = new JComboBox<String>(
				levelStrings[0]);
		this.pluginListsMap.put(pluginOrder.get(0), plugInLevel1List);

		JComboBox<String> plugInLevel2List = new JComboBox<String>(
				levelStrings[1]);
		this.pluginListsMap.put(pluginOrder.get(1), plugInLevel2List);

		JComboBox<String> plugInLevel3List = new JComboBox<String>(
				levelStrings[2]);
		this.pluginListsMap.put(pluginOrder.get(2), plugInLevel3List);

		JComboBox<String> plugInLevel4List = new JComboBox<String>(
				levelStrings[3]);
		this.pluginListsMap.put(pluginOrder.get(3), plugInLevel4List);

		JComboBox<String> plugInLevel5List = new JComboBox<String>(
				levelStrings[4]);
		this.pluginListsMap.put(pluginOrder.get(4), plugInLevel5List);

		JComboBox<String> plugInLevel6List = new JComboBox<String>(
				levelStrings[5]);
		this.pluginListsMap.put(pluginOrder.get(5), plugInLevel6List);

		JComboBox<String> plugInLevel7List = new JComboBox<String>(
				levelStrings[6]);
		this.pluginListsMap.put(pluginOrder.get(6), plugInLevel7List);

		for (String chooseString : this.pluginListsMap.keySet()) {
			this.pluginListsMap.get(chooseString).insertItemAt(
					"Choose your " + chooseString + " plugin...", 0);
			this.pluginListsMap.get(chooseString).setSelectedIndex(0);
		}

		AccordionEntry accordionElement;

		List<SimProp> listofAllSimProperties = this.getSections();
		Set<String> listOfAllPluginLayers = new HashSet<String>();

		Iterator<SimProp> simPropertyIterator = listofAllSimProperties
				.iterator();
		while (simPropertyIterator.hasNext()) {
			String plugInName = simPropertyIterator.next().getPluginLayer();
			listOfAllPluginLayers.add(plugInName);
		}

		// Select the categories
		Iterator<String> plugInNameIterator = listOfAllPluginLayers.iterator();
		while (plugInNameIterator.hasNext()) {
			String plugInName = plugInNameIterator.next();

			Logger.Log(LogLevel.DEBUG, "New Accordion Entry for " + plugInName);
			accordionElement = new AccordionEntry(plugInName,
					this.pluginListsMap.get(plugInName));
			this._panel.add(accordionElement, gridBagConstraints);
		}

		gridBagConstraints.weighty = 1;

		// Spring element to push all other elements to the top
		// needed for alignment
		JPanel jp = new JPanel();
		this._panel.add(jp, gridBagConstraints);

		this.setVisible(true);
	}

	void setPlugin(String pluginLevel, String selectedPlugin) {
		Logger.Log(LogLevel.DEBUG, "Loaded pluginLevel " + pluginLevel + " to "
				+ selectedPlugin);
		this.pluginListsMap.get(pluginLevel).setSelectedItem(selectedPlugin);
	}

	public void update() {
		final Map<String, String> activePlugins = simPropRegistry.getActivePlugins();
		for (final String pluginLevel : activePlugins.keySet()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setPlugin(pluginLevel, activePlugins.get(pluginLevel));
				}
			});
		}
	}
}

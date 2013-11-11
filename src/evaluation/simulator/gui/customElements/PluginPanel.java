package evaluation.simulator.gui.customElements;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import evaluation.simulator.gui.customElements.accordion.AccordionEntry;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

@SuppressWarnings("serial")
public class PluginPanel extends JScrollPane {

	private JPanel panel;
	private JPanel pluginSelectionPanel;
	private JPanel globalPreferencesPanel;
	HashMap<String, JComboBox<String>> pluginListsMap = new HashMap<>();

	SimPropRegistry simPropRegistry;

	public PluginPanel() {
		this.initPanel();
	}

	private class ValueComparator implements Comparator<String> {

		Map<String, Integer> base;
		public ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with equals.
		@Override
		public int compare(String a, String b) {
			if (this.base.get(a) <= this.base.get(b)) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	private void initPanel() {
		// Start Layout
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.getVerticalScrollBar().setUnitIncrement(16);

		this.pluginSelectionPanel = new JPanel();
		this.pluginSelectionPanel.setBorder(new TitledBorder(null, "Plugin Configuration",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.pluginSelectionPanel, gridBagConstraints);
		this.pluginSelectionPanel.setLayout(gridBagLayout);

		this.setViewportView(this.pluginSelectionPanel);

		// End Layout

		this.simPropRegistry = SimPropRegistry.getInstance();

		Map<String, Integer> layerMap = this.simPropRegistry.getLayerMapDisplayNameToOrder();
		// Sort the map by value (first) and key (second)
		ValueComparator comperatorLayer =  new ValueComparator(layerMap);
		TreeMap<String,Integer> sortedLayerMap = new TreeMap<String,Integer>(comperatorLayer);
		sortedLayerMap.putAll(layerMap);

		// Sort the static configurations by value
		Map<String, Integer> staticMap = this.simPropRegistry.getStaticConfigurationDisplay();
		ValueComparator comperatorStatic =  new ValueComparator(staticMap);
		TreeMap<String,Integer> sortedStaticMap = new TreeMap<String,Integer>(comperatorStatic);
		sortedStaticMap.putAll(staticMap);

		System.out.println("results: "+sortedLayerMap);

		String[] levelStrings[] = new String[sortedLayerMap.size()][];

		AccordionEntry accordionElement;
		this.globalPreferencesPanel = new JPanel();

		int i = 0;
		for ( String layer : sortedLayerMap.keySet() ){

			levelStrings[i] = SimPropRegistry.getInstance().getPluginsInLayer( layer );

			String key = layer;
			this.pluginListsMap.put(key, new JComboBox<String>(levelStrings[i]));
			this.pluginListsMap.get(key).insertItemAt("Choose your " + key + " plugin", 0);
			this.pluginListsMap.get(key).setSelectedIndex(0);

			Logger.Log(LogLevel.DEBUG, "New Accordion Entry for " + key);
			accordionElement = new AccordionEntry(key, this.pluginListsMap.get(key));
			this.pluginSelectionPanel.add(accordionElement, gridBagConstraints);

			i++;
		}
		i = 0;
		for ( String layer : sortedStaticMap.keySet() ){

			levelStrings[i] = SimPropRegistry.getInstance().getPluginsInLayer( layer );

			String key = layer;
			this.pluginListsMap.put(key, new JComboBox<String>(levelStrings[i]));
			this.pluginListsMap.get(key).insertItemAt("Choose your " + key + " plugin", 0);
			this.pluginListsMap.get(key).setSelectedIndex(0);

			Logger.Log(LogLevel.DEBUG, "New Accordion Entry for " + key);
			accordionElement = new AccordionEntry(key, this.pluginListsMap.get(key));
			this.globalPreferencesPanel.add(accordionElement, gridBagConstraints);

			i++;
		}

		this.panel = new JPanel();
		this.panel.add(this.pluginSelectionPanel);
		this.panel.add(this.globalPreferencesPanel);

		gridBagConstraints.weighty = 1;

		// Spring element to push all other elements to the top
		// needed for alignment
		JPanel jp = new JPanel();
		this.panel.add(jp, gridBagConstraints);

		this.setVisible(true);
	}

	void setPlugin(String configName, String selectedPlugin) {
		String pluginLevel = SimPropRegistry.getInstance().configNameToPluginName(configName);
		Logger.Log(LogLevel.DEBUG, "Loaded pluginLevel " + pluginLevel + " to " + selectedPlugin);

		this.pluginListsMap.get(pluginLevel).setSelectedItem(selectedPlugin);
	}

	public void update() {

		for (Component component : this.panel.getComponents()) {
			if (component.getClass().equals(AccordionEntry.class)) {
				AccordionEntry accordianEntry = (AccordionEntry) (component);
				accordianEntry.setVibility(true);
			} else {
				// Logger.Log(LogLevel.DEBUG, "Found component "+ component.getClass().getName() + " / " + AccordionEntry.class);
			}
		}

		final Map<String, String> activePlugins = this.simPropRegistry
				.getActivePlugins(true);

		for (final String pluginLevel : activePlugins.keySet()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					PluginPanel.this.setPlugin(pluginLevel,activePlugins.get(pluginLevel));
				}
			});
		}
	}
}

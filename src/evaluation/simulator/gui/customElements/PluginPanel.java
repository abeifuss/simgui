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
	private JPanel generalPreferencesPanel;
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

		this.generalPreferencesPanel = new JPanel();
		this.generalPreferencesPanel.setBorder(new TitledBorder(null, "General Configuration",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		this.panel = new JPanel();

		//GridBagLayout for plugin configuration
		GridBagLayout gridBagLayoutPlugins = new GridBagLayout();
		GridBagConstraints gridBagConstraintsPlugins = new GridBagConstraints();
		gridBagConstraintsPlugins.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsPlugins.anchor = GridBagConstraints.NORTH;
		gridBagConstraintsPlugins.weightx = 1;
		gridBagConstraintsPlugins.weighty = 1;
		gridBagConstraintsPlugins.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraintsPlugins.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayoutPlugins.setConstraints(this.pluginSelectionPanel, gridBagConstraintsPlugins);
		this.pluginSelectionPanel.setLayout(gridBagLayoutPlugins);

		//GridBagLayout for general configuration
		GridBagLayout gridBagLayoutGeneral = new GridBagLayout();
		GridBagConstraints gridBagConstraintsGeneral = new GridBagConstraints();
		gridBagConstraintsGeneral.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsGeneral.anchor = GridBagConstraints.CENTER;
		gridBagConstraintsGeneral.weightx = 1;
		gridBagConstraintsGeneral.weighty = 1;
		gridBagConstraintsGeneral.gridx = gridBagConstraintsPlugins.gridx;
		gridBagConstraintsGeneral.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayoutGeneral.setConstraints(this.pluginSelectionPanel, gridBagConstraintsGeneral);
		this.generalPreferencesPanel.setLayout(gridBagLayoutGeneral);

		//GridBagLayout for overall configuration-panel
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.panel, gridBagConstraints);
		this.panel.setLayout(gridBagLayout);

		this.setViewportView(this.panel);

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
		Map<String, Boolean> isStaticLayerMap = SimPropRegistry.getInstance().getIsStaticLayerMap();
		sortedStaticMap.putAll(staticMap);

		System.out.println("results: "+sortedLayerMap);

		String[] levelStrings[] = new String[sortedLayerMap.size()][];
		AccordionEntry accordionElement;

		int i = 0;
		for ( String layer : sortedLayerMap.keySet() ){
			if((isStaticLayerMap.get(layer) == null) || !isStaticLayerMap.get(layer)){
				levelStrings[i] = SimPropRegistry.getInstance().getPluginsInLayer( layer );

				String key = layer;
				this.pluginListsMap.put(key, new JComboBox<String>(levelStrings[i]));
				this.pluginListsMap.get(key).insertItemAt("Choose your " + key + " plugin", 0);
				this.pluginListsMap.get(key).setSelectedIndex(0);
				Logger.Log(LogLevel.DEBUG, "New Accordion Entry for " + key);
				accordionElement = new AccordionEntry(key, this.pluginListsMap.get(key));
				this.pluginSelectionPanel.add(accordionElement, gridBagConstraintsPlugins);

				i++;
			}else{
				levelStrings[i] = SimPropRegistry.getInstance().getPluginsInLayer( layer );

				String key = layer;
				this.pluginListsMap.put(key, new JComboBox<String>(levelStrings[i]));
				this.pluginListsMap.get(key).insertItemAt("Choose your " + key + " plugin", 0);
				this.pluginListsMap.get(key).setSelectedIndex(0);
				Logger.Log(LogLevel.DEBUG, "New Accordion Entry for " + key);
				accordionElement = new AccordionEntry(key, this.pluginListsMap.get(key));
				this.generalPreferencesPanel.add(accordionElement, gridBagConstraintsPlugins);

				i++;
			}

		}

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.panel.add(this.pluginSelectionPanel,gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		this.panel.add(this.generalPreferencesPanel,gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;

		// Spring element to push all other elements to the top
		// needed for alignment
		gridBagConstraints.weighty = 1;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
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

package evaluation.simulator.gui.customElements;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	HashMap<String, JComboBox<String>> pluginListsMap = new HashMap<>();

	SimPropRegistry simPropRegistry;

	public PluginPanel() {
		this.initPanel();
	}
	
	private void initPanel() {
		// Start Layout
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.getVerticalScrollBar().setUnitIncrement(16);

		this.panel = new JPanel();
		this.panel.setBorder(new TitledBorder(null, "Plugin Configuration",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.panel, gridBagConstraints);
		this.panel.setLayout(gridBagLayout);

		this.setViewportView(this.panel);

		// End Layout

		this.simPropRegistry = SimPropRegistry.getInstance();

//		Map<String, String>[] PluginLayerMap = this.simPropRegistry.getPluginLayerMap();
//		List<String> pluginOrder = this.simPropRegistry.getPluginLevels();
		
		// set the order manually until it works automatically (by annotations)
		List<String> order = new LinkedList<>();
		order.add("Load Generator");
		order.add("Mix Client");
		order.add("Mix Proxy");
		order.add("Mix Server");
		order.add("Recoding Scheme");
		order.add("Topology");
		order.add("Underlay-net");
		order.add("Plotter");
		
		String[] levelStrings[] = new String[SimPropRegistry.getInstance().getNumberOfPluginLayers()][];
		
		AccordionEntry accordionElement;
		
		for (int i = 0; i < SimPropRegistry.getInstance().getNumberOfPluginLayers(); i ++){
			
			levelStrings[i] = SimPropRegistry.getInstance().getPluginsInLayer( order.get(i) );

			String key = order.get(i);
			this.pluginListsMap.put(key, new JComboBox<String>(levelStrings[i]));
			this.pluginListsMap.get(key).insertItemAt("Choose your " + key + " plugin", 0);
			this.pluginListsMap.get(key).setSelectedIndex(0);
			
			Logger.Log(LogLevel.DEBUG, "New Accordion Entry for " + key);
			accordionElement = new AccordionEntry(key, this.pluginListsMap.get(key));
			this.panel.add(accordionElement, gridBagConstraints);
		}

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
				Logger.Log(LogLevel.DEBUG, "Found component!");
			} else {
				Logger.Log(LogLevel.DEBUG, "Found component "+ component.getClass().getName() + " / " + AccordionEntry.class);
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

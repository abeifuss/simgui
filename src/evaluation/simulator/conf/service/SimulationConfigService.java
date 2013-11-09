package evaluation.simulator.conf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class SimulationConfigService {

	private SimPropRegistry simPropRegistry;

	public SimulationConfigService(SimPropRegistry simPropRegistry) {
		this.setSimPropRegistry(simPropRegistry);
	}

	public SimPropRegistry getSimPropRegistry() {
		return this.simPropRegistry;
	}

	public void loadConfig(File file) {

		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not read from file.");
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Could not read from file.");
		}

		// This value is not in range
		// Load simProps
		for (Entry<String, SimProp> s : this.simPropRegistry.getAllSimProps()) {
			Logger.Log(LogLevel.DEBUG, "Load value for " + s.getKey());
			try {
				if (s.getValue().getValueType() == String.class) {
					s.getValue().setValue((props.get(s.getKey())));
				} else if (s.getValue().getValueType() == Integer.class) {

					if (props.get(s.getKey()).equals("AUTO")) { // TODO Fix this
																// in a nice way

					} else if (props.get(s.getKey()).equals("UNLIMITED")) { // TODO Fix this
																			// in a nice way

					} else {
						s.getValue().setValue(Integer.parseInt((String) props.get(s.getKey())));
					}

				} else if (s.getValue().getValueType() == Float.class) {
					s.getValue().setValue(
							Float.parseFloat((String) props.get(s.getKey())));
				} else if (s.getValue().getValueType() == Double.class) {
					s.getValue().setValue(
							Double.parseDouble((String) props.get(s.getKey())));
				} else if (s.getValue().getValueType() == Boolean.class) {
					s.getValue()
							.setValue(
									Boolean.parseBoolean((String) props.get(s
											.getKey())));
				}
			} catch (NullPointerException e) {
				Logger.Log(LogLevel.DEBUG,
						"Can not read value for " + s.getKey());
			}
		}

		SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
		List<String> pluginLevels = simPropRegistry.getPluginLevels();

		for (String pluginLevel : pluginLevels) {
			String configName = SimPropRegistry.getInstance()
					.pluginNameToConfigName(pluginLevel);
			String selectedPlugin = (String) props.getProperty(configName);
			SimPropRegistry.getInstance().setActivePluginsMapped(configName,
					selectedPlugin);

			// Update GUI in order to inform JComboBoxes
			SimConfigPanel.getInstance().update();
		}

		DependencyChecker.checkAll(this.simPropRegistry);
		SimConfigPanel.setStatusofSaveButton(!DependencyChecker.errorsInConfig);
	}

	public void setSimPropRegistry(SimPropRegistry simPropRegistry) {
		this.simPropRegistry = simPropRegistry;
	}

	public void writeConfig(File outputFile) {

		PropertiesConfiguration props;
		try {

			props = new PropertiesConfiguration("etc/templates/experiment.cfg");

			props.setProperty("EDF_VERSION", 1);

			// static part
			Map<String, String> plugins = this.simPropRegistry
					.getActivePlugins(true);
			Logger.Log(LogLevel.DEBUG, "Active plugins are:");
			for (String key : plugins.keySet()) {
				Logger.Log(LogLevel.DEBUG, key + " with " + plugins.get(key));
				props.setProperty(key, plugins.get(key));
			}

			// dynamic part
			for (Entry<String, SimProp> s : this.simPropRegistry
					.getAllSimProps()) {
				try {
					props.setProperty(s.getKey(), s.getValue().getValue()
							.toString());
				} catch (Exception e) {
					Logger.Log(LogLevel.DEBUG, s.getKey()
							+ " has not associated property -> SKIP");
				}
			}

			props.save(outputFile);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

	}
}

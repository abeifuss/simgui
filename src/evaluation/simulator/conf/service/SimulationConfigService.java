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
import javax.swing.SwingUtilities;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.annotations.property.DoubleProp;
import evaluation.simulator.annotations.property.FloatProp;
import evaluation.simulator.annotations.property.IntProp;
import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationConfigService {
	
	private static Logger logger = Logger.getLogger(SimulationConfigService.class);

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
		
		// Properties to vary
		for (String s : this.simPropRegistry.getPropertiesToVary().keySet()) {
			logger.log(Level.DEBUG, "Load value for property to vary " + s);
			try {
				this.simPropRegistry.setPropertyToVaryValue(s, props.getProperty(s));
				logger.log(Level.DEBUG, s + " = " + props.getProperty(s));
			} catch (Exception e) {
				logger.log(Level.ERROR,"Can not read value for " + s);
			}
		}
		
		// Load simProps
		for (Entry<String, SimProp> s : this.simPropRegistry.getAllSimProps()) {
			logger.log(Level.DEBUG, "Load value for " + s.getKey());
			try {
				if (s.getValue().getValueType() == String.class) {
					s.getValue().setValue((props.get(s.getKey())));
				} else if (s.getValue().getValueType() == Integer.class) {
					if (props.get(s.getKey()).equals("AUTO")) {
						((IntProp) s.getValue() ).setAuto(true);
					} else if (props.get(s.getKey()).equals("UNLIMITED")) {
						((IntProp) s.getValue() ).setUnlimited(true);
					}else{
						((IntProp) s.getValue() ).setAuto(false);
						((IntProp) s.getValue() ).setUnlimited(false);
						s.getValue().setValue(Integer.parseInt((String) props.get(s.getKey())));
					}
				} else if (s.getValue().getValueType() == Float.class) {
					if (props.get(s.getKey()).equals("AUTO")) {
						((FloatProp) s.getValue() ).setAuto(true);
					} else if (props.get(s.getKey()).equals("UNLIMITED")) {
						((FloatProp) s.getValue() ).setUnlimited(true);
					}else{
						((FloatProp) s.getValue() ).setAuto(false);
						((FloatProp) s.getValue() ).setUnlimited(false);
						s.getValue().setValue(Float.parseFloat((String) props.get(s.getKey())));
					}
				} else if (s.getValue().getValueType() == Double.class) {
					if (props.get(s.getKey()).equals("AUTO")) {
						((DoubleProp) s.getValue() ).setAuto(true);
					} else if (props.get(s.getKey()).equals("UNLIMITED")) {
						((DoubleProp) s.getValue() ).setUnlimited(true);
					}else{
						((DoubleProp) s.getValue() ).setAuto(false);
						((DoubleProp) s.getValue() ).setUnlimited(false);
						s.getValue().setValue(Double.parseDouble((String) props.get(s.getKey())));
					}
				} else if (s.getValue().getValueType() == Boolean.class) {
					s.getValue().setValue(Boolean.parseBoolean((String) props.get(s.getKey())));
				}
				s.getValue().changed();
			} catch (NullPointerException e) {
				logger.log(Level.ERROR,"Can not read value for " + s.getKey());
			}
		}

		SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
		List<String> pluginLevels = simPropRegistry.getPluginLayers();

		for (String pluginLevel : pluginLevels) {
			String configName = SimPropRegistry.getInstance().displayNameToConfigName(pluginLevel);
			String selectedPlugin = (String) props.getProperty(configName);
			SimPropRegistry.getInstance().setActivePluginsMapped(configName,selectedPlugin);

			// Update GUI in order to inform JComboBoxes
			SimConfigPanel.getInstance().update();
		}
		this.simPropRegistry.setCurrentConfigFile(file.getAbsolutePath());
		DependencyChecker.checkAll(this.simPropRegistry);
		SimConfigPanel.setStatusofSaveButton(!DependencyChecker.errorsInConfig);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SimConfigPanel.getInstance().foldAccordions();
				SimPropRegistry.getInstance().setUnsavedChanges(false);
			}
		});
		this.simPropRegistry.setUnsavedChanges(false);
	} 

	public void setSimPropRegistry(SimPropRegistry simPropRegistry) {
		this.simPropRegistry = simPropRegistry;
	}

	public void writeConfig(File outputFile) {

		PropertiesConfiguration props;
		try {

			props = new PropertiesConfiguration("etc/conf/experiment_template.cfg");

			props.setProperty("EDF_VERSION", 1);

			// static part
			Map<String, String> plugins = this.simPropRegistry.getActivePlugins(true);
			logger.log(Level.DEBUG, "Active plugins are:");
			for (String key : plugins.keySet()) {
				logger.log(Level.DEBUG, key + " with " + plugins.get(key));
				props.setProperty(key, plugins.get(key));
			}
			
			// Properties to vary
			for (Entry<String, String> s : this.simPropRegistry.getPropertiesToVary().entrySet()) {
				// quickfix (TODO: find real problem)
				logger.log(Level.DEBUG, s.getKey() + "=" + s.getValue().toString());
//				if ( s.getKey().equals("SECOND_PROPERTY_TO_VARY") ){
//					props.setProperty(s.getKey().trim(), " " + s.getValue());
//				}else if (s.getKey().equals("VALUES_FOR_THE_SECOND_PROPERTY_TO_VARY") ) {
//					props.setProperty("VALUES_FOR_THE_SECOND_PROPERTY", " 1,2,3");
//				}else {
					props.setProperty(s.getKey(), s.getValue());
//				}
			}

			// dynamic part
			for (Entry<String, SimProp> s : this.simPropRegistry.getAllSimProps()) {
				try {
					if (s.getValue().getValueType() == Integer.class && ((IntProp)(s.getValue())).getAuto()) {
						logger.log(Level.DEBUG, s.getKey() + "=AUTO");
						props.setProperty(s.getKey(), "AUTO");
					}else if (s.getValue().getValueType() == Integer.class && ((IntProp)(s.getValue())).getUnlimited()) {
						logger.log(Level.DEBUG, s.getKey() + "=UNLIMITED");
						props.setProperty(s.getKey(), "UNLIMITED");
					}else if (s.getValue().getValueType() == Float.class && ((FloatProp)(s.getValue())).getAuto()) {
						logger.log(Level.DEBUG, s.getKey() + "=AUTO");
						props.setProperty(s.getKey(), "AUTO");
					}else if (s.getValue().getValueType() == Float.class && ((FloatProp)(s.getValue())).getUnlimited()) {
						logger.log(Level.DEBUG, s.getKey() + "=UNLIMITED");
						props.setProperty(s.getKey(), "UNLIMITED");
					}else if (s.getValue().getValueType() == Double.class && ((DoubleProp)(s.getValue())).getAuto()) {
						logger.log(Level.DEBUG, s.getKey() + "=AUTO");
						props.setProperty(s.getKey(), "AUTO");
					}else if (s.getValue().getValueType() == Double.class && ((DoubleProp)(s.getValue())).getUnlimited()) {
						logger.log(Level.DEBUG, s.getKey() + "=UNLIMITED");
						props.setProperty(s.getKey(), "UNLIMITED");
					}else{
						logger.log(Level.DEBUG, s.getKey() + "=" + s.getValue().getValue().toString());
						props.setProperty(s.getKey(), s.getValue().getValue().toString());
					}
				} catch (Exception e) {
					logger.log(Level.DEBUG, s.getKey() + " has no associated property -> SKIP");
				}
			}

			props.save(outputFile);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

	}
}

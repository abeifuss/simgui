package evaluation.simulator.conf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JOptionPane;

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
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		this.writeConfig(new File("etc/conf/config.dump." + timeStamp));
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
		for (Entry<String, SimProp> s : this.simPropRegistry.getAllSimProps()) {
			if (s.getValue().getValueType() == String.class) {
				s.getValue().setValue((props.get(s.getKey())));
			} else if (s.getValue().getValueType() == Integer.class) {
				s.getValue().setValue(
						Integer.parseInt((String) props.get(s.getKey())));
			} else if (s.getValue().getValueType() == Float.class) {
				s.getValue().setValue(
						Float.parseFloat((String) props.get(s.getKey())));
			} else if (s.getValue().getValueType() == Boolean.class) {
				s.getValue().setValue(
						Boolean.parseBoolean((String) props.get(s.getKey())));
			}
		}
		DependencyChecker.checkAll(this.simPropRegistry);
		SimConfigPanel.setStatusofSaveButton(!DependencyChecker.errorsInConfig);
	}

	public void setSimPropRegistry(SimPropRegistry simPropRegistry) {
		this.simPropRegistry = simPropRegistry;
	}

	public void writeConfig(File outputFile) {
		Properties props = new Properties();

		// static part
		Map<String, String> plugins = this.simPropRegistry.getActivePlugins();
		Logger.Log( LogLevel.DEBUG , "Active Plugins: ");
		for (String key : plugins.keySet()) {
			Logger.Log( LogLevel.DEBUG , key + " : " +plugins.get(key));
			props.setProperty(key, plugins.get(key));
		}

		// dynamic part
		for (Entry<String, SimProp> s : this.simPropRegistry.getAllSimProps()) {
			props.setProperty(s.getKey(), s.getValue().getValue().toString());
		}
		try {
			props.store(new FileOutputStream(outputFile), null);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not write to file.");
		}
	}
}

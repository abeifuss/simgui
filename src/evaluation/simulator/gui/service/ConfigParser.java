package evaluation.simulator.gui.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;

import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class ConfigParser {

	public void clenupConfigurationForSimulator(File file) {
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;

		Map<String, String> configNameToPluginName = new HashMap<>();
		configNameToPluginName.put("TYPE_OF_DELAY_BOX", "delayBox");
		configNameToPluginName
				.put("TYPE_OF_TRAFFIC_GENERATOR", "trafficSource");
		configNameToPluginName.put("PLOT_TYPE", "plotType");
		configNameToPluginName.put("OUTPUT_STRATEGY", "outputStrategy");
		configNameToPluginName.put("TOPOLOGY_SCRIPT", "topology");
		configNameToPluginName.put("MIX_SEND_STYLE", "mixSendStyle");
		configNameToPluginName.put("CLIENT_SEND_STYLE", "clientSendStyle");

		// Get plugin-names
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		
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

		// Load simProps
		List<String> activePlugins = new LinkedList<String>();
		List<String> generalProperties = new LinkedList<String>();
		
		for (String s : configNameToPluginName.keySet() ) {
				String tmp = props.getProperty(s);
				Logger.Log(LogLevel.DEBUG, "Found: " + s + "=" + tmp);
				activePlugins.add(tmp);
				generalProperties.add(s);
		}

		try {
			reader = new BufferedReader(new FileReader(file));
			String string = null;
			String[] subStrings = { "" };
			// Filter relevant lines in configuration file
			while ((string = reader.readLine()) != null) {
				
				for (String plugin : generalProperties) {
					if (string.matches("^" + plugin + "=\\w*")) {
						content.append(string).append(System.getProperty("line.separator"));
					}
				}
				
				for (String plugin : activePlugins) {
					if (string.matches("^" + plugin + "\\\\:\\\\:\\w*=\\w*")) {
						subStrings = string.split("\\\\:\\\\:");
						content.append(subStrings[1]).append(System.getProperty("line.separator"));
					}
				}
			}
			// Logger.Log(LogLevel.DEBUG, "Parsed config:" + "\n" + content.toString());

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}

		System.out.println(content.toString());

	}
}

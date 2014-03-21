package evaluation.simulator.gui.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import evaluation.simulator.conf.service.SimulationConfigService;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * @author alex
 *
 */
public class ConfigParser {
	
	private static Logger logger = Logger.getLogger(ConfigParser.class);

	
	/**
	 * Read config file content into a string 
	 * 
	 * @param file
	 * @return
	 * 		a string with config file's content
	 */
	public String cleanupConfigurationForSimulator(File file) {
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;
		
		List<String> configNamesForPluginLayers = SimPropRegistry.getInstance().getConfigNamesForPluginLayers();
		
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
		Set<String> generalProperties = new HashSet<String>();
		
		for (String s : configNamesForPluginLayers ) {
				String tmp = props.getProperty(s);
				logger.log(Level.DEBUG, "Set " + s + " to " + tmp);
				activePlugins.add(tmp);
				generalProperties.add(s);
		}

		try {
			reader = new BufferedReader(new FileReader(file));
			String string = null;
			
			@SuppressWarnings("unused")
			String[] subStrings = { "" };
			
			// Filter relevant lines in configuration file
			while ((string = reader.readLine()) != null) {
				
				// filter comments
				if ( !string.matches("^#.*") ){
					content.append(string.replace(" = ", "=")).append(System.getProperty("line.separator"));
					// content.append(string).append(System.getProperty("line.separator"));
					continue;
				}
			}

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

		return content.toString();

	}
}

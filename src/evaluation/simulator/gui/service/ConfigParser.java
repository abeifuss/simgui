package evaluation.simulator.gui.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConfigParser {

	public void clenupConfigurationForSimulator(File file) {
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;

		// Get plugin-names
		
		List<String> pluginNames = new LinkedList<String>();
		pluginNames.add("CONSTANT");
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String s = null;
			String[] x = {""};
			// Filter relevant lines in configuration file
			while ((s = reader.readLine()) != null) {
				for ( String plugin : pluginNames ){
					if ( s.matches("^"+plugin+"\\\\:\\\\:\\w*=\\w") ){
						x = s.split("\\\\:\\\\:");
						System.err.println(x[1]);
						content.append(s).append(System.getProperty("line.separator"));
						
						// Remove namespaces
					}
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
		
		System.out.println(content.toString());

	}
}

package evaluation.simulator.gui.helper;

import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;

import evaluation.simulator.gui.results.GnuplotPanel;

/**
 * Cleans the output folder
 * 
 * @author nachkonvention
 * 
 */
public class IOActions {
	/**
	 * @throws IOException
	 */
	public static void cleanOutputFolder() throws IOException {
		FileUtils.cleanDirectory(GnuplotPanel.outputFolder);
	}
}

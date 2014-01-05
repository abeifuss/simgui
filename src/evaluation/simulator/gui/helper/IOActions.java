package evaluation.simulator.gui.helper;

import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;

import evaluation.simulator.gui.results.GnuplotPanel;

public class IOActions {
	public static void cleanOutputFolder() throws IOException {
		FileUtils.cleanDirectory(GnuplotPanel.outputFolder);
	}
}

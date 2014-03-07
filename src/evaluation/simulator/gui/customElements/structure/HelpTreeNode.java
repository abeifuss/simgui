package evaluation.simulator.gui.customElements.structure;

import org.apache.log4j.Logger;
import evaluation.simulator.gui.launcher.GuiLauncher;

public class HelpTreeNode {
	private String helpTreeNodeName;
	private String helpTreeNodeURL;
	
	private static Logger logger = Logger.getLogger(GuiLauncher.class);

	public HelpTreeNode(String string, String filename) {
		setHelpTreeNodeName(string);
		setHelpTreeNodeURL(filename);
		if (getHelpTreeNodeURL() == null) {
			logger.error("Couldn't find file: " + filename);
		}
	}

	public String toString() {
		return getHelpTreeNodeName();
	}

	public String getHelpTreeNodeName() {
		return helpTreeNodeName;
	}

	public void setHelpTreeNodeName(String helpTreeNodeName) {
		this.helpTreeNodeName = helpTreeNodeName;
	}

	public String getHelpTreeNodeURL() {
		return helpTreeNodeURL;
	}

	public void setHelpTreeNodeURL(String helpTreeNodeURL) {
		this.helpTreeNodeURL = helpTreeNodeURL;
	}
}

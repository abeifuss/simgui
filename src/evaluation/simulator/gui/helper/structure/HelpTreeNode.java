package evaluation.simulator.gui.helper.structure;

public class HelpTreeNode {
	private String helpTreeNodeName;
	private String helpTreeNodeURL;

	public HelpTreeNode(String string, String filename) {
		setHelpTreeNodeName(string);
		setHelpTreeNodeURL(filename);
		if (getHelpTreeNodeURL() == null) {
			System.err.println("Couldn't find file: " + filename);
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

package framework.core.launcher;

import edu.umd.cs.findbugs.util.NotImplementedYetException;
import evaluation.localTest.LocalTest;
import evaluation.simulator.Simulator;
import framework.core.AnonNode;
import framework.core.gui.model.XMLResource;

public enum ToolName {

	NOT_SET(new String[] {}), LOCAL_TEST(new String[] { "localTest", "1" }), INFO_SERVICE(new String[] { "infoService",
			"2" }), MIX(new String[] { "mix", "3" }), CLIENT(new String[] { "client", "4" }), P2P(new String[] { "p2p",
			"mixAndClient", "5" }), LOAD_GENERATOR(new String[] { "loadGenerator", "6" }), SIMULATOR(new String[] {
			"simulator", "7" });

	public String[] identifiers;

	private ToolName(String[] identifiers) {
		this.identifiers = identifiers;
	}

	public void execute(XMLResource resource) {
		switch (this) {
		case NOT_SET:
			throw new RuntimeException("cannot execute the tool, as \"ToolName\" is not set!");
		case MIX:
			new AnonNode(resource, this);
			break;
		case CLIENT:
			new AnonNode(resource, this);
			break;
		case P2P:
			new AnonNode(resource, this);
			break;
		case SIMULATOR:
			new Simulator(/*resource*/);
			break;
		case LOAD_GENERATOR:
			throw new NotImplementedYetException("");
//			new LoadGenerator(resource);
//			break;
		case LOCAL_TEST:
			new LocalTest(resource);
			break;
		case INFO_SERVICE:
			throw new NotImplementedYetException("");
//			new InfoServiceServer(resource);
//			break;
		default:
			throw new RuntimeException("unknown tool; add an entry in framework.core.launcher.ToolName.java");
		}

	}

	public static ToolName getToolByIdentifier(String identifier) {
		for (ToolName tool : ToolName.values())
			for (String ident : tool.identifiers)
				if (ident.equalsIgnoreCase(identifier) || ident.equalsIgnoreCase("-" + identifier))
					return tool;
		return ToolName.NOT_SET;
	}

}

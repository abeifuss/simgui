package evaluation.simulator.core.binding;

import evaluation.simulator.Simulator;
import framework.core.launcher.CommandLineParameters;

public class gMixBinding {

	public gMixBinding(String[] configFile) {
		this.init(configFile);
	}

	private void init(String[] configFile) {
		CommandLineParameters params = new CommandLineParameters(configFile);

		@SuppressWarnings("unused")
		Simulator gMixSim = new Simulator(params);
	}

}

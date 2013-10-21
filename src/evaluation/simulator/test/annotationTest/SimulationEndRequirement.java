package evaluation.simulator.test.annotationTest;

import evaluation.simulator.annotations.Requirement;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationEndRequirement extends Requirement {

	@Override
	public boolean check() {

		SimPropRegistry gcr = SimPropRegistry.getInstance();
		boolean enableState = true;

		System.err.println(this.equals("CFG_INT_1", "3"));
		enableState &= this.equals("CFG_INT_1", "3");

		if (!enableState) {
			gcr.getValue("CFG_BOOL1").setEnable(false);
			return enableState;
		}

		gcr.getValue("CFG_BOOL1").setEnable(true);
		return enableState;
	}

}

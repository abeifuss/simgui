package evaluation.simulator.test.annotationTest;

import evaluation.simulator.annotations.simulationProperty.Requirement;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationEndRequirement extends Requirement {

	@Override
	public boolean check() {

		SimPropRegistry gcr = SimPropRegistry.getInstance();
		boolean enableState = true;

		System.err.println(this.equals("CR::INT", "3"));
		enableState &= this.equals("CR::INT", "3");

		if (!enableState) {
			gcr.getValue("CR::BOOL").setEnable(false);
			return enableState;
		}

		gcr.getValue("CR::BOOL").setEnable(true);
		return enableState;
	}

}

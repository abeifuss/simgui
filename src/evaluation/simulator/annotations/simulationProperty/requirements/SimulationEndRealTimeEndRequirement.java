package evaluation.simulator.annotations.simulationProperty.requirements;

import evaluation.simulator.annotations.simulationProperty.Requirement;
import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationEndRealTimeEndRequirement extends Requirement {
	
	
	public boolean check() {

		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getProperties().get("REAL_TIME_LIMIT_IN_SEC");
		
		if (equals("SIMULATION_END", "REAL_TIME_END")){
			simProp.setEnable(true);
			System.out.println("Enabled Real Time");
			return true;
		} else {
			simProp.setEnable(false);
			System.out.println("Disabled Real Time");
			return false;
		}
	}
}

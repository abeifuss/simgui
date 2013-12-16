package evaluation.simulator.annotations.simulationProperty.requirements;

import evaluation.simulator.annotations.simulationProperty.Requirement;
import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationEndSimulationTimeEndRequirement extends Requirement {
	
	
	public boolean check() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getProperties().get("SIMULATION_TIME_LIMIT_IN_MS");
		
		if (equals("SIMULATION_END", "SIMULATION_TIME_END")){
			simProp.setEnable(true);
			System.out.println("Enabled Simulation Time");
			return true;
		} else {
			simProp.setEnable(false);
			System.out.println("Disabled Simulation Time");
			return false;
		}
	}
}

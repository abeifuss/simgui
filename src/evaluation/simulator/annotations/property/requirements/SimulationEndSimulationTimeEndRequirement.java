package evaluation.simulator.annotations.property.requirements;

import java.util.HashSet;
import java.util.Set;

import evaluation.simulator.annotations.property.Requirement;
import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationEndSimulationTimeEndRequirement extends Requirement {
	
	
	public boolean check() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getProperties().get("SIMULATION_TIME_LIMIT_IN_MS");
		String msg = "Requires \"Simulation end condition\" to be set to \"SIMULATION_TIME_END\"";
		
		// create warnings if not existent
		if( simProp.getWarnings() == null ){
			simProp.setWarnings( new HashSet<String>() );
		}
		
		if (equals("SIMULATION_END", "SIMULATION_TIME_END")){
			Set<String> warnings = simProp.getWarnings();
			warnings.remove(msg);
			simProp.setWarnings(warnings);
			simProp.setEnable(true);
			return true;
		} else {
			Set<String> warnings = simProp.getWarnings();
			warnings.add(msg);
			simProp.setWarnings(warnings);
			simProp.setEnable(false);
			return false;
		}
	}
}

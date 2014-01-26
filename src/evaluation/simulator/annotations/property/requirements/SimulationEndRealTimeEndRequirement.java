package evaluation.simulator.annotations.property.requirements;

import java.util.HashSet;
import java.util.Set;

import evaluation.simulator.annotations.property.Requirement;
import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SimulationEndRealTimeEndRequirement extends Requirement {
	
	
	public boolean check() {

		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getProperties().get("REAL_TIME_LIMIT_IN_SEC");
		String msg = "Requires \"Simulation end condition\" to be set to \"REAL_TIME_END\"";

		// create warnings if not existent
		if( simProp.getWarnings() == null ){
			System.err.println("NEW WARNINGS");
			simProp.setWarnings( new HashSet<String>() );
		}
		
		if (equals("SIMULATION_END", "REAL_TIME_END")){
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

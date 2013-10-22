package test.annotationTest;

import gui.pluginRegistry.SimPropRegistry;
import annotations.simulationProperty.Requirement;

public class SimulationEndRequirement extends Requirement {

	@Override
	public
	boolean check() {
		
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		boolean enableState = true;
		
		System.err.println( equals("CR::CFG_INT_1", "3") );
		enableState &= equals("CR::CFG_INT_1", "3");
		
		if ( !enableState ){
			gcr.getValue("CR::CFG_BOOL_1").setEnable(false);
			return enableState;
		}
		
		gcr.getValue("CR::CFG_BOOL_1").setEnable(true);
		return enableState;
	}

}

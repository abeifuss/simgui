package test.annotationTest;

import gui.pluginRegistry.SimPropRegistry;
import annotations.Requirement;

public class SimulationEndRequirement extends Requirement {

	@Override
	public
	boolean check() {
		
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		boolean enableState = true;
		
		System.err.println( equals("CFG_INT_1", "3") );
		enableState &= equals("CFG_INT_1", "3");
		
		if ( !enableState ){
			gcr.getValue("CFG_BOOL1").setEnable(false);
			return enableState;
		}
		
		gcr.getValue("CFG_BOOL1").setEnable(true);
		return enableState;
	}

}

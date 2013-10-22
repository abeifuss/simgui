package test.annotationTest;

import gui.pluginRegistry.SimPropRegistry;
import annotations.simulationProperty.Requirement;

public class SimulationEndRequirement extends Requirement {

	@Override
	public
	boolean check() {
		
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		boolean enableState = true;
		
		System.err.println( equals("CR::INT", "3") );
		enableState &= equals("CR::INT", "3");
		
		if ( !enableState ){
			gcr.getValue("CR::BOOL").setEnable(false);
			return enableState;
		}
		
		gcr.getValue("CR::BOOL").setEnable(true);
		return enableState;
	}

}

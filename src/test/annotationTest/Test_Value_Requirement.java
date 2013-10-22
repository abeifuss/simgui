package test.annotationTest;

import annotations.simulationProperty.Requirement;

public class Test_Value_Requirement extends Requirement {

	@Override
	public
	boolean check() {

		// Min und Max Setter
		setIntegerMaxValueOfOption("CR::CFG_INT_MIN", getIntegerValueOfOption("CR::CFG_INT_MAX"));
		setIntegerMaxValueOfOption("CR::CFG_INT_AVG", getIntegerValueOfOption("CR::CFG_INT_MAX"));
		
		
		setIntegerMinValueOfOption("CR::CFG_INT_MAX", getIntegerValueOfOption("CR::CFG_INT_MIN"));
		setIntegerMinValueOfOption("CR::CFG_INT_AVG", getIntegerValueOfOption("CR::CFG_INT_MIN"));
		
		return true;
	}

}
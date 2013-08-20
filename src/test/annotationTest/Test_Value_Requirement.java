package test.annotationTest;

import annotations.Requirement;

public class Test_Value_Requirement extends Requirement {

	@Override
	public
	boolean check() {

		// Min und Max Setter
		setIntegerMaxValueOfOption("CFG_INT_MIN", getIntegerValueOfOption("CFG_INT_MAX"));
		setIntegerMaxValueOfOption("CFG_INT_AVG", getIntegerValueOfOption("CFG_INT_MAX"));
		
		
		setIntegerMinValueOfOption("CFG_INT_MAX", getIntegerValueOfOption("CFG_INT_MIN"));
		setIntegerMinValueOfOption("CFG_INT_AVG", getIntegerValueOfOption("CFG_INT_MIN"));
		
		return true;
	}

}
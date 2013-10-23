package evaluation.simulator.test.annotationTest;

import evaluation.simulator.annotations.simulationProperty.Requirement;

public class Test_Value_Requirement extends Requirement {

	@Override
	public boolean check() {

		// Min und Max Setter
		setIntegerMaxValueOfOption("CR::INT_MIN",
				getIntegerValueOfOption("CR::INT_MAX"));
		setIntegerMaxValueOfOption("CR::INT_AVG",
				getIntegerValueOfOption("CR::INT_MAX"));

		setIntegerMinValueOfOption("CR::INT_MAX",
				getIntegerValueOfOption("CR::INT_MIN"));
		setIntegerMinValueOfOption("CR::INT_AVG",
				getIntegerValueOfOption("CR::INT_MIN"));

		return true;
	}

}
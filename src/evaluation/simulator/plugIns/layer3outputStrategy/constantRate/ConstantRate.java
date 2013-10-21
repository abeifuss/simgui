package evaluation.simulator.plugIns.layer3outputStrategy.constantRate;

import evaluation.simulator.annotations.BoolSimulationProperty;
import evaluation.simulator.annotations.FloatSimulationProperty;
import evaluation.simulator.annotations.IntSimulationProperty;
import evaluation.simulator.annotations.StringSimulationProperty;
import evaluation.simulator.test.annotationTest.SimulationEndRequirement;
import evaluation.simulator.test.annotationTest.Test_Value_Requirement;

public class ConstantRate {

	@BoolSimulationProperty(name = "A Bool", description = "Some text about Integers (int1)", tooltip = "1st integer's tooltip", valueType = Integer.class, value = true, enable_requirements = { SimulationEndRequirement.class }, category = "TEST")
	int CFG_BOOL1;

	@FloatSimulationProperty(name = "A Float", description = "Some text about Integers (int1)", tooltip = "1st integer's tooltip", valueType = Integer.class, min = 0.0f, max = 10.0f, value = 2.23f, category = "TEST")
	int CFG_FLOAT1;

	@IntSimulationProperty(name = "INT1", description = "Some text about Integers (int2)", tooltip = "2integer's tooltip", valueType = Integer.class, min = 0, max = 100, value = 100, category = "TEST")
	int CFG_INT_1;

	@IntSimulationProperty(name = "AVG INT", description = "Some text about Integers (int2)", tooltip = "2integer's tooltip", valueType = Integer.class, min = 0, max = 100, value = 50, category = "TEST", value_requirements = Test_Value_Requirement.class)
	int CFG_INT_AVG;

	@IntSimulationProperty(name = "MAX INT", description = "Some text about Integers (int2)", tooltip = "2integer's tooltip", valueType = Integer.class, min = 0, max = 100, value = 100, category = "TEST")
	int CFG_INT_MAX;

	@IntSimulationProperty(name = "MIN INT", description = "Some text about Integers (int2)", tooltip = "2integer's tooltip", valueType = Integer.class, min = 0, max = 100, value = 0, category = "TEST")
	int CFG_INT_MIN;

	@StringSimulationProperty(name = "A String", description = "Some text about Integers (int1)", tooltip = "1st integer's tooltip", valueType = Integer.class, value = "String", category = "TEST")
	int CFG_STING1;

}

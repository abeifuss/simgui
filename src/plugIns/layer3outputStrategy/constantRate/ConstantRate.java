package plugIns.layer3outputStrategy.constantRate;

import test.annotationTest.SimulationEndRequirement;
import annotations.BoolSimulationProperty;
import annotations.FloatSimulationProperty;
import test.annotationTest.Test_Value_Requirement;
import annotations.IntSimulationProperty;
import annotations.StringSimulationProperty;

public class ConstantRate {

	@IntSimulationProperty(name = "INT1", 
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 100,
			category = "TEST")
	int CFG_INT_1;
	
	@IntSimulationProperty(name = "MAX INT", 
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 100,
			category = "TEST")
	int CFG_INT_MAX;
	
	@IntSimulationProperty(name = "MIN INT", 
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 0,
			category = "TEST")
	int CFG_INT_MIN;
	
	@IntSimulationProperty(name = "AVG INT", 
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 50,
			category = "TEST",
			value_requirements = Test_Value_Requirement.class)
	int CFG_INT_AVG;
	
	
	
	@FloatSimulationProperty(name = "A Float", 
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = Float.class,
			min = 0.0f, 
			max = 10.0f, 
			value = 2.23f,
			category = "TEST")
	int CFG_FLOAT1;
	
	@BoolSimulationProperty(name = "A Bool", 
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = Boolean.class,
			value = true,
			enable_requirements = {SimulationEndRequirement.class},
			category = "TEST")
	int CFG_BOOL1;
	
	@StringSimulationProperty(name = "A String", 
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = String.class,
			value = "Predef",
			possibleValues = "valOne, valTwo",
			category = "TEST")
	int CFG_STRING1;
	
	@StringSimulationProperty(name = "A String", 
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = String.class,
			value = "String",
			category = "TEST")
	int CFG_STRING2;
}

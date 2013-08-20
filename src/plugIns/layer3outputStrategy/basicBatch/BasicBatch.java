package plugIns.layer3outputStrategy.basicBatch;

import test.annotationTest.Test_Value_Requirement;
import annotations.IntSimulationProperty;

public class BasicBatch {

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
	
//	@BoolSimulationProperty(name = "Another Integer", 
//			description = "Some other text about Integers (int2)", 
//			tooltip = "2nd integer's tooltip", 
//			value = false,
//			value_requirements = {SimulationEndRequirement.class},
//			category = "TEST")
//	int CFG_INT2;
	
//	@IntSimulationProperty(name = "Overwrite", 
//			description = "Some other text about Integers (int1)", 
//			tooltip = "empty", 
//			value = 1337,
//			min=0,
//			max=10,
//			category = "TEST2")
//	int CFG_INT1;
	
}

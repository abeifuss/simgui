package plugIns.outputStrategy;

import test.annotationTest.Test_Value_Requirement;
import annotations.plugin.PluginAnnotation;
import annotations.simulationProperty.IntSimulationProperty;

@PluginAnnotation(name="BB", documentationURL="BBPlugin.html")
public class BasicBatch {

	@IntSimulationProperty(name = "Packet Size (byte)",
			propertykey = "PACKET_SIZE",
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 50,
			order = 1,
			value_requirements = Test_Value_Requirement.class)
	int packetSize;
	
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

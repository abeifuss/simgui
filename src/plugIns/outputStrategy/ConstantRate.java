package plugIns.outputStrategy;

import test.annotationTest.SimulationEndRequirement;
import test.annotationTest.Test_Value_Requirement;
import annotations.plugin.PluginAnnotation;
import annotations.simulationProperty.BoolSimulationProperty;
import annotations.simulationProperty.FloatSimulationProperty;
import annotations.simulationProperty.IntSimulationProperty;
import annotations.simulationProperty.StringSimulationProperty;

@PluginAnnotation(name="CR", documentationURL="CRPlugin.html")
public class ConstantRate {

	@IntSimulationProperty(name = "A Number",
			propertykey = "INT",
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 100,
			order = 1)
	int CFG_INT_1;
	
	@IntSimulationProperty(name = "Maximum",
			propertykey = "INT_MAX",
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 100,
			order = 3)
	int CFG_INT_MAX;
	
	@IntSimulationProperty(name = "Minimum",
			propertykey = "INT_MIN",
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 0,
			order = 2)
	int CFG_INT_MIN;
	
	@IntSimulationProperty(name = "Average",
			propertykey = "INT_AVG",
			description = "Some text about Integers (int2)", 
			tooltip = "2integer's tooltip",
			valueType = Integer.class,
			min = 0, 
			max = 100, 
			value = 50,
			order = 4,
			value_requirements = Test_Value_Requirement.class)
	int CFG_INT_AVG;
	
	
	
	@FloatSimulationProperty(name = "A Float",
			propertykey = "FLOAT",
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = Float.class,
			min = 0.0f, 
			max = 10.0f, 
			value = 2.23f,
			order = 5)
	int CFG_FLOAT1;
	
	@BoolSimulationProperty(name = "A Bool",
			propertykey = "BOOL",
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = Boolean.class,
			value = true,
			enable_requirements = {SimulationEndRequirement.class},
			order = 6)
	int CFG_BOOL_1;
	
	@StringSimulationProperty(name = "A String",
			propertykey = "STRING_1",
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = String.class,
			value = "Predef",
			possibleValues = "valOne, valTwo",
			order = 7)
	int CFG_STRING_1;
	
	@StringSimulationProperty(name = "A second String",
			propertykey = "STRING_2",
			description = "Some text about Integers (int1)", 
			tooltip = "1st integer's tooltip",
			valueType = String.class,
			value = "String",
			order = 8)
	int CFG_STRING_2;
}

package test.annotationTest;

//import annotations.SimulationProperty;

public class TestClassNoExtend {

//	// Participants
//	@SimulationProperty(name = "An Integer", 
//			description = "Some text about Integers (int1)", 
//			tooltip = "1st integer's tooltip",
//			valueType = Integer.class,
////			minValue = 0, 
////			maxValue = 255, 
////			boolValue = true,
//			category = "TEST")
//	int CFG_INT1;
//	
//	@SimulationProperty(name = "Another Integer", 
//			description = "Some other text about Integers (int2)", 
//			tooltip = "2nd integer's tooltip", 
////			minValue = 0, 
////			maxValue = 2048, 
////			intValue = 815,
//			value_requirements = {SimulationEndRequirement.class},
//			category = "TEST")
//	int CFG_INT2;
//
//	@SimulationProperty(name= "A Bool",
//			valueType = Boolean.class,
//			category="TEST")
//	boolean CFG_BOOL1;
//	
//	@SimulationProperty(name = "A Bool", 
//			description = "Some text about Boolean (bool2)", 
//			tooltip = "This is a boolean", 
//			valueType = String.class, 
////			minValue = 0, 
////			maxValue = 2048, 
//			category = "TEST")
//	boolean CFG_BOOL2;
//	
//	@SimulationProperty(name = "A String", 
//			valueType = String.class,
////			strValue = "Just a string",
//			category = "TEST")
//	boolean CFG_STRING;
//	
//	// EXPERIMENT
//	@SimulationProperty(name= "Evaluations",
//			valueType = String.class,
////			strValue = "",
//			category="Experiment")
//	String DESIRED_EVALUATIONS;
//	
//	@SimulationProperty(name= "Start at",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Experiment")
//	int START_RECORDING_STATISTICS_AT;
//	
//	@SimulationProperty(name= "Property to vary",
//			valueType = String.class,
////			strValue = "",
//			category="Experiment")
//	String PROPERTY_TO_VARY;
//	
//	@SimulationProperty(name= "Propery values",
//			valueType = String.class,
////			strValue = "",
//			category="Experiment")
//	String VALUES_FOR_THE_PROPERTY_TO_VARY;
//	
//	@SimulationProperty(name = "Use second property", 
//			valueType = Boolean.class,
////			boolValue = false,
//			category = "Experiment")
//	boolean USE_SECOND_PROPERTY_TO_VARY;
//	
//	@SimulationProperty(name= "2nd property to vary",
//			valueType = String.class,
////			strValue = "",
//			category="Experiment")
//	String SECOND_PROPERTY_TO_VARY;
//	
//	@SimulationProperty(name= "2nd property values",
//			valueType = String.class,
////			strValue = "",
//			category="Experiment")
//	String VALUES_FOR_THE_SECOND_PROPERTY_TO_VARY;
//	
//	//SIMULATION
//	
//	@SimulationProperty(name= "Runs",
//			valueType = Integer.class,
//			intValue = 1,
//			category="Simulation")
//	int VALIDATION_RUNS;
//	
//	@SimulationProperty(name= "Evaluations",
//			valueType = String.class,
////			strValue = "REAL_TIME_END",
//			category="Simulation")
//	String SIMULATION_END;
//	
//	@SimulationProperty(name= "Duration",
//			valueType = Integer.class,
////			intValue = 100,
//			category="Experiment")
//	int REAL_TIME_LIMIT_IN_SEC;
//	
//	@SimulationProperty(name= "Duration",
//			valueType = Integer.class,
////			intValue = 1000,
//			category="Experiment")
//	int SIMULATION_TIME_LIMIT_IN_MS;
//	
//	//EVALUATION
//	
//	@SimulationProperty(name = "Calculate Avg.", 
//			valueType = Boolean.class,
////			boolValue = true,
//			category = "Evaluation")
//	boolean CALC_AVG_OF_RUNS;
//	
//	@SimulationProperty(name= "Plot script",
//			valueType = String.class,
////			strValue = "eval.plt",
//			category="Evaluation")
//	String NAME_OF_PLOT_SCRIPT;
//	
//	@SimulationProperty(name= "Overwrite parameters",
//			valueType = String.class,
////			strValue = "",
//			category="Evaluation")
//	String OVERWRITABLE_PARAMETERS;
//	
//	@SimulationProperty(name= "Permit overwriting for",
//			valueType = String.class,
////			strValue = "set logscale x",
//			category="Evaluation")
//	String NONE_OVERWRITABLE_PARAMETERS;
//	
//	@SimulationProperty(name = "Inverse", 
//			valueType = Boolean.class,
////			boolValue = false,
//			category = "Evaluation")
//	boolean IS_INVERSE;
//	
//	//TOPOLOGY
//	@SimulationProperty(name= "Topology",
//			valueType = String.class,
////			strValue = "THREE_MIX_CASCADE",
//			category="Topology")
//	String TOPOLOGY_SCRIPT;
//	
//	@SimulationProperty(name= "Type of delay box",
//			valueType = String.class,
////			strValue = "BASIC_DELAY_BOX",
//			category="Topology")
//	String TYPE_OF_DELAY_BOX;
//	
//	@SimulationProperty(name= "Client bandwidth send",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_CLIENT_BANDWIDTH_SEND;
//	
//	@SimulationProperty(name= "Client bandwidth receive",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_CLIENT_BANDWIDTH_RECEIVE;
//	
//	@SimulationProperty(name= "Client latency",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_CLIENT_LATENCY;
//	
//	@SimulationProperty(name= "Mix bandwidth send",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_MIX_BANDWIDTH_SEND;
//	
//	@SimulationProperty(name= "Mix bandwidth receive",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_MIX_BANDWIDTH_RECEIVE;
//	
//	@SimulationProperty(name= "Mix latency",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_MIX_LATENCY;
//	
//	@SimulationProperty(name= "Proxy bandwidth send",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_DISTANT_PROXY_BANDWIDTH_SEND;
//	
//	@SimulationProperty(name= "Proxy bandwidth receive",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_DISTANT_PROXY_BANDWIDTH_RECEIVE;
//	
//	@SimulationProperty(name= "Proxy latency",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Topology")
//	int BASIC_DELAY_BOX_DEFAULT_DISTANT_PROXY_LATENCY;
//	
//	//LOAD
//	
//	@SimulationProperty(name= "Type of traffic source",
//			valueType = String.class,
////			strValue = "POISSON",
//			category="Load")
//	String TYPE_OF_TRAFFIC_GENERATOR;
//	
//	@SimulationProperty(name= "Number of Clients",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Load")
//	int NUMBER_OF_CLIENTS_TO_SIMULATE;
//	
//	@SimulationProperty(name= "Avg. Requests per second",
//			valueType = Integer.class,
////			intValue = 1,
//			category="Load")
//	int AVERAGE_REQUESTS_PER_SECOND_AND_CLIENT;
//	
//	@SimulationProperty(name= "Request Size",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Load")
//	int REQUEST_SIZE;
//	
//	@SimulationProperty(name= "Reply size",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Load")
//	int REPLY_SIZE;
//	
//	@SimulationProperty(name= "Resolve time",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Load")
//	int RESOLVE_TIME;
//	
//	@SimulationProperty(name= "Alpha",
//			valueType = Integer.class,
////			intValue = 1,
//			category="Load")
//	int PARETO_ALPHA;
//	
//	@SimulationProperty(name= "Trace file",
//			valueType = String.class,
////			strValue = "",
//			category="Load")
//	String PATH_TO_TRACE;
//	
//	@SimulationProperty(name = "Limit clients", 
//			valueType = Boolean.class,
////			boolValue = false,
//			category = "Load")
//	boolean LIMIT_CLIENT_NUMBER;
//	
//	@SimulationProperty(name= "Max. Clients",
//			valueType = Integer.class,
////			intValue = 123,
//			category="Load")
//	int CLIENT_LIMIT;
//	
//	@SimulationProperty(name = "Choose random clients", 
//			valueType = Boolean.class,
////			boolValue = true,
//			category = "Load")
//	boolean CHOOSE_RANDOM_CLIENTS;
//	
//	@SimulationProperty(name= "Communication mode",
//			valueType = String.class,
////			strValue = "SIMPLEX",
//			category="Load")
//	String COMMUNICATION_MODE;
//	
//	//MIX STRATEGY
//	
//	@SimulationProperty(name= "Output strategy",
//			valueType = String.class,
////			strValue = "COTTRELL_RANDOM_DELAY",
//			category="Mix Strategy")
//	String OUTPUT_STRATEGY;
//	
//	@SimulationProperty(name= "Batch size",
//			valueType = Integer.class,
////			intValue = 100,
//			category="Mix Strategy")
//	int BATCH_SIZE;
//	
//	@SimulationProperty(name= "Pool size",
//			valueType = Integer.class,
////			intValue = 50,
//			category="Mix Strategy")
//	int POOL_SIZE;
//	
//	@SimulationProperty(name= "Reply delays I",
//			valueType = Integer.class,
////			intValue = 10,
//			category="Mix Strategy")
//	int MAX_REPLY_DELAY_DISTINCT_USER_BATCH;
//	
//	@SimulationProperty(name= "Request delay",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Mix Strategy")
//	int MAX_DLPA_REQUEST_DELAY;
//	
//	@SimulationProperty(name= "Reply delay II",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Mix Strategy")
//	int MAX_DLPA_REPLY_DELAY;
//	
//	@SimulationProperty(name = "Dummy traffic", 
//			valueType = Boolean.class,
////			boolValue = false,
//			category = "Mix Strategy")
//	boolean RECEIVER_SUPPORTS_DUMMY_TRAFFIC;
//	
//	//MESSAGES
//	
//	@SimulationProperty(name= "Message format",
//			valueType = String.class,
////			strValue = "BASIC_MIX_MESSAGE",
//			category="Messages")
//	String MESSAGE_FORMAT;
//	
//	@SimulationProperty(name= "Packet payload",
//			valueType = Integer.class,
////			intValue = 512,
//			category="Messages")
//	int NETWORK_PACKET_PAYLOAD_SIZE;
//	
//	@SimulationProperty(name= "Request payload",
//			valueType = Integer.class,
////			intValue = 512,
//			category="Messages")
//	int MIX_REQUEST_PAYLOAD_SIZE;
//	
//	@SimulationProperty(name= "Request header size",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Messages")
//	int MIX_REQUEST_HEADER_SIZE;
//	
//	@SimulationProperty(name= "Reply payload",
//			valueType = Integer.class,
////			intValue = 512,
//			category="Messages")
//	int MIX_REPLY_PAYLOAD_SIZE;
//	
//	@SimulationProperty(name= "Reply header size",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Messages")
//	int MIX_REPLY_HEADER_SIZE;
//	
//	@SimulationProperty(name= "Request creation time",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Messages")
//	int MIX_REQUEST_CREATION_TIME;
//	
//	@SimulationProperty(name= "Request decryption time",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Messages")
//	int MIX_REPLY_DECRYPTION_TIME;
//	
//	@SimulationProperty(name= "Processing time requests",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Messages")
//	int PROCESSING_TIME_FOR_1000_REQUESTS;
//	
//	@SimulationProperty(name= "Processing time replies",
//			valueType = Integer.class,
////			intValue = 0,
//			category="Messages")
//	int PROCESSING_TIME_FOR_1000_REPLIES;
//	
//	@SimulationProperty(name= "Client send style",
//			valueType = String.class,
////			strValue = "SEND_IMMEDIATELY",
//			category="Messages")
//	String CLIENT_SEND_STYLE;
//	
//	@SimulationProperty(name= "Mix send style",
//			valueType = String.class,
////			strValue = "WAIT_FOR_FURTHER_DATA_BEFORE_REPLY",
//			category="Messages")
//	String MIX_SEND_STYLE;
//	
//	@SimulationProperty(name= "Waiting time user",
//			valueType = Integer.class,
////			intValue = 1,
//			category="Messages")
//	int TIME_TO_WAIT_FOR_FURTHER_DATA_FROM_USER;
//	
//	@SimulationProperty(name= "Waiting time proxy",
//			valueType = Integer.class,
////			intValue = 1,
//			category="Messages")
//	int TIME_TO_WAIT_FOR_DATA_FROM_DISTANT_PROXY;
//	
//	@SimulationProperty(name= "Send interval",
//			valueType = Integer.class,
////			intValue = 100,
//			category="Messages")
//	int BASIC_SYNCHRONOUS_SEND_INTERVAL_IN_MS;
//	
//	@SimulationProperty(name= "Reply interval",
//			valueType = Integer.class,
////			intValue = 100,
//			category="Messages")
//	int BASIC_SYNCHRONOUS_REPLY_INTERVAL_IN_MS;
}

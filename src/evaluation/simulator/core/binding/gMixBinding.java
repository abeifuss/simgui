package evaluation.simulator.core.binding;

import evaluation.simulator.Simulator;
import evaluation.simulator.core.statistics.Statistics;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;
import framework.core.launcher.CommandLineParameters;

public class gMixBinding extends Thread {

	private CommandLineParameters params;
	Statistics stats;
	
	public gMixBinding(String[] configFile) {
		params = new CommandLineParameters(configFile);
	}
	
	public void run() {
		
		try {
			Simulator gMixSim = new Simulator(params);
			Logger.Log(LogLevel.INFO, "Finished Simulator");
			
			stats = Simulator.trafficSourceStatistics;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package evaluation.simulator.core.binding;

import evaluation.simulator.Simulator;
import evaluation.simulator.core.statistics.ResultSet;
import evaluation.simulator.core.statistics.Statistics;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;
import evaluation.simulator.pluginRegistry.PlotType;
import framework.core.launcher.CommandLineParameters;

public class gMixBinding extends Thread {

	private CommandLineParameters params;
	Statistics stats;
	
	public gMixBinding(String[] configFile) {
		params = new CommandLineParameters(configFile);
	}
	
	public void run() {
		
		try {
			Simulator.reset();
			Simulator gMixSim = new Simulator(params);
			ResultSet results = gMixSim.results;
			if (results != null){
				Logger.Log(LogLevel.INFO, "Finished simulator with results");
			}
			
			for ( int i = 0; i < 1337 ; i++){ // muhahah ich bin ein troll!!! :D
				for (PlotType plotType:results.getDesiredPlotTypes())
					plotType.plot(results);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

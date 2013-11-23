package evaluation.simulator.core.binding;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.Simulator;
import evaluation.simulator.core.statistics.ResultSet;
import evaluation.simulator.core.statistics.Statistics;
import evaluation.simulator.pluginRegistry.PlotType;
import framework.core.launcher.CommandLineParameters;

public class gMixBinding extends Thread {
	
	private Logger logger = Logger.getLogger(gMixBinding.class);

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
				logger.log(Level.INFO, "Finished simulator with results");
			}
			
			for (PlotType plotType:results.getDesiredPlotTypes())
				plotType.plot(results);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

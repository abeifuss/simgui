package evaluation.simulator.plugins.delayBox;

import evaluation.simulator.Simulator;
import evaluation.simulator.annotations.plugin.Plugin;
import evaluation.simulator.annotations.property.IntSimulationProperty;

@Plugin(pluginKey = "RND_DELAY_BOX", 
	pluginName = "Random delay")
public class RndDelayBox extends DelayBoxImpl {
	
	@IntSimulationProperty(	name = "Minimum delay", 
			key = "RND_DELAY_BOX_MIN_DELAY", 
			property_to_vary = true, min = 0, max = 5)
	private int min = new Integer(
			Simulator.settings.getProperty(
					"RND_DELAY_BOX_MIN_DELAY"));
	
	@IntSimulationProperty(	name = "Maximum delay", 
			key = "RND_DELAY_BOX_MAX_DELAY",
			property_to_vary = true, min = 5, max = 10, 
			guiElement = "slider")
	private int max = new Integer(
			Simulator.settings.getProperty(
					"RND_DELAY_BOX_MAX_DELAY"));
	
	public RndDelayBox() {
		super();
	}
	
	@Override
	public int getReceiveDelay(int numberOfBytesToReceive) {
		return (int) (min + Math.random() * (max - min));
	}

	@Override
	public int getSendDelay(int numberOfBytesToSend) {
		return (int) (min + Math.random() * (max - min));
	}

}
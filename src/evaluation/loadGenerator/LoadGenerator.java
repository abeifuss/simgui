/*
 * gMix open source project - https://svs.informatik.uni-hamburg.de/gmix/
 * Copyright (C) 2012  Karl-Peter Fuchs
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package evaluation.loadGenerator;


import evaluation.loadGenerator.applicationLevelTraffic.requestReply.ALRR_Scheduled_ExitNodeRequestReceiver;
import evaluation.loadGenerator.asFastAsPossible.AFAP_Echo_ExitNodeRequestReceiver;
import evaluation.loadGenerator.asFastAsPossible.AFAP_LoadGenerator;
import evaluation.loadGenerator.dynamicSchedule.DynamicScheduleLoadGenerator;
import evaluation.loadGenerator.fixedSchedule.FixedScheduleLoadGenerator;
import framework.core.AnonNode;
import framework.core.gui.model.PlugInType;
import framework.core.gui.model.XMLResource;
import framework.core.launcher.GMixTool;
import framework.core.launcher.ToolName;


public class LoadGenerator extends GMixTool {

	public static enum InsertLevel {APPLICATION_LEVEL, MIX_PACKET_LEVEL};
	public static enum AL_Mode {AFAP, TRACE_FILE, CONSTANT_RATE, POISSON};
	public static enum MPL_Mode {AFAP, CONSTANT_RATE, POISSON};
	
	//private Output output;
	public InsertLevel INSERT_LEVEL;
	public AL_Mode AL_MODE;
	public MPL_Mode MPL_MODE;
	
	public XMLResource settings;
	public ToolName tool;
	
	public boolean IS_LOCAL_TEST;
	public int SCHEDULE_AHEAD;
	
	
	public LoadGenerator(XMLResource settings, ToolName tool) {
		//output = Output.getOutput(LoadGenerator.class);
		//output.out("\nLoadGenerator started");
		System.out.println("LoadGenerator started"); 
		
		this.settings = settings;
		if (tool == ToolName.LOCAL_TEST)
			this.IS_LOCAL_TEST = true;
		this.tool = ToolName.LOAD_GENERATOR;
		setLoadGeneratorPlugins();
		loadParameters(settings);
		
		boolean sendAsFastAsPossible = (AL_MODE == AL_Mode.AFAP || MPL_MODE == MPL_Mode.AFAP);
		boolean useFixedSchedule = (!settings.getPropertyAsBoolean("/gMixConfiguration/general/infoService/duplex") || !settings.getPropertyAsBoolean("/gMixConfiguration/general/loadGenerator/alTraceFileUseDynamicSchedule"));
		
		if (sendAsFastAsPossible) // use performance-optimized writer for AFAP mode (InsterLevel is irrelevant for AFAP)
			AFAP_LoadGenerator.createInstance(this);
		else if (useFixedSchedule) 
			FixedScheduleLoadGenerator.createInstance(this);
		else 
			DynamicScheduleLoadGenerator.createInstance(this);
	}
	
	
	private void setLoadGeneratorPlugins() {
		//clientSettings.setPlugIn("loadGeneratorClient", 5, PlugInType.CLIENT);
		
		
		//TODO MF This is not possible! You have to add every property of the plugIn. Besides you have to get the plugIn by its id. 
		//settings.addTemporaryValue("LAYER_5_PLUG-IN_MIX", "loadGeneratorPlugIn_v0_001");
		//settings.setProperty("LAYER_5_PLUG-IN_CLIENT", "loadGeneratorPlugIn_v0_001");
		settings.addTemporaryValue("/gMixConfiguration/composition/layer5/client/plugIn@id", "loadGeneratorClient");
		settings.addTemporaryValue("/gMixConfiguration/composition/layer5/client/plugIn@source", "src/plugIns/layer5application/loadGeneratorPlugIn_v0_001/client/PlugInSettings.xml");
		
		settings.addTemporaryValue("/gMixConfiguration/composition/layer5/mix/plugIn@id", "loadGeneratorMix");
		settings.addTemporaryValue("/gMixConfiguration/composition/layer5/mix/plugIn@source", "src/plugIns/layer5application/loadGeneratorPlugIn_v0_001/mix/PlugInSettings.xml");
		System.out.println("Attention: The composition was changed. Unexpected behaviour while accessing the porperties of layer 5 plug-ins is possible.");
		//output.out("Attention: The composition was changed. Unexpected behaviour while accessing the porperties of layer 5 plug-ins is possible.");
	
	}
	
	
	private void loadParameters(XMLResource settings) {
		settings.setTemporaryPrefix("/gMixConfiguration/general/loadGenerator/");
		
		if (settings.getPropertyAsString("generateLoad").equalsIgnoreCase("APPLICATION_LEVEL"))
			this.INSERT_LEVEL = InsertLevel.APPLICATION_LEVEL;
		else if (settings.getPropertyAsString("generateLoad").equalsIgnoreCase("MIX_PACKET_LEVEL"))
			this.INSERT_LEVEL = InsertLevel.MIX_PACKET_LEVEL;
		else
			System.err.println("could not process property \"generateLoad\": " + settings.getPropertyAsString("generateLoad")); 
		this.SCHEDULE_AHEAD = settings.getPropertyAsInt("scheduleAhead");
		if (INSERT_LEVEL == InsertLevel.APPLICATION_LEVEL) {
			if (settings.getPropertyAsString("alMode").equalsIgnoreCase("AFAP"))
				this.AL_MODE = AL_Mode.AFAP;
			else if (settings.getPropertyAsString("alMode").equalsIgnoreCase("TRACE_FILE"))
				this.AL_MODE = AL_Mode.TRACE_FILE;
			else if (settings.getPropertyAsString("alMode").equalsIgnoreCase("CONSTANT_RATE"))
				this.AL_MODE = AL_Mode.CONSTANT_RATE;
			else if (settings.getPropertyAsString("alMode").equalsIgnoreCase("POISSON"))
				this.AL_MODE = AL_Mode.POISSON;
			else
				System.err.println("could not process property \"alMode\": " + settings.getPropertyAsString("alMode")); 	
		} else if (INSERT_LEVEL == InsertLevel.MIX_PACKET_LEVEL) {
			if (settings.getPropertyAsString("mplMode").equalsIgnoreCase("AFAP"))
				this.MPL_MODE = MPL_Mode.AFAP;
			else if (settings.getPropertyAsString("mplMode").equalsIgnoreCase("CONSTANT_RATE"))
				this.MPL_MODE = MPL_Mode.CONSTANT_RATE;
			else if (settings.getPropertyAsString("mplMode").equalsIgnoreCase("POISSON"))
				this.MPL_MODE = MPL_Mode.POISSON;
			else
				System.err.println("could not process property \"mplMode\": " + settings.getPropertyAsString("mplMode")); 	
		}
		
		settings.resetTemporaryPrefix();
	}

	
	public static ExitNodeRequestReceiver createExitNodeRequestReceiver(AnonNode anonNode) {
		XMLResource settings  = anonNode.getSettings();
		
		settings.setTemporaryPrefix("/gMixConfiguration/general/loadGenerator/");
		
		boolean afapModeOn = 
				((settings.getPropertyAsString("generateLoad").equalsIgnoreCase("APPLICATION_LEVEL") 
						&& settings.getPropertyAsString("alMode").equalsIgnoreCase("AFAP")) 
				|| 
				(settings.getPropertyAsString("generateLoad").equalsIgnoreCase("MIX_PACKET_LEVEL")
						&& settings.getPropertyAsString("mplMode").equalsIgnoreCase("AFAP"))
			);
		
		settings.resetTemporaryPrefix();
		
		if (afapModeOn)
			return AFAP_Echo_ExitNodeRequestReceiver.createInstance(anonNode);
		else if (settings.getPropertyAsString("/gMixConfiguration/general/loadGenerator/generateLoad").equalsIgnoreCase("APPLICATION_LEVEL"))
			return new ALRR_Scheduled_ExitNodeRequestReceiver(anonNode);
		else if (settings.getPropertyAsString("/gMixConfiguration/general/loadGenerator/generateLoad").equalsIgnoreCase("MIX_PACKET_LEVEL"))
			throw new RuntimeException("no special purpose ExitNodeRequestReceiver for MIX_PACKET_LEVEL"); 
		else
			throw new RuntimeException("could not process property \"generateLoad\": " + settings.getPropertyAsString("/gMixConfiguration/general/loadGenerator/generateLoad"));
		
	}
	
	
	public static InsertLevel getInsertLevel(AnonNode anonNode) {
		XMLResource settings  = anonNode.getSettings();
		
		if (settings.getPropertyAsString("/gMixConfiguration/general/loadGenerator/generateLoad").equalsIgnoreCase("APPLICATION_LEVEL")) {
			return InsertLevel.APPLICATION_LEVEL;
		} else if (settings.getPropertyAsString("/gMixConfiguration/general/loadGenerator/generateLoad").equalsIgnoreCase("MIX_PACKET_LEVEL")) {
			return InsertLevel.MIX_PACKET_LEVEL;
		} else
			throw new RuntimeException("could not process property \"generateLoad\": " + settings.getPropertyAsString("generateLoad")); 
	}
	
	
	public final static boolean VALIDATE_IO = false; // TODO
	static {
		if (VALIDATE_IO)
			System.err.println(
					"WARNING: LoadGenerator.VALIDATE_IO is enabled;"
					+" this may severely decrease performance"
				); 
	}
}

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
package evaluation.simulator.plugins.outputStrategy;

import java.security.SecureRandom;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ExponentialDistributionImpl;

import evaluation.simulator.Simulator;
import evaluation.simulator.annotations.plugin.Plugin;
import evaluation.simulator.annotations.property.DoubleSimulationProperty;
import evaluation.simulator.annotations.property.FloatSimulationProperty;
import evaluation.simulator.annotations.property.IntSimulationProperty;
import evaluation.simulator.annotations.property.requirements.SgMixMaxClientMixDelayRequirement;
import evaluation.simulator.annotations.property.requirements.SgMixMaxInterMixDelayRequirement;
import evaluation.simulator.annotations.property.requirements.SgMixMinClientMixDelayRequirement;
import evaluation.simulator.annotations.property.requirements.SgMixMinInterMixDelayRequirement;
import evaluation.simulator.core.message.BasicMixMessage;
import evaluation.simulator.core.networkComponent.AbstractClient;
import evaluation.simulator.core.networkComponent.NetworkNode;

// This is an example of how to manually cap a plugin
// by providing the pluginLayerKay. If the pluginLayer
// is not present, the SimPropRegistry tries to find a superclass!
@Plugin(pluginKey = "STOP_AND_GO", pluginName="Stop And Go", pluginLayerKey = "OUTPUT_STRATEGY")
public class StopAndGoMessage extends BasicMixMessage {

	private long[] tsMin; // 0: first mix; 1: second mix...
	private long[] tsMax;
	public int[] delay;
	private int tsMinCounter = 0;
	private int tsMaxCounter = 0;
	private int delayCounter = 0;
	private ExponentialDistributionImpl expDist;
	private static SecureRandom secureRandom = new SecureRandom();
	private int identifier;
	private static int idCounter = 0;
	
	@DoubleSimulationProperty ( name = "Security parameter mu", 
			key = "SGMIX_SECURITY_PARAMETER_MU")
	private double securityParameterMu;
	
	@IntSimulationProperty( name = "Minimum inter mix delay (ms)", 
			key = "SGMIX_MIN_INTER_MIX_DELAY",
			min = 0, value_requirements = SgMixMinInterMixDelayRequirement.class)
	private int minInterMixDelay;
	
	@IntSimulationProperty( name = "Maximum inter mix delay (ms)", 
			key = "SGMIX_MAX_INTER_MIX_DELAY",
			min = 0, value_requirements = SgMixMaxInterMixDelayRequirement.class)
	private int maxInterMixDelay;
	
	@IntSimulationProperty( name = "Minimum client mix delay (ms)", 
			key = "SGMIX_MIN_CLIENT_MIX_DELAY",
			value_requirements=SgMixMinClientMixDelayRequirement.class,
			min = 0)
	private int minClientMixDelay;
	
	@IntSimulationProperty( name = "Maximum client mix delay (ms)", 
			key = "SGMIX_MAX_CLIENT_MIX_DELAY",
			value_requirements=SgMixMaxClientMixDelayRequirement.class,
			min = 0)
	private int maxClientMixDelay;
	
	@IntSimulationProperty( name = "Maximum clock deviation (ms)", 
			key = "SGMIX_MAX_CLOCK_DEVITION",
			min = 0)
	private int maxClockDeviation;
	
	
	public StopAndGoMessage(boolean isRequest, NetworkNode source,
			NetworkNode destination, AbstractClient owner, long creationTime,
			boolean isDummy) {
		
		super(isRequest, source, destination, owner, creationTime, isDummy);
		
		this.identifier = idCounter++;
		
		// generate delays
		securityParameterMu = Simulator.settings.getPropertyAsDouble("SGMIX_SECURITY_PARAMETER_MU");
		Simulator simulator = Simulator.getSimulator();
		int numberOfMixes = simulator.getMixes().size();
		this.delay = new int[numberOfMixes];
		this.expDist = new ExponentialDistributionImpl(1d/securityParameterMu);
		this.expDist.reseedRandomGenerator(secureRandom.nextLong());
		boolean useTimeStamps = Simulator.settings.getPropertyAsBoolean("SGMIX_USE_TIMESTAMPS");
		
		for (int i=0; i<numberOfMixes; i++) {
			
			try {
				delay[i] = (int)Math.round(expDist.sample() * 1000d);
			} catch (MathException e) {
				e.printStackTrace();
				throw new RuntimeException("ERROR: could not draw sample from exponential distribution!"); 
			}

		}
		
		if (useTimeStamps) {
			
			minInterMixDelay = Simulator.settings.getPropertyAsInt("SGMIX_MIN_INTER_MIX_DELAY");
			maxInterMixDelay = Simulator.settings.getPropertyAsInt("SGMIX_MAX_INTER_MIX_DELAY");
			minClientMixDelay = Simulator.settings.getPropertyAsInt("SGMIX_MIN_CLIENT_MIX_DELAY");
			maxClientMixDelay = Simulator.settings.getPropertyAsInt("SGMIX_MAX_CLIENT_MIX_DELAY");
			maxClockDeviation = Simulator.settings.getPropertyAsInt("SGMIX_MAX_CLOCK_DEVITION");
			
			tsMin = new long[numberOfMixes];
			tsMax = new long[numberOfMixes];
				
			for (int i=0; i<numberOfMixes; i++) {
				if (isRequest) {
					tsMin[i] = getMinTimestampForRequest(i, maxClockDeviation, delay, minInterMixDelay, minClientMixDelay);
					tsMax[i] = getMaxTimestampForRequest(i, maxClockDeviation, delay, maxInterMixDelay, maxClientMixDelay);
				} else {
					tsMin[i] = getMinTimestampForReply(i, maxClockDeviation, delay, minInterMixDelay, minClientMixDelay);
					tsMax[i] = getMaxTimestampForReply(i, maxClockDeviation, delay, maxInterMixDelay, maxClientMixDelay);
				}
				
			}
			
		}
		
	}

	
	// mixNumer: 0,1,...,numberOfMixes
	public static long getMinTimestampForRequest(	int mixNumber, 
													int maxClockDeviation,
													int[] sgDelays, 
													int minInterMixDelay, 
													int minClientMixDelay) {
		
		int sumOfSgDelays = 0;
		for (int i=0; i<mixNumber; i++)
			sumOfSgDelays += sgDelays[i];
		
		int minDelay = minClientMixDelay + mixNumber * minInterMixDelay;
		int maxClockDev = (mixNumber + 1) * maxClockDeviation;

		return Simulator.getNow() + sumOfSgDelays + minDelay - maxClockDev;
	
	}
	
	
	public static long getMaxTimestampForRequest(	int mixNumber, 
													int maxClockDeviation,
													int[] sgDelays, 
													int maxInterMixDelay, 
													int maxClientMixDelay) {

		int sumOfSgDelays = 0;
		for (int i=0; i<mixNumber; i++)
			sumOfSgDelays += sgDelays[i];

		int maxDelay = maxClientMixDelay + mixNumber * maxInterMixDelay;
		int maxClockDev = (mixNumber + 1) * maxClockDeviation;

		return Simulator.getNow() + sumOfSgDelays + maxDelay + maxClockDev;

	}
	
	
	public static long getMinTimestampForReply(	int mixNumber, 
												int maxClockDeviation,
												int[] sgDelays, 
												int minInterMixDelay, 
												int minClientMixDelay) {

		if (mixNumber == 0)
			return Long.MIN_VALUE; 
		
		int sumOfSgDelays = 0;
		for (int i=0; i<mixNumber; i++)
			sumOfSgDelays += sgDelays[i];

		int minDelay = mixNumber * minInterMixDelay;
		int maxClockDev = mixNumber * maxClockDeviation;

		if (mixNumber == sgDelays.length-1)
			minDelay = minDelay - minInterMixDelay + minClientMixDelay;
		
		return Simulator.getNow() + sumOfSgDelays + minDelay - maxClockDev;

	}
	
	
	public static long getMaxTimestampForReply(	int mixNumber, 
												int maxClockDeviation,
												int[] sgDelays, 
												int maxInterMixDelay, 
												int maxClientMixDelay) {

		if (mixNumber == 0)
			return Long.MAX_VALUE;  

		int sumOfSgDelays = 0;
		for (int i=0; i<mixNumber; i++)
			sumOfSgDelays += sgDelays[i];

		int maxDelay = mixNumber * maxInterMixDelay;
		int maxClockDev = mixNumber * maxClockDeviation;

		if (mixNumber == sgDelays.length-1)
			maxDelay = maxDelay - maxInterMixDelay + maxClientMixDelay;

		return Simulator.getNow() + sumOfSgDelays + maxDelay - maxClockDev;

	}

	
	// call only once per mix!
	public long getTsMin() {
		return tsMin[tsMinCounter++];
	}
	
	// call only once per mix!
	public long getTsMax() {
		return tsMax[tsMaxCounter++];
	}

	// call only once per mix!
	public int getDelay() {
		return delay[delayCounter++];
	}
	
	
	public String toString() {
		
		String replyOrRequest = super.isRequest() ? "Request" : "Reply";
		
		
		String timeInfo = " ";
		for (int i=0; i<tsMin.length; i++)
			timeInfo += ""+i +": [min:" +tsMin[i] +", expected: "+delay[i] +", max: "+tsMax[i] +"] ";
		
		return "MixMessage ("+replyOrRequest +", owner: "+owner +", id: " +identifier +timeInfo+")";
		
	}
	
}

/*
 * gMix open source project - https://svs.informatik.uni-hamburg.de/gmix/
 * Copyright (C) 2013  Karl-Peter Fuchs
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
package evaluation.simulator.plugins.trafficSource;

import java.security.SecureRandom;

import org.apache.commons.math.random.RandomDataImpl;

import evaluation.simulator.Simulator;
import evaluation.simulator.annotations.plugin.Plugin;
import evaluation.simulator.annotations.simulationProperty.DoubleSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.IntSimulationProperty;
import evaluation.simulator.core.event.Event;
import evaluation.simulator.core.event.SimulationEvent;
import evaluation.simulator.core.message.EndToEndMessage;
import evaluation.simulator.core.networkComponent.AbstractClient;
import evaluation.traceParser.engine.dataStructure.ExtendedTransaction;

@Plugin(pluginKey = "PARETO")
public class ParetoClient extends AbstractClient {

	// Properties
	@IntSimulationProperty( name = "Request size", key = "PARETO_REQUEST_SIZE", enableAuto = true)
	private int REQUEST_SIZE;
	@IntSimulationProperty( name = "Reply size", key = "PARETO_REPLY_SIZE", enableAuto = true)
	private int REPLY_SIZE;
	@IntSimulationProperty( name = "Resolve time (ms)", key = "PARETO_RESOLVE_TIME")
	private int RESOLVE_TIME;
	@DoubleSimulationProperty( name = "Alpha", key = "PARETO_ALPHA")
	private double ALPHA;
	@DoubleSimulationProperty( name = "Average requests per second", key = "PARETO_AVERAGE_REQUESTS_PER_SECOND_AND_CLIENT")
	private double AVERAGE_REQUESTS_PER_SECOND;
	
	private NormalizedPareto pareto;
	private RandomDataImpl randomDataImpl;
	private static SecureRandom secureRandom = new SecureRandom();
	
	
	public ParetoClient(String identifier, Simulator simulator, int clientId) {
		super(identifier, simulator);
		if (Simulator.settings.getProperty("PARETO_REQUEST_SIZE").equals("AUTO"))
			this.REQUEST_SIZE = Simulator.settings.getPropertyAsInt("MIX_REQUEST_PAYLOAD_SIZE");
		else
			this.REQUEST_SIZE = Simulator.settings.getPropertyAsInt("PARETO_REQUEST_SIZE");
		if (Simulator.settings.getProperty("PARETO_REPLY_SIZE").equals("AUTO"))
			this.REPLY_SIZE = Simulator.settings.getPropertyAsInt("MIX_REPLY_PAYLOAD_SIZE");
		else
			this.REPLY_SIZE = Simulator.settings.getPropertyAsInt("PARETO_REPLY_SIZE");
		this.RESOLVE_TIME = Simulator.settings.getPropertyAsInt("PARETO_RESOLVE_TIME");
		this.clientId = clientId;
		this.ALPHA = Simulator.settings.getPropertyAsDouble("PARETO_ALPHA");
		this.AVERAGE_REQUESTS_PER_SECOND = Simulator.settings.getPropertyAsDouble("PARETO_AVERAGE_REQUESTS_PER_SECOND_AND_CLIENT");
		this.pareto = new NormalizedPareto(ALPHA, AVERAGE_REQUESTS_PER_SECOND);
		this.randomDataImpl = new RandomDataImpl();
		this.randomDataImpl.reSeed(secureRandom.nextLong());
	}

	
	public void startSending() {
		scheduleNextSends();
	}

	
	@Override
	public void incomingMessage(EndToEndMessage message) {
		// ParetoClient does not take feedback into account...
	}

	
	@Override
	public void messageReachedServer(EndToEndMessage message) {
		// ParetoClient does not take feedback into account...
	}
	
	
	private void sendMessage() {
		ExtendedTransaction et = new ExtendedTransaction(0, 0l, 0l, REQUEST_SIZE, 0, new long[] {0}, new long[] {(0+RESOLVE_TIME)}, new int[] {REPLY_SIZE});
		EndToEndMessage eteMessage = new EndToEndMessage(0, et, true);
		sendMessage(eteMessage);
	}
	
	
	public void scheduleNextSends() {
		long now = Simulator.getNow();
		long end = now + 1000;
		long numberOfMessages = Math.round(pareto.drawSample());
		for (int i=0; i<numberOfMessages; i++) {
			long whenToSend = (long) randomDataImpl.nextUniform(now, end);
			simulator.scheduleEvent(new Event(this, whenToSend, ParetoClientClientEvent.SEND_MESSAGE), this);	
		}
	}
	
	
	public enum ParetoClientClientEvent implements SimulationEvent {
		SEND_MESSAGE;
	}
	
	
	@Override
	public void executeEvent(Event event) {
		if (event.getEventType() instanceof ParetoClientClientEvent) {
			if (event.getEventType() == ParetoClientClientEvent.SEND_MESSAGE) {
				sendMessage();
			} else
				throw new RuntimeException("ERROR: received unknown Event: " +event.toString()); 
		} else {
			super.executeEvent(event);
		}
	}

}
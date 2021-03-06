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

import evaluation.simulator.Simulator;
import evaluation.simulator.annotations.plugin.Plugin;
import evaluation.simulator.annotations.property.IntSimulationProperty;
import evaluation.simulator.core.networkComponent.AbstractClient;

@Plugin(pluginKey = "CONSTANT", pluginName="Constant")
public class SendConstantModel extends TrafficSourceImplementation {

	private SendConstantClient[] clients;
	
	@IntSimulationProperty(name = "Number of Clients", 
			key = "CONSTANT_NUMBER_OF_CLIENTS_TO_SIMULATE", 
			position = 1,
			min = 1)
	private int numberOfClients;
	
	@Override
	public AbstractClient[] createClientsArray() {
		numberOfClients = Simulator.settings.getPropertyAsInt("CONSTANT_NUMBER_OF_CLIENTS_TO_SIMULATE");
		clients = new SendConstantClient[numberOfClients];
		for (int i=0; i<clients.length; i++) 
			clients[i] = new SendConstantClient("Client" +i, Simulator.getSimulator(), i);
		return this.clients;
	}

	
	@Override
	public void startSending() {
		for (SendConstantClient client: clients)
			client.startSending();
	}

}

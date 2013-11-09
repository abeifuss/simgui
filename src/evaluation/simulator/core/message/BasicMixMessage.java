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
package evaluation.simulator.core.message;

import java.util.Vector;

import evaluation.simulator.Simulator;
import evaluation.simulator.annotations.plugin.PluginSuperclass;
import evaluation.simulator.annotations.simulationProperty.IntSimulationProperty;
import evaluation.simulator.core.networkComponent.AbstractClient;
import evaluation.simulator.core.networkComponent.NetworkNode;

public class BasicMixMessage extends MixMessage {

	@IntSimulationProperty( 
			name = "Mix request playload size (byte)", 
			propertykey = "MIX_REQUEST_PAYLOAD_SIZE", 
			order = 1, 
			inject = "5:RECODING_SCHEME,Recoding Scheme (injected)")
	private int requestPlayloadSize;
	@IntSimulationProperty( 
			name = "Mix reply playload size (byte)", 
			propertykey = "MIX_REPLY_PAYLOAD_SIZE", 
			order = 3, 
			inject = "5:RECODING_SCHEME,Recoding Scheme (injected)")
	private int replyPlayloadSize;
	@IntSimulationProperty( 
			name = "Mix request header size (byte)", 
			propertykey = "MIX_REQUEST_HEADER_SIZE", 
			order = 2,
			inject = "5:RECODING_SCHEME,Recoding Scheme (injected)")
	private int requestHeaderSize;
	@IntSimulationProperty( 
			name = "Mix reply header size (byte)", 
			propertykey = "MIX_REPLY_HEADER_SIZE", 
			order = 4,
			inject = "5:RECODING_SCHEME,Recoding Scheme (injected)")
	private int replyHeaderSize;
	
	private int payloadSize;
	private int maxPayloadSize; // byte
	private int headerSize; // byte
	private int totalSize; // byte
	private Vector<PayloadObject> payloadObjectsContained;
	private int transportMessagesContained = 0;
	//private int messageFragmentsContained = 0;
	
	// RECORDING_SCHEME
	
	protected BasicMixMessage(boolean isRequest, NetworkNode source,
			NetworkNode destination, AbstractClient owner, long creationTime,
			boolean isDummy) {
		
		super(isRequest, source, destination, owner, creationTime, isDummy, null);
		this.maxPayloadSize = isRequest ? Simulator.settings.getPropertyAsInt("MIX_REQUEST_PAYLOAD_SIZE") : Simulator.settings.getPropertyAsInt("MIX_REPLY_PAYLOAD_SIZE");
		this.headerSize = isRequest ? Simulator.settings.getPropertyAsInt("MIX_REQUEST_HEADER_SIZE") : Simulator.settings.getPropertyAsInt("MIX_REPLY_HEADER_SIZE");
		this.totalSize = maxPayloadSize + headerSize;
		payloadObjectsContained = new Vector<PayloadObject>(10,10); // TODO calculate instead of fixed 10,10
		super.setPayload(payloadObjectsContained);
		
	}


	@Override
	public int getLength() {
		return totalSize;
	}
	

	@Override
	public int getPayloadLength() {
		return payloadSize;
	}


	@Override
	public int getNumberOfMessagesContained() {
		return transportMessagesContained;
	}


	@Override
	public int getMaxPayloadLength() {
		return maxPayloadSize;
	}


	@Override
	public PayloadObject[] getPayloadObjectsContained() {
		return this.payloadObjectsContained.toArray(new PayloadObject[0]);
	}


	@Override
	public boolean addPayloadObject(PayloadObject payloadObject) {
		if (payloadObject instanceof TransportMessage) {
			this.transportMessagesContained++;
		} else if (payloadObject instanceof MessageFragment) {
			//this.messageFragmentsContained++;
			if (((MessageFragment)payloadObject).isLastFragment())
				this.transportMessagesContained++;
		} else
			throw new RuntimeException("ERROR: unknown PayloadObject" +payloadObject); 
		
		if (payloadObject.getLength() <= maxPayloadSize - payloadSize) { // enough space
			
			payloadSize += payloadObject.getLength();
			payloadObjectsContained.add(payloadObject);
			if (payloadObject instanceof TransportMessage)
				((TransportMessage)payloadObject).setAssociatedMixMessage(this);
			
			return true;
			
		} else { // not enough space
			
			return false;
			
		}
	}


	@Override
	public TransportMessage[] getTransportMessagesContained() {
		
		if (isDummy)
			return new TransportMessage[0];
		
		Vector<TransportMessage> transportMessagesContained = new Vector<TransportMessage>(payloadObjectsContained.size());
		
		for (PayloadObject po: payloadObjectsContained) {
			if (po instanceof TransportMessage) {
				//System.out.println("its a complete TransportMessage (" +(TransportMessage)po +"), size: " +((TransportMessage)po).getLength()); 
				transportMessagesContained.add((TransportMessage)po);
			} else {
				if (((MessageFragment)po).isLastFragment()) {
					//System.out.println("its the final fragment (" +((MessageFragment)po).getAssociatedTransportMessage() +", "  +(MessageFragment)po +"), size: " +((MessageFragment)po).getLength()); 
					transportMessagesContained.add(((MessageFragment)po).getAssociatedTransportMessage());
				} else {
					//System.out.println("its a fragment (" +((MessageFragment)po).getAssociatedTransportMessage() +", " +(MessageFragment)po +"), size: " +((MessageFragment)po).getLength()); 
				}
			}
		}
		return transportMessagesContained.toArray(new TransportMessage[0]);
		
	}
	
}

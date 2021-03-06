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
package plugIns.layer2recodingScheme.noDelay_v0_001;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import framework.core.controller.Implementation;
import framework.core.interfaces.Layer1NetworkClient;
import framework.core.interfaces.Layer2RecodingSchemeClient;
import framework.core.interfaces.Layer3OutputStrategyClient;
import framework.core.interfaces.Layer4TransportClient;
import framework.core.message.ExternalMessage.DummyStatus;
import framework.core.message.Reply;
import framework.core.message.Request;
import framework.core.routing.MixList;
import framework.core.routing.RoutingMode;
import framework.core.util.Util;


public class ClientPlugIn extends Implementation implements Layer2RecodingSchemeClient {

	private SecureRandom secureRandom;
	
	
	@Override
	public void constructor() {
		
	}
	

	@Override
	public void initialize() {
		try {
			this.secureRandom = SecureRandom.getInstance(settings.getProperty("PRNG_ALGORITHM"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	

	@Override
	public void begin() {
		
	}
	
	
	@Override
	public void setReferences(
			Layer1NetworkClient layer1,
			Layer2RecodingSchemeClient layer2, 
			Layer3OutputStrategyClient layer3,
			Layer4TransportClient layer4) {
		assert layer2 == this;
	}
	

	@Override
	public Request applyLayeredEncryption(Request request) {
		if (request.getByteMessage().length  > anonNode.MAX_PAYLOAD)
			throw new RuntimeException("can't send more than " +anonNode.MAX_PAYLOAD +" bytes in one message");
		byte[] lengthHeader = Util.shortToByteArray(request.getByteMessage().length);
		if (request.getByteMessage().length < anonNode.MAX_PAYLOAD) { // add padding
			int paddingLength = anonNode.MAX_PAYLOAD - request.getByteMessage().length;
			byte[] padding = new byte[paddingLength];
			secureRandom.nextBytes(padding);
			byte[] message = Util.concatArrays(request.getByteMessage(), padding);
			request.setByteMessage(message);
		}
		byte[] message = Util.concatArrays(lengthHeader, request.getByteMessage());
		request.setByteMessage(message);
		// TODO: support additional headers (e.g. for sg-mixes)
		
		if (anonNode.ROUTING_MODE == RoutingMode.FREE_ROUTE_SOURCE_ROUTING) { // add route-header
			assert request.route != null;
			byte[] routeArray = MixList.packIdArray(request.route, (short)0);
			if (anonNode.DISPLAY_ROUTE_INFO)
				System.out.println("" +anonNode +" creating route header"); 
			request.setByteMessage(Util.concatArrays(routeArray, request.getByteMessage()));
		}
		return request;
		
		/*if (anonNode.ROUTING_MODE == RoutingMode.CASCADE) {
			return request;
		} else if (anonNode.ROUTING_MODE == RoutingMode.FREE_ROUTE_SOURCE_ROUTING) {
			assert request.route != null;
			byte[] routeArray = MixList.packIdArray(request.route, (short)0);
			if (anonNode.DISPLAY_ROUTE_INFO)
				System.out.println("" +anonNode +" creating route header"); 
			request.setByteMessage(Util.concatArrays(routeArray, request.getByteMessage()));
			return request;
		} else if (anonNode.ROUTING_MODE == RoutingMode.FREE_ROUTE_DYNAMIC_ROUTING) {
			return request;
		} else {
			throw new RuntimeException("not supported routing mode: " +anonNode.ROUTING_MODE); 
		}*/
	}

	
	@Override
	public int getMaxPayloadForNextMessage() {
		return anonNode.MAX_PAYLOAD;
		/*removed, because the payload should always be the same (headers are allowed to become bigger)
		if (anonNode.ROUTING_MODE == RoutingMode.CASCADE) {
			return anonNode.MAX_PAYLOAD;
		} else if (anonNode.ROUTING_MODE == RoutingMode.FREE_ROUTE_SOURCE_ROUTING) {
			return anonNode.MAX_PAYLOAD - MixPlugIn.getRouteHeaderSize(anonNode);
		} else if (anonNode.ROUTING_MODE == RoutingMode.FREE_ROUTE_DYNAMIC_ROUTING) {
			return anonNode.MAX_PAYLOAD;
		} else {
			throw new RuntimeException("not supported routing mode: " +anonNode.ROUTING_MODE); 
		}*/
	}

	
	@Override
	public int getMaxPayloadForNextReply() {
		return anonNode.MAX_PAYLOAD;
	}

	
	@Override
	public Reply extractPayload(Reply reply) {
		byte[] payload = reply.getByteMessage();
		//System.out.println("cl received rep: " +Util.toHex(reply.getByteMessage())); // TODO: remove
		int lengthOfPayload = Util.byteArrayToShort(Arrays.copyOf(payload, 2));
		assert lengthOfPayload >= 0: lengthOfPayload;
		if (lengthOfPayload == 0) {
			reply.setDummyStatus(DummyStatus.DUMMY);
			reply.setByteMessage(new byte[0]);
		} else {
			reply.setDummyStatus(DummyStatus.NO_DUMMY);
			payload = Arrays.copyOfRange(payload, 2, 2 + lengthOfPayload);
			reply.setByteMessage(payload);
		}
		return reply;
	}

}
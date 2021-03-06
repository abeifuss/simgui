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
package plugIns.layer5application.loadGeneratorPlugIn_v0_001;

import java.util.Arrays;
import java.util.Random;

import framework.core.AnonNode;
import framework.core.message.Request;
import framework.core.routing.RoutingMode;
import framework.core.socket.datagram.DatagramAnonServerSocketImpl;
import framework.core.socket.socketInterfaces.AnonMessage;
import framework.core.socket.socketInterfaces.IO_EventObserver;
import framework.core.socket.socketInterfaces.AnonSocketOptions.CommunicationDirection;
import framework.core.socket.socketInterfaces.NoneBlockingAnonSocketOptions.IO_Mode;
import framework.core.util.Util;


public class MixPacketLevelHandler implements IO_EventObserver {

	private DatagramAnonServerSocketImpl socket;
	private AnonNode owner;
	private final boolean IS_DUPLEX;
	private Random random = new Random();
	
	
	public MixPacketLevelHandler(AnonNode anonNode) {
		System.out.println("MixPacketLevelHandler started"); 
		this.owner = anonNode;
		this.IS_DUPLEX = anonNode.IS_DUPLEX;
		CommunicationDirection cd = IS_DUPLEX ? CommunicationDirection.DUPLEX : CommunicationDirection.SIMPLEX_RECEIVER;
		IO_Mode ioMode = IO_Mode.OBSERVER_PATTERN;
		this.socket = (DatagramAnonServerSocketImpl) anonNode.createDatagramServerSocket(
				owner.getSettings().getPropertyAsInt("SERVICE_PORT1"), 
				cd, 
				ioMode,
				this,
				true, 
				true, 
				owner.ROUTING_MODE != RoutingMode.CASCADE
			);
	}

	@Override
	public void incomingRequest(Request request) {
		//System.out.println("DISTANT_PROXY: received request"); 
		AnonMessage message = new AnonMessage(request.getByteMessage());
		message.setUser(request.getOwner());
		if (this.IS_DUPLEX) { // extract pseudonym
			message.setMaxReplySize(socket.getMaxSizeForNextMessageSend());
			int endToEndPseudonym = Util.byteArrayToInt(Arrays.copyOf(message.getByteMessage(), 4));
			message.setByteMessage(Arrays.copyOfRange(message.getByteMessage(), 4, message.getByteMessage().length));
			message.setSourcePseudonym(endToEndPseudonym);
			System.err.println("test"); 
			// send reply:
			if (message.getByteMessage().length == message.getMaxReplySize()) {
				socket.sendMessage(message);
			} else {
				byte[] replyPayload = new byte[message.getMaxReplySize()];
				random.nextBytes(replyPayload);
				message.setByteMessage(replyPayload);
				socket.sendMessage(message);
			}
		}

	}

	
}

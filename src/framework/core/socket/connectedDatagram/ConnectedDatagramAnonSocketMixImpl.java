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
package framework.core.socket.connectedDatagram;

import java.io.IOException;


import framework.core.AnonNode;
import framework.core.controller.Layer4TransportMixController;
import framework.core.socket.socketInterfaces.AdaptiveAnonSocket;
import framework.core.socket.socketInterfaces.ConnectedDatagramAnonSocketMix;
import framework.core.userDatabase.User;


public class ConnectedDatagramAnonSocketMixImpl extends AdaptiveAnonSocket implements ConnectedDatagramAnonSocketMix {

	private boolean isConnected = false;
	private ConnectedDatagramAnonServerSocketImpl serverSocket;
	private User user;
	private Layer4TransportMixController layer4controller;
	
	
	// TODO: add isMessageOriented (allow payloads larger than ma x payload and handle fragmentation) (might also be useful for other sockets)
	public ConnectedDatagramAnonSocketMixImpl(
			ConnectedDatagramAnonServerSocketImpl serverSocket,
			User user,
			AnonNode owner,
			int endToEndPseudonym,
			CommunicationDirection communicationMode,
			boolean isReliable, 
			boolean isOrderPreserving, 
			boolean isFreeRoute
			) {
		super(	owner, 
				endToEndPseudonym,
				communicationMode, 
				true, 
				isReliable,
				isOrderPreserving, 
				isFreeRoute
				);
		this.layer4controller = owner.getTransportLayerControllerMix();
		this.serverSocket = serverSocket;
		this.isConnected = true;
		if (isDuplex && !owner.LAYER_1_LINKS_MESSAGES)
			throw new RuntimeException("not yet supported"); // TODO: requires same user-object for each message (maybe this is a general problem and can't be solved without layer1 linkage) 
	}

	
	@Override
	public void disconnect() throws IOException {
		if (!isConnected)
			throw new IOException("not connected");
		serverSocket.disconnect(endToEndPseudonym);
		isConnected = false;
	}

	
	@Override
	public boolean isConnected() {
		return this.isConnected;
	}

	
	@Override
	public byte[] receiveMessage() throws IOException {
		if (!isConnected)
			throw new IOException("not connected");
		return super.getNextRequest().getByteMessage();
	}

	
	@Override
	public void sendMessage(byte[] payload) throws IOException {
		if (!isDuplex)
			throw new RuntimeException("this socket is simplex only");
		if (!isConnected)
			throw new IOException("not connected");
		if (payload.length > getMaxSizeForNextMessageSend())
			throw new IOException("message too large; see getMaxSizeForNextMessageSend()");
		
		layer4controller.write(user, payload);
	}

	
	@Override
	public int getMaxSizeForNextMessageSend() throws IOException {
		if (!isConnected)
			throw new IOException("not connected");
		return layer4controller.getMaxSizeOfNextWrite();
	}

	
	@Override
	public int getMaxSizeForNextMessageReceive() throws IOException {
		if (!isConnected)
			throw new IOException("not connected");
		int maxSize = layer4controller.getMaxSizeOfNextRead() -2; // -2 for port
		if (!owner.LAYER_1_LINKS_MESSAGES)
			maxSize -= 4; // pseudonym
		return maxSize;
	}


	@Override
	public User getUser() {
		return user;
	}


	@Override
	public AdaptiveAnonSocket getImplementation() {
		return this;
	}

}

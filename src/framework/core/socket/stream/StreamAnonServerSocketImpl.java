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
package framework.core.socket.stream;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import framework.core.AnonNode;
import framework.core.message.Request;
import framework.core.socket.socketInterfaces.AdaptiveAnonServerSocket;
import framework.core.socket.socketInterfaces.IO_EventObserver;
import framework.core.socket.socketInterfaces.IO_EventObserver_Stream;
import framework.core.socket.socketInterfaces.StreamAnonServerSocket;
import framework.core.socket.socketInterfaces.StreamAnonSocketMix;
import framework.core.util.Util;


public class StreamAnonServerSocketImpl extends AdaptiveAnonServerSocket implements StreamAnonServerSocket {

	private ConcurrentHashMap<Integer, StreamAnonSocketMixImpl> sockets;
	private LinkedBlockingQueue<StreamAnonSocketMixImpl> newConncetions;
	
	
	public StreamAnonServerSocketImpl(
			AnonNode owner,
			int bindPseudonym,
			int bindPort,
			CommunicationDirection communicationMode,
			IO_Mode ioMode,
			IO_EventObserver requestObserver,
			boolean isFreeRoute
			) {
		
		super(	owner,
				bindPseudonym, 
				bindPort, 
				communicationMode, 
				ioMode,
				requestObserver,
				true,
				true, 
				true, 
				isFreeRoute);
		if (communicationMode == CommunicationDirection.DUPLEX && !owner.IS_DUPLEX)
			throw new RuntimeException("the current plug-in config does not suport duplex sockets");
		if (communicationMode == CommunicationDirection.SIMPLEX_SENDER)
			throw new RuntimeException("this is a simplex socket (server backend); the server backend can only be \"CommunicationMode.SIMPLEX_RECEIVER\"");
		if (ioMode == IO_Mode.OBSERVER_PATTERN && !(requestObserver instanceof IO_EventObserver_Stream))
			throw new RuntimeException("this socket requires an requestObserver of type IO_EventObserver_Stream");
		this.sockets = new  ConcurrentHashMap<Integer, StreamAnonSocketMixImpl>((int) (owner.EXPECTED_NUMBER_OF_USERS * 1.2));
		this.newConncetions = new LinkedBlockingQueue<StreamAnonSocketMixImpl>();
	}

	
	public StreamAnonServerSocketImpl(
			AnonNode owner,
			int bindPseudonym,
			int bindPort,
			CommunicationDirection communicationMode,
			IO_Mode ioMode,
			boolean isFreeRoute
			) {
		
		this(	owner,
				bindPseudonym, 
				bindPort, 
				communicationMode, 
				ioMode,
				null,
				isFreeRoute);
	}
	
	@Override
	public StreamAnonSocketMix accept() {
		try {
			return newConncetions.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return accept();
		}
	}

	
	private void putInNewConnectionQueue(StreamAnonSocketMixImpl socket) {
		try {
			newConncetions.put(socket);
		} catch (InterruptedException e) {
			e.printStackTrace();
			putInNewConnectionQueue(socket);
		}
	}
	
	
	@Override
	public void incomingRequest(Request request) {
		int endToEndPseudonym;
		if (owner.LAYER_1_LINKS_MESSAGES) { // use user-id as pseudonym
			endToEndPseudonym = request.getOwner().getIdentifier();
		} else { // extract pseudonym
			endToEndPseudonym = Util.byteArrayToInt(Arrays.copyOf(request.getByteMessage(), 4));
			request.setByteMessage(Arrays.copyOfRange(request.getByteMessage(), 4, request.getByteMessage().length));
		}
		StreamAnonSocketMixImpl socket = sockets.get(endToEndPseudonym);
		if (socket == null) {
			socket = new StreamAnonSocketMixImpl(
					this,
					request.getOwner(),
					owner, 
					endToEndPseudonym,
					communicationDirection,
					isFreeRoute);
			sockets.put(endToEndPseudonym, socket);
			if (requestObserver != null) { // notify observer
				((IO_EventObserver_Stream)requestObserver).incomingConnection(socket);
			} else { // store for later async read
				putInNewConnectionQueue(socket);
			}
		}
		socket.newIncomingMessage(request);
		if (requestObserver != null) // notify observer
			((IO_EventObserver_Stream)requestObserver).dataAvailable(socket);
	}
	

	public void disconnect(int endToEndPseudonym) {
		sockets.remove(endToEndPseudonym);
	}


	@Override
	public AdaptiveAnonServerSocket getImplementation() {
		return this;
	}

}
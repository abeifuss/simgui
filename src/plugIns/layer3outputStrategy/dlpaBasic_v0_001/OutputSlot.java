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
package plugIns.layer3outputStrategy.dlpaBasic_v0_001;

import java.util.Arrays;
import java.util.HashMap;

import framework.core.AnonNode;
import framework.core.gui.model.XMLResource;
import framework.core.interfaces.Layer2RecodingSchemeMix;
import framework.core.message.MixMessage;
import framework.core.message.Reply;
import framework.core.message.Request;
import framework.core.userDatabase.User;
import framework.core.userDatabase.UserDatabase;


public class OutputSlot {

	private AnonNode anonNode;
	private HashMap<User, MixMessage> messagesToSend;
	private boolean isRequestSlot;
	private long timeOfOutput;
	private XMLResource settings;
	private UserDatabase userDatabase;
	private Layer2RecodingSchemeMix recodingScheme;
	
	
	public OutputSlot(boolean isRequestSlot, long timeOfOutput, AnonNode anonNode) {
		this.settings = anonNode.getSettings();
		this.userDatabase = anonNode.getUserDatabase();
		this.messagesToSend = new HashMap<User, MixMessage>(settings.getPropertyAsInt("/gMixConfiguration/composition/layer3/mix/plugIn/dlpaBasicDefaultSlotSize"));
		this.isRequestSlot = isRequestSlot;
		this.timeOfOutput = timeOfOutput;
		this.anonNode = anonNode;
	}
	
	
	public boolean isUsedBy(User user) {
		synchronized (this) {
			return messagesToSend.containsKey(user);
		}
	}
	
	
	public void addMessage(MixMessage mixMessage) {
		synchronized (this) {
			messagesToSend.put(mixMessage.getOwner(), mixMessage);
		}
	}
	
	
	public void putOutMessages() {
		// create dummies if needed
		int normalMessages;
		int dummyCounter = 0;
		synchronized (this) { // TODO: remove
			normalMessages = messagesToSend.size();
			for (User user: userDatabase.getAllUsers()) {
				MixMessage mixMessage = messagesToSend.get(user);
				if (mixMessage == null) {
					if (isRequestSlot)
						mixMessage = recodingScheme.generateDummy(user);
					else 
						mixMessage = recodingScheme.generateDummyReply(user);
					dummyCounter++;
				}
				messagesToSend.put(user, mixMessage);
			}
			System.out.println("putting out slot (" +dummyCounter +" dummies and " +normalMessages +" normal messages)"); // TODO: remove 
			MixMessage[] messages = messagesToSend.values().toArray(new MixMessage[0]);
			Arrays.sort(messages);
			// send messages
			if (isRequestSlot)
				anonNode.putOutRequests((Request[])messages);
			else
				anonNode.putOutReplies((Reply[])messages);
			messagesToSend.clear();
		}
	}
	

	public int getNumerOfMessagesContained() {
		synchronized (this) {
			return messagesToSend.size();
		}
	}


	public long getTimeOfOutput() {
		synchronized (this) {
			return timeOfOutput;
		}
	}
	
}

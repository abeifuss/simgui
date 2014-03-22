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
package evaluation.loadGenerator.traceBasedTraffic;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import evaluation.loadGenerator.scheduler.ScheduleTarget;
import evaluation.loadGenerator.scheduler.ThreadPoolScheduler;
import evaluation.loadGenerator.traceBasedTraffic.event.Event;
import evaluation.loadGenerator.traceBasedTraffic.event.ReplayNextFlowEvent;
import evaluation.loadGenerator.traceBasedTraffic.event.IncomingReplyEvent;
import evaluation.loadGenerator.traceBasedTraffic.event.ReplayNextTransactionEvent;
import evaluation.simulator.plugins.trafficSource.ActiveFlow;
import evaluation.traceParser.engine.dataStructure.Flow;
import evaluation.traceParser.engine.dataStructure.Flow.Restriction;
import evaluation.traceParser.engine.dataStructure.Transaction;
import evaluation.traceParser.engine.fileReader.FlowGroupFlowIterator;
import evaluation.traceParser.engine.fileReader.FlowReader;
import framework.core.logger.OutputCap;
import framework.core.socket.socketInterfaces.StreamAnonSocket;
import framework.core.util.Util;


public class RaFM_TraceReplayClient implements ScheduleTarget<Event> {
	
	private int clientId;
	private boolean isDuplex;
	private final boolean DEBUG_ON;
	private FlowSource flowSource;
	private int idOfFirstFlowOfCurrentFlowGroup = 0;
	private int idOfLatestStartedFlow = Util.NOT_SET;
	private HashSet<Integer> finishedFlows;
	private HashMap<Integer, ActiveFlow> activeFlowsByFlowId = new HashMap<Integer, ActiveFlow>();
	private HashMap<Integer, ActiveFlow> activeFlowsByTransactionId = new HashMap<Integer, ActiveFlow>();
	private OutputCap noMoreFlowsMessage = new OutputCap(this.toString() +": no more flows to schedule", Long.MAX_VALUE);
	private boolean noMoreFlowsToSchedule = false;
	private RaFM_LoadGenerator owner;
	private ThreadPoolScheduler<Event> scheduler;
	private StreamAnonSocket socket;
	private BufferedOutputStream outputStream;
	
	
	
	public RaFM_TraceReplayClient(FlowReader trace, int clientId, boolean isDuplex, boolean debugOn) {
		this.clientId = clientId;
		this.isDuplex = isDuplex;
		this.DEBUG_ON = debugOn;
		this.flowSource = new FlowSource(trace);
	}

	
	public synchronized void startSending(RaFM_LoadGenerator owner, ThreadPoolScheduler<Event> scheduler, StreamAnonSocket socket) {
		this.owner = owner;
		this.scheduler = scheduler;
		this.socket = socket;
		try {
			this.outputStream = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		tryScheduleNextFlows();
	}
	
	
	// called when a reply was received (by RaFM_LoadGenerator)
	public synchronized void incomingReply(int transactionId) {
		if (DEBUG_ON)
		    System.out.println("Client " +clientId +": received reply for transaction " +transactionId); 
		ActiveFlow relatedFlow = activeFlowsByTransactionId.get(transactionId);
		relatedFlow.replyReceived();
		tryScheduleNextTransaction(relatedFlow);
		if (!isDuplex) {  // simplex simulation -> schedule arrival of next reply (-> assume replies arrive as in original trace)
			Transaction transaction = relatedFlow.getCurrentTransaction();
			if (transaction.hasMoreDistinctSimplexWithFeedbackReplyDelays()) {
				IncomingReplyEvent replyEvent = new IncomingReplyEvent(transaction.getTransactionId());
				scheduler.executeIn(transaction.getNextDistinctSimplexWithFeedbackReplyDelay(), this, replyEvent);
			}
		}
	}
	
	
	private void tryScheduleNextTransaction(ActiveFlow activeFlow) {
		if (activeFlow.allRepliesForCurrentTransactionReceived()) {
			if (activeFlow.getCurrentTransaction() != null)
				activeFlowsByTransactionId.remove(activeFlow.getCurrentTransaction().getTransactionId());
			if (activeFlow.hasNextTransaction()) { // next transaction available -> schedule its send
				Transaction nextTransaction = activeFlow.getNextTransaction();
				activeFlowsByTransactionId.put(nextTransaction.getTransactionId(), activeFlow);
				ReplayNextTransactionEvent replayNextTransactionEvent = new ReplayNextTransactionEvent(activeFlow);
				scheduler.executeIn(TimeUnit.MILLISECONDS.toNanos(nextTransaction.getSendDelay()), this, replayNextTransactionEvent);
			} else { // no next transaction (end of flow)
				finishedFlows.add(activeFlow.getFlow().flowId);
				activeFlowsByFlowId.remove(activeFlow.getFlow().flowId); 
				if (noMoreFlowsToSchedule && activeFlowsByFlowId.size() == 0) { // nothing left to schedule and no more active flows -> end simulation
					owner.voteForStop(socket);
				}
			}
		}
		tryScheduleNextFlows();
	}
	
	
	private void tryScheduleNextFlows() {
		while (flowSource.peekNextFlow() != null) { 
			if (flowSource.peekNextFlow().restriction == Restriction.NONE) { // schedule none restricted flow
				Flow nextFlow = flowSource.readNextFlow();
				ReplayNextFlowEvent replayNextFlowEvent = new ReplayNextFlowEvent(nextFlow); // Note: we do not call "replayFlow(nextFlow)" directly since the order of actions should not be changed
				scheduler.executeIn(1l, this, replayNextFlowEvent);
			} else if (flowSource.peekNextFlow().restriction == Restriction.SIMPLE_DELAY) {
				Flow nextFlow = flowSource.readNextFlow();
				ReplayNextFlowEvent replayNextFlowEvent = new ReplayNextFlowEvent(nextFlow); // Note: we do not call "replayFlow(nextFlow)" directly since the order of actions should not be changed
				scheduler.executeIn(TimeUnit.MILLISECONDS.toNanos(nextFlow.offsetFromRestriction), this, replayNextFlowEvent);
			} else if (flowSource.peekNextFlow().restriction == Restriction.NOT_BEFORE_END_OF_OTHER_FLOW) {
				if (flowSource.peekNextFlow().idOfRestrictingFlow < idOfFirstFlowOfCurrentFlowGroup || finishedFlows.contains(flowSource.peekNextFlow().idOfRestrictingFlow)) { // no more blocked (restricting flow is finished now)
					Flow nextFlow = flowSource.readNextFlow();
					ReplayNextFlowEvent replayNextFlowEvent = new ReplayNextFlowEvent(nextFlow); // Note: we do not call "replayFlow(nextFlow)" directly since the order of actions should not be changed
					scheduler.executeIn(TimeUnit.MILLISECONDS.toNanos(nextFlow.offsetFromRestriction), this, replayNextFlowEvent);
				} else { // next flow is blocked
					break;
				}
			} else if (flowSource.peekNextFlow().restriction == Restriction.NOT_BEFORE_END_OF_TRANSACTION) {
				int idOfRestrictingFlow = flowSource.peekNextFlow().idOfRestrictingFlow;
				int idOfRestrictingTransaction = flowSource.peekNextFlow().idOfRestrictingTransaction;
				ActiveFlow restrictingFlow = activeFlowsByFlowId.get(idOfRestrictingFlow);
				if (	(restrictingFlow == null && idOfLatestStartedFlow >= idOfRestrictingFlow) // restricting flow is finished already
						|| (restrictingFlow != null && idOfRestrictingTransaction < restrictingFlow.getArrayOffsetOfCurrentTransaction()) // restricting transaction is already finished
						|| (restrictingFlow != null && (idOfRestrictingTransaction == restrictingFlow.getArrayOffsetOfCurrentTransaction() // restricting transaction is currently being replayed
							&& flowSource.peekNextFlow().idOfRestrictingReply <= restrictingFlow.getIdOfLatestFinishedReply())) // the restricting reply of the restricting transaction is already replayed
					) { // restriction no longer given
					Flow nextFlow = flowSource.readNextFlow();
					ReplayNextFlowEvent replayNextFlowEvent = new ReplayNextFlowEvent(nextFlow); // Note: we do not call "replayFlow(nextFlow)" directly since the order of actions should not be changed
					scheduler.executeIn(TimeUnit.MILLISECONDS.toNanos(nextFlow.offsetFromRestriction), this, replayNextFlowEvent);
				} else { // next flow is blocked
					break;
				}
			} else {
				throw new RuntimeException("no handler implemented for restriction " +flowSource.peekNextFlow().restriction); 
			}
		}
		if (flowSource.peekNextFlow() == null) { // no more flows to schedule
			noMoreFlowsMessage.putOut();
			noMoreFlowsToSchedule = true;
		}
	}

	
	private void replayFlow(Flow flow) {
		ActiveFlow activeFlow = new ActiveFlow(flow);
		activeFlowsByFlowId.put(flow.flowId, activeFlow);
		idOfLatestStartedFlow = flow.flowId;
		assert activeFlow.hasNextTransaction();
		tryScheduleNextTransaction(activeFlow);
	}
	
	
	private void replayTransaction(ActiveFlow activeFlow) {
		assert activeFlow.getCurrentTransaction() != null;
		Transaction transaction = activeFlow.getCurrentTransaction();
		transaction.attachment = activeFlow;
		if (transaction.getRequestSize() == 0) // -> server shall send first message
			transaction.setRequestSize(16); // we assume that the client must first send a message to the final mix to open a socket that can receive data from the server (e.g. socks 5 bind command) 
		byte[] message = transaction.createSendableTransaction(clientId);
		try {
			outputStream.write(message);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			owner.stopSimulation(e.getLocalizedMessage());
		}
		if (!isDuplex) { // simplex simulation -> schedule arrival of next reply (-> assume replies arrive as in original trace)
			if (transaction.containsReplies()) {
				IncomingReplyEvent replyEvent = new IncomingReplyEvent(transaction.getTransactionId());
				scheduler.executeIn(transaction.getNextDistinctSimplexWithFeedbackReplyDelay(), this, replyEvent);
            } else { // no reply
				// nothing to do
			}
		}
		if (!transaction.containsReplies()) { // try to schedule next transaction if we do not have to wait for a reply from the server
			tryScheduleNextTransaction(activeFlow);
		}
	}
	
	
	// called by scheduler
	@Override
	public synchronized void execute(Event event) {
		if (event instanceof ReplayNextFlowEvent) {
			replayFlow(((ReplayNextFlowEvent)event).nextFlow);
		} else if (event instanceof ReplayNextTransactionEvent) {
			replayTransaction(((ReplayNextTransactionEvent)event).correspondingFlow);
		} else if (event instanceof IncomingReplyEvent) { // SIMPLEX ONLY
			incomingReply(((IncomingReplyEvent)event).transactionID);
		} else {
			throw new RuntimeException("ERROR: received unknown Event: " +event.toString());
		}
	}
	
	
	private class FlowSource {
		
		private FlowReader trace;
		private FlowGroupFlowIterator currentFlowGroup;
		private Flow nextFlow = null;
		private boolean wasPeekNextFlowCalled = false;
		private boolean newFlowGroup = false;
		
		
		private FlowSource(FlowReader trace) {
			this.trace = trace;
		}
		
		
		private Flow peekNextFlow() {
			if (wasPeekNextFlowCalled)
				return nextFlow;
			try {
				wasPeekNextFlowCalled = true;
				if (nextFlow != null) {
					return nextFlow;
				} else {
					if (trace.peekNextFlow() == null || trace.peekNextFlow().senderId != clientId) { // no more flows for this client
						return null;
					} else { // more flows available
						if (currentFlowGroup == null || !currentFlowGroup.hasNext()) { // need next flow group
							currentFlowGroup = trace.getFlowGroupFlowIterator();
							newFlowGroup = true;
						}
						nextFlow = currentFlowGroup.next();
						return nextFlow;
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("could not read trace file " +e.getLocalizedMessage()); 
			}
		}
		
		
		private Flow readNextFlow() {
			if (!wasPeekNextFlowCalled)
				peekNextFlow();
			wasPeekNextFlowCalled = false;
			Flow result = nextFlow;
			if (newFlowGroup) {
				if (finishedFlows != null)
					finishedFlows.clear();
				finishedFlows = new HashSet<Integer>();
				idOfFirstFlowOfCurrentFlowGroup = result.flowId;
				newFlowGroup = false;
			}
			nextFlow = null;
			return result;
		}
		
	}


}
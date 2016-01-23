package com.dataeye.flume.runnable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClientConfigurationConstants;

import com.dataeye.flume.embedagent.LoadBalanceAnent;
import com.dataeye.flume.sdk.LoadBalanceClient;
import com.dateeye.flume.util.EventQueue;

public class ConsumeEvent implements Runnable {
	private boolean useAgent = false;
	private int batchSize = RpcClientConfigurationConstants.DEFAULT_BATCH_SIZE;
	private LoadBalanceClient lbClient = LoadBalanceClient.getInstance();
	private LoadBalanceAnent lbAgent = LoadBalanceAnent.getInstance();

	public ConsumeEvent(int batchSize) {
		this.batchSize = batchSize;
	}

	public boolean isUseAgent() {
		return useAgent;
	}

	private synchronized void setUseAgent(boolean useAgent) {
		this.useAgent = useAgent;
	}

	@Override
	public void run() {
		while (true) {
			List<Event> eventList = EventQueue.get(batchSize);
			if (useAgent) {
				try {
					lbAgent.putAll(eventList);
				} catch (EventDeliveryException e) {
					e.printStackTrace();
				}
			} else {
				try {
					lbClient.appendBatch(eventList);
				} catch (Exception e) {
					setUseAgent(true);
					try {
						lbAgent.putAll(eventList);
					} catch (EventDeliveryException e1) {
						e1.printStackTrace();
					}
				}
			}
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

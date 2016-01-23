package com.dataeye.flume.sdk;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;

public class LoadBalanceClient {
	private static final LoadBalanceClient instance = new LoadBalanceClient();
	private RpcClient client;
	private Properties config = new Properties();
	private Lock lock = new ReentrantLock();

	private LoadBalanceClient() {
	}

	public static LoadBalanceClient getInstance() {
		return instance;
	}

	private void reconnectIfRequired() {
		if (client != null && !client.isActive()) {
			closeClient();
		}
		if (client == null) {
			lock.lock();
			try {
				client = RpcClientFactory.getInstance(config);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	private synchronized void closeClient() {
		if (client != null) {
			client.close();
		}
		client = null;
	}

	public synchronized void config(Properties config) {
		this.config = config;
		// 必填参数检查
		reconnectIfRequired();
	}

	public void appendBatch(List<Event> eventList) throws EventDeliveryException {
		reconnectIfRequired();
		client.appendBatch(eventList);
	}
}

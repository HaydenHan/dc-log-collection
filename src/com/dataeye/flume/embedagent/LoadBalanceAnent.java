package com.dataeye.flume.embedagent;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.agent.embedded.EmbeddedAgent;

public class LoadBalanceAnent {
	private static final EmbeddedAgent agent = new EmbeddedAgent("UsingFlume");
	private static final LoadBalanceAnent instance = new LoadBalanceAnent();

	private LoadBalanceAnent() {
	}

	public static LoadBalanceAnent getInstance() {
		return instance;
	}

	public void config(Properties config) {
		Map<String, String> configMap = new HashMap<String, String>();
		Enumeration enum1 = config.propertyNames();
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = config.getProperty(strKey);
			configMap.put(strKey, strValue);
		}
		agent.configure(configMap);
		agent.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				agent.stop();
			}
		}));
	}

	public void putAll(List<Event> eventList) throws EventDeliveryException {
		agent.putAll(eventList);
	}
}

package com.dataeye.flume.runnable;

import com.dateeye.flume.util.EventQueue;

public class EventQueueMonitor implements Runnable {

	@Override
	public void run() {
		while (true) {
			if (EventQueue.isSick()) {

			}
		}

	}

}

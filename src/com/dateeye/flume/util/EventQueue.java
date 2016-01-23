package com.dateeye.flume.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.flume.Event;

public class EventQueue {
	private static final int QUEUE_CAPACITY = 100000;
	private static BlockingQueue<Event> queue = new ArrayBlockingQueue<Event>(QUEUE_CAPACITY);

	public static void put(Event e) {
		try {
			queue.put(e);
		} catch (InterruptedException e1) {
			// 添加队列失败直接发送
			e1.printStackTrace();
		}
	}

	public static List<Event> get(int num) {
		List<Event> eventList = new ArrayList<Event>();
		if (!queue.isEmpty() && num > 0) {
			try {
				Event e = queue.take();
				eventList.add(e);
				num--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return eventList;
	}

	public static Event get() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isEmpty() {
		return queue.isEmpty();
	}

	public static boolean isSick() {
		//
		return false;
	}

	public static boolean isHeath() {
		//
		return true;
	}
}

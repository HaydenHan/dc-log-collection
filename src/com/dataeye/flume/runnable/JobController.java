package com.dataeye.flume.runnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobController {
	public static void init(int batchSize, int threadNum) {
		ExecutorService exec = Executors.newFixedThreadPool(threadNum);
		while (threadNum > 0) {
			exec.execute(new ConsumeEvent(batchSize));
			threadNum--;
		}
		exec.shutdown();
	}
}

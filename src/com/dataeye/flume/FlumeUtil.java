package com.dataeye.flume;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.flume.Event;
import org.apache.flume.api.RpcClientConfigurationConstants;
import org.apache.flume.event.EventBuilder;

import com.dataeye.flume.embedagent.LoadBalanceAnent;
import com.dataeye.flume.runnable.JobController;
import com.dataeye.flume.sdk.LoadBalanceClient;
import com.dateeye.flume.util.EventQueue;
import com.dateeye.flume.util.FlumeConfigurationConstants;
import com.dateeye.flume.util.StringUtil;

/**
 * <pre>
 * 供客户端调用的接口
 * @author Hayden<br>
 * @date 2016年1月21日 下午5:37:16
 * <br>
 */
public class FlumeUtil {
	private static LoadBalanceClient client = LoadBalanceClient.getInstance();
	private static LoadBalanceAnent agent = LoadBalanceAnent.getInstance();

	public static void init() {
	}

	/**
	 * <pre>
	 * 根据配置文件进行初始化
	 * @param clientPath
	 * @param agentPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author Hayden<br>
	 * @date 2016年1月22日 上午10:32:22
	 * <br>
	 */
	public static void init(String clientPath, String agentPath) throws FileNotFoundException, IOException {
		initEmbededAgent(agentPath);
		initClientSdk(clientPath);
	}

	public static void initClientSdk(String path) throws FileNotFoundException, IOException {
		Properties config = new Properties();
		config.load(new FileInputStream(path));
		client.config(config);
		int threadNum = StringUtil.convertInt(config.getProperty(FlumeConfigurationConstants.CONFIG_THREAD_NUM),
				FlumeConfigurationConstants.DEFAULT_THREAD_NUM);
		int batch_size = StringUtil.convertInt(config.getProperty(RpcClientConfigurationConstants.CONFIG_BATCH_SIZE),
				RpcClientConfigurationConstants.DEFAULT_BATCH_SIZE);
		JobController.init(threadNum, batch_size);
	}

	public static void initEmbededAgent(String path) throws FileNotFoundException, IOException {
		Properties config = new Properties();
		config.load(new FileInputStream(path));
		agent.config(config);
	}

	public static void push(Map<String, String> header, String body) {
		push(header, body.getBytes());
	}

	public static void push(Map<String, String> header, byte[] body) {
		pushEvent(EventBuilder.withBody(body, header));
	}

	public static void push(byte[] body) {
		pushEvent(EventBuilder.withBody(body));
	}

	public static void push(String body) {
		push(body.getBytes());
	}

	private static void pushEvent(Event e) {
		EventQueue.put(e);
	}
}

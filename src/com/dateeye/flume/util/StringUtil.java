package com.dateeye.flume.util;

public class StringUtil {
	public static int convertInt(String intStr, int defaultValue) {
		try {
			return Integer.valueOf(intStr).intValue();
		} catch (Throwable t1) {
			try {
				return Float.valueOf(intStr).intValue();
			} catch (Throwable t2) {
				return defaultValue;
			}
		}
	}
}

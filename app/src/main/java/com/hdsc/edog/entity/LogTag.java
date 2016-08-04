package com.hdsc.edog.entity;

import android.util.Log;

public class LogTag {

	private static boolean islog = true;

	public static void d(String tag, String log_content) {
		if (islog) {
			Log.d(tag, log_content);
		}
	}

	public static void i(String tag, String log_content) {
		if (islog) {
			Log.i(tag, log_content);
		}
	}

	public static void w(String tag, String log_content) {
		if (islog) {
			Log.w(tag, log_content);
		}
	}

	public static void e(String tag, String log_content) {
		if (islog) {
			Log.e(tag, log_content);
		}
	}

	public static void f(String tag, String log_content) {
		if (islog) {
			Log.e(tag, log_content);
		}
	}

	public static void netTimeLog(String method, long time) {
		StringBuffer sb = new StringBuffer();
		sb.append(method).append(":").append(time).append("**************");
		System.out.println(sb.toString());
	}
}

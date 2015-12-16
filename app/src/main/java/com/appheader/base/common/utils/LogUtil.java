package com.appheader.base.common.utils;

import android.util.Log;

import com.appheader.base.application.GlobalVars;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Logcat工具类
 * 
 * @author alaowan
 *
 */
public class LogUtil {

	public static void error(String tag, Throwable e) {
		if(GlobalVars.isDebug){
			Log.e(tag, ExceptionUtils.getFullStackTrace(e));
		}
	}
	
	public static void error(String tag, String errorMsg) {
		if (errorMsg == null)
			return;
		if(GlobalVars.isDebug){
			Log.e(tag, errorMsg);
		}
	}

	public static void info(String tag, String message) {
		if (message == null)
			return;
		if(GlobalVars.isDebug){
			Log.i(tag, message);
		}
	}

	public static void debug(String tag, String message) {
		if (message == null)
			return;
		if(GlobalVars.isDebug){
			Log.d(tag, message);
		}
	}

}

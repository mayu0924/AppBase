package com.appheader.base.common.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	
	private static SimpleDateFormat formatterYesterday = new SimpleDateFormat("昨天 HH:mm:ss");

	private static SimpleDateFormat formatterEarly = new SimpleDateFormat("MM月dd日 HH:mm:ss");

	private static SimpleDateFormat formatterToday = new SimpleDateFormat("HH:mm:ss");
	
	private static SimpleDateFormat formatterTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat formatterTime2 = new SimpleDateFormat("yyyy-MM");
	
	/**
	 * 生成时间显示字符串
	 * 
	 * @param msgTime
	 * @return
	 */
	public static String generateTimeStr(long msgTime) {
		String timeStr = null;
		Calendar calNow = Calendar.getInstance();
		Calendar calMsg = Calendar.getInstance();
		calMsg.setTimeInMillis(msgTime);
		// 当天的消息
		if (calNow.get(Calendar.DAY_OF_MONTH) == calMsg.get(Calendar.DAY_OF_MONTH)) {
			timeStr = formatterToday.format(new Date(msgTime));
		} else if (calNow.get(Calendar.DAY_OF_MONTH) - calMsg.get(Calendar.DAY_OF_MONTH) == 1) {
			// 前一天的消息
			timeStr = formatterYesterday.format(new Date(msgTime));
		} else {
			// 更早的消息
			timeStr = formatterEarly.format(new Date(msgTime));
		}
		return timeStr;
	}
	
	
	private static SimpleDateFormat formatterYesterday2 = new SimpleDateFormat("昨天");

	private static SimpleDateFormat formatterEarly2 = new SimpleDateFormat("MM月dd日");

	private static SimpleDateFormat formatterToday2 = new SimpleDateFormat("HH:mm");
	
	/**
	 * 生成时间显示字符串
	 * yyyy-MM-dd HH:mm:ss
	 * @param msgTime
	 * @return
	 */
	public static String generateTimeStr3(long msgTime) {
		String timeStr = null;
		Calendar calMsg = Calendar.getInstance();
		calMsg.setTimeInMillis(msgTime);
		timeStr = new SimpleDateFormat("yyyy-MM").format(new Date(msgTime));
		return timeStr;
	}
	
	public static String generateTimeStr4(long msgTime) {
		String timeStr = null;
		Calendar calMsg = Calendar.getInstance();
		calMsg.setTimeInMillis(msgTime);
		timeStr = new SimpleDateFormat("yyyy年MM月").format(new Date(msgTime));
		return timeStr;
	}
	
	/**
	 * 生成时间显示字符串
	 * yyyy-MM-dd HH:mm:ss
	 * @param msgTime
	 * @return
	 */
	public static String generateTimeStr2(String msgTimeStr) {
		long msgTime = getTimeMills(msgTimeStr);
		String timeStr = null;
		Calendar calNow = Calendar.getInstance();
		Calendar calMsg = Calendar.getInstance();
		calMsg.setTimeInMillis(msgTime);
		// 当天的消息
		if (calNow.get(Calendar.DAY_OF_MONTH) == calMsg.get(Calendar.DAY_OF_MONTH)) {
			timeStr = formatterToday2.format(new Date(msgTime));
		} else if (calNow.get(Calendar.DAY_OF_MONTH) - calMsg.get(Calendar.DAY_OF_MONTH) == 1) {
			// 前一天的消息
			timeStr = formatterYesterday2.format(new Date(msgTime));
		} else {
			// 更早的消息
			timeStr = formatterEarly2.format(new Date(msgTime));
		}
		return timeStr;
	}
	
	
	/**
	 * 获取该日期的毫秒值
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long getTimeMills(String dateStr) {
		long currDayMills = 0L;
			try {
				currDayMills = formatterTime.parse(dateStr).getTime();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
//		LogUtil.debug("获取该日期的毫秒值", dateStr + "  >>>>>>>  " + currDayMills);
		return currDayMills;
	}
	
	/**
	 * 获取该日期的毫秒值
	 * yyyy-MM
	 * @return
	 */
	public static long getTimeMills2(String dateStr) {
		long currDayMills = 0L;
			try {
				currDayMills = formatterTime2.parse(dateStr).getTime();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		return currDayMills;
	}
	
	public static String generatorTimeLengthForDisplay(int seconds){
		if(seconds < 60)
			return seconds + "\"";
		
		if(seconds % 60 == 0){
			return seconds/60 + "\"";
		}else{
			return ((seconds - seconds % 60)/60) + "'" + seconds%60 + "\"";
		}
	}

}

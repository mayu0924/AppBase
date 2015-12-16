package com.appheader.base.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.Toast;

import com.appheader.base.R;
import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.network.entity.ResponseEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Tools {

	public static <T> List<T> getLastElements(ArrayList<T> list, int nbElems) {
		return list.subList(Math.max(list.size() - nbElems, 0), list.size());
	}

	public static Long getLong(Cursor c, String col) {
		return c.getLong(c.getColumnIndex(col));
	}

	public static int getInt(Cursor c, String col) {
		return c.getInt(c.getColumnIndex(col));
	}

	public static String getString(Cursor c, String col) {
		return c.getString(c.getColumnIndex(col));
	}

	public static boolean getBoolean(Cursor c, String col) {
		return getInt(c, col) == 1;
	}

	public static Date getDateSeconds(Cursor c, String col) {
		return new Date(Long.parseLong(Tools.getString(c, col)) * 1000);
	}

	public static Date getDateMilliSeconds(Cursor c, String col) {
		return new Date(Long.parseLong(Tools.getString(c, col)));
	}

	public static boolean isInt(String value) {
		try {
			// modify by sun
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}

	public static Boolean parseBool(String value) {
		Boolean res = null;
		if (value.compareToIgnoreCase("true") == 0) {
			res = true;
		} else if (value.compareToIgnoreCase("false") == 0) {
			res = false;
		}
		return res;
	}

	public static Integer parseInt(String value) {
		Integer res = null;
		try {
			res = Integer.parseInt(value);
		} catch (Exception e) {
		}

		return res;
	}

	public static Integer parseInt(String value, Integer defaultValue) {
		Integer res = defaultValue;
		try {
			res = Integer.parseInt(value);
		} catch (Exception e) {
		}

		return res;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date parseDate(String dateStr) {
		try {
			String pattern = "yyyy-MM-dd HH:mm";
			if (dateStr.length() == 10)
				pattern = "yyyy-MM-dd";
			else if (dateStr.length() == 16)
				pattern = "yyyy-MM-dd HH:mm";
			else if (dateStr.length() == 19)
				pattern = "yyyy-MM-dd HH:mm:ss";

			return new SimpleDateFormat(pattern).parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDate(Date date) {
		return formatDateTime(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDateTime(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定范围内的随机整数
	 * 
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	public static int getRandomInt(int minVal, int maxVal) {
		Random rand = new Random();
		int tmp = Math.abs(rand.nextInt());
		return tmp % (maxVal - minVal + 1) + minVal;
	}

	public static int getMinNonNeg(int... x) {
		int min = Integer.MAX_VALUE;
		for (int i : x) {
			if (i >= 0 && i < min)
				min = i;
		}
		return min;
	}

	private static String getAppBaseDir(Context ctx) {
		String filesDir = ctx.getFilesDir().toString();
		int index = filesDir.indexOf("/files");
		return filesDir.substring(0, index);
	}

	public static String getSharedPrefDir(Context ctx) {
		return getAppBaseDir(ctx) + "/shared_prefs";
	}

	public static String shortenString(String s) {
		if (s.length() > 20) {
			return s.substring(0, 20);
		} else {
			return s;
		}
	}

	public static String getTextValue(EditText text) {
		return text.getText().toString();
	}

	/**
	 * 从ResponseEntity中提取错误信息
	 * 
	 * @param entity
	 * @return
	 */
	public static String getErrorMessage(ResponseEntity entity) {
		String msg = GlobalVars.getContext().getString(R.string.pub_network_error);
		if (entity != null && !TextUtils.isEmpty(entity.getErrorMessage()))
			msg = entity.getErrorMessage();
		return msg;
	}

	public static String getJsonString(JSONObject json, String key) {
		String value = "";
		if (json.has(key)) {
			try {
				value = json.getString(key);
			} catch (JSONException e) {
			}
		}
		return value;
	}

	public static int getJsonInt(JSONObject json, String key) {
		int value = 0;
		if (json.has(key)) {
			try {
				value = json.getInt(key);
			} catch (Exception e) {
			}
		}
		return value;
	}

	public static long getJsonLong(JSONObject json, String key) {
		long value = -1;
		if (json.has(key)) {
			try {
				value = json.getLong(key);
			} catch (Exception e) {
			}
		}
		return value;
	}

	public static float getJsonFloat(JSONObject json, String key) {
		float value = 0f;
		if (json.has(key)) {
			try {
				value = Float.parseFloat(json.getString(key));
			} catch (Exception e) {
			}
		}
		return value;
	}

	public static boolean getJsonBoolean(JSONObject json, String key) {
		return getJsonBoolean(json, key, false);
	}

	public static boolean getJsonBoolean(JSONObject json, String key, boolean defaultVal) {
		boolean value = defaultVal;
		if (json.has(key)) {
			try {
				value = json.getBoolean(key);
			} catch (Exception e) {
			}
		}
		return value;
	}

	public static JSONArray getJsonArray(JSONObject json, String key) {
		JSONArray value = null;
		if (json.has(key)) {
			try {
				value = json.getJSONArray(key);
			} catch (JSONException e) {
			}
		} else {
			value = new JSONArray();
		}
		return value;
	}

	public static JSONObject getJsonObject(JSONObject json, String key) {
		JSONObject result = null;
		if (json.has(key)) {
			try {
				result = json.getJSONObject(key);
			} catch (JSONException e) {
			}
		}
		return result;
	}

	public static JSONObject getJsonObject(JSONArray arr, int index) {
		JSONObject result = null;
		try {
			result = arr.getJSONObject(index);
		} catch (JSONException e) {
		}
		return result;
	}

	/**
	 * 根据当前时间来计算任务的剩余天数
	 * 
	 * @param endDate
	 * @return add by sun
	 */
	public static int getRemainDays(String endDate) {
		long millisDiff = Tools.parseDate(endDate).getTime() - System.currentTimeMillis();
		return getDayCountByMillis(millisDiff);
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static int getDayCount(String fromDate, String toDate) {
		long millisDiff = Tools.parseDate(toDate).getTime() - Tools.parseDate(fromDate).getTime();
		return getDayCountByMillis(millisDiff);
	}

	public static int getDayCount(String fromDate, long toDateMillis) {
		long millisDiff = toDateMillis - Tools.parseDate(fromDate).getTime();
		return getDayCountByMillis(millisDiff);
	}

	public static void showToast(String msg) {
		Toast.makeText(GlobalVars.getContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public static void showNetworkError() {
		Toast.makeText(GlobalVars.getContext(), R.string.pub_network_error, Toast.LENGTH_SHORT).show();
	}

	public static boolean processNetworkResponse(ResponseEntity response) {
		if (response == null) {
			showNetworkError();
			return false;
		} else {
			if (!response.isSuccess()) {
				showToast(getErrorMessage(response));
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据时间差（毫秒）计算相差天数
	 * 
	 * @param timeDiff
	 * @return
	 */
	private static int getDayCountByMillis(long timeDiff) {
		int result = 0;
		int c = timeDiff < 0 ? -1 : 1;
		long millisOfDay = (24 * 1000 * 3600);
		// 相差不到24小时
		if (Math.abs(timeDiff) < millisOfDay) {
			result = 1; // 不足24小时算一天
		} else {
			result = (int) (Math.abs(timeDiff) / millisOfDay) + (Math.abs(timeDiff) % millisOfDay > 0 ? 1 : 0);
		}
		return result * c;
	}

	/**
	 * 转换手机号码格式，如果返回null，则不是手机号
	 * 
	 * @param str
	 * @return
	 */
	public static String getAvailableMobile(String str) {
		// 先去掉空格
		str = str.replace(" ", "");
		if (str.startsWith("+86"))
			str = str.substring(3);
		// 如果是11为数字，直接返回
		if (str.length() == 11 && TextUtils.isDigitsOnly(str) && str.startsWith("1"))
			return str;

		return null;
	}

	/**
	 * 判断是否是11位手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAvailableMobile(String str) {
		return str != null && str.length() == 11 && TextUtils.isDigitsOnly(str) && str.startsWith("1");
	}
	
	public static int getScreenWidth(Activity activity){
		return getDisplayMetrix(activity).widthPixels;
	}
	
	public static int getScreenHeight(Activity activity){
		return getDisplayMetrix(activity).heightPixels;
	}
	
	public static DisplayMetrics getDisplayMetrix(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

}

package com.appheader.base.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * json解析工具
 * @author mayu
 *
 */
public class GsonUtil {
	
	/**
	 * 解析JsonArray,默认【name = list】
	 * @param clazz
	 * @param json
	 * @return
	 */
	public static <T, clazz> List<T> getListData(Class<T> clazz, JSONObject json){
		return getListData(clazz, "list", json);
	}
	
	/**
	 * 解析JsonArray,【name = key】
	 * @param clazz
	 * @param key
	 * @param json
	 * @return
	 */
	public static <T, clazz> List<T> getListData(Class<T> clazz, String key, JSONObject json){
		Type listType = new TypeToken<List<clazz>>() {}.getType();
		return new Gson().fromJson(json.optJSONArray(key).toString(), listType);
	}
	
	/**
	 * 解析JsonObject
	 * @param <clazz>
	 * @param json
	 * @return
	 */
	public static <T, clazz> T getData(Class<T> clazz, JSONObject json){
		Type listType = new TypeToken<clazz>() {}.getType();
		return new Gson().fromJson(json.toString(), listType);
	}
}

package com.mayu.tiangou.common.network.entity;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 参数实体
 * @author mayu
 */
public class RequestParam {

	private HashMap<String, String> map = new HashMap<String, String>();

	public HashMap<String, String> toHashMap() {
		return map;
	}

	public RequestParam append(String key, String value) {
		map.put(key, value);
		return this;
	}

	/**
	 *
	 * @return 参数的json格式
	 */
	public JSONObject toJson(){
		JSONObject json = new JSONObject(map);
		return json;
	}

}

package com.mayu.tiangou.common.network;


import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.mayu.tiangou.common.util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by mayu on 15/8/25,上午11:28.
 */
public class RequestHelper {
    private static final String TAG = "Volley";
    private static RequestQueue mRequestQueue;

    private static Context mCtx;

    /**
     * 初始化
     * @param ctx
     */
    public static void init(Context ctx) {
        mCtx = ctx;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        mRequestQueue.start();
    }

    /**
     * 发送请求
     * @param tag 			【标记：用来表示当前请求】
     * @param url
     * @param params
     * @param success
     * @param failed
     */
    public static <T> void sendRequest(String tag, String url, Map<String, String> params, Response.Listener<JSONObject> success, Response.ErrorListener failed) {
        logParams(url, params);
        NormalPostRequest request = new NormalPostRequest(url, params, success, failed);
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(request);
    }

    public static <T> void sendBodyRequest(String tag, String url, String params, Response.Listener<JSONObject> success, Response.ErrorListener failed) {
        NormalPostRequest request = new NormalPostRequest(url, params, success, failed);
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(request);
    }

    public static <T> void sendGetRequest(String tag, String url, Map<String, String> params, Response.Listener<JSONObject> success, Response.ErrorListener failed) {
        logParams(url, params);
        NormalGetRequest request = new NormalGetRequest(url, params, success, failed);
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,0));
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(request);
    }

    public static <T> void sendJsonRequest(String tag, String url, JSONObject json, Response.Listener<JSONObject> success, Response.ErrorListener failed) {
        L.d(tag, "json/param------\n"+json.toString());
        JsonObjectRequestWithCookie requestWithCookie = new JsonObjectRequestWithCookie(url, json, success, failed);
        requestWithCookie.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(requestWithCookie);
    }

    /**
     * 取消请求
     * @param tag
     */
    public static void cancel(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 清理缓存
     */
    public static void clearCache(){
        mRequestQueue.getCache().clear();
    }

    /**
     * 获取缓存
     * @param url
     * @return
     */
    public static JSONObject getCache(String url){
        Cache cache = mRequestQueue.getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                try {
					return new JSONObject(data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            return null;
        }
       return null;
    }

    /**
     * 打印【请求地址、参数、标签】
     * @param url
     * @param param
     */
    public static void logParams(String url, Map<String, String> param){
        L.d("VolleyHelper", "【RequestURL:】\n" + url
                + "\n【RequestParamStr:】\n" + ((param == null) ? "参数为空" : param.toString()));
    }
}

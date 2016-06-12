package com.mayu.tiangou.common.network;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayu on 15/8/25,下午3:22.
 */
public class NormalPostRequest extends Request<JSONObject> {
    public static volatile String cookies;
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "ASP.NET_SessionId";
    private Map<String, String> mMap;
    private String mUrl;
    private String mBody;
    private Response.Listener<JSONObject> mListener;

    public NormalPostRequest(String url, Map<String, String> map, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        mListener = listener;
        mMap = map;
    }

    public NormalPostRequest(String url, String body, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        mUrl = url;
        mListener = listener;
        mBody = body;
    }

    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (!TextUtils.isEmpty(mBody)) {
            return mBody.getBytes();
        } else {
            return super.getBody();
        }
    }

    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        Map<String, String> responseHeaders = response.headers;
//        String rawCookies = responseHeaders.get(SET_COOKIE_KEY);
//        if(!TextUtils.isEmpty(rawCookies)){
//            cookies = rawCookies.substring(0, rawCookies.indexOf(";"));
//        }
        // String cookies = part1;
//        Log.d("sessionid", "sessionid----------------" + cookies);
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        super.getHeaders();
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        Log.d("调试", "headers----------------" + headers);
        return headers;
    }

}

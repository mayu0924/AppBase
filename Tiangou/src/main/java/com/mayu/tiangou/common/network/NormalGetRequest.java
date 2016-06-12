package com.mayu.tiangou.common.network;

import android.text.TextUtils;

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
 * Created by zhn on 16/3/22,21:10.
 */
public class NormalGetRequest extends Request<JSONObject> {
    private Map<String, String> mMap;
    private String mUrl;
    private String mBody;
    private Response.Listener<JSONObject> mListener;

    public NormalGetRequest(String url, Map<String, String> map, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
        mMap = map;
    }

    public NormalGetRequest(String url, String body, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mUrl = url;
        mListener = listener;
        mBody = body;
    }

    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(mMap == null){
            mMap = new HashMap<String, String>();
        }
        return mMap;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if(!TextUtils.isEmpty(mBody)){
            return mBody.getBytes();
        } else {
            return super.getBody();
        }
    }

    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
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
        headers.put("platform", "android");
        headers.put("Accept-Charset", "UTF-8");
//        headers.put("versionCode", String.valueOf(GlobalVars.getVersionCode()));
//        headers.put("clientType", "customer");
//        UserInfo userInfo = CurrentUserManager.getCurrentUser();
//        if (userInfo != null) {
//            headers.put("session_id", userInfo.getSession_id());
//            LogUtil.debug("NormalPostRequest", "session_id = " + userInfo.getSession_id());
//        }
        return headers;
    }
}

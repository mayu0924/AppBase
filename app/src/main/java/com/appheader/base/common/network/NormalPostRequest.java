package com.appheader.base.common.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.user.UserInfo;
import com.appheader.base.common.utils.LogUtil;

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
    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;

    public NormalPostRequest(String url, Map<String, String> map, Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        mListener = listener;
        mMap = map;
    }

    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
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
        headers.put("versionCode", String.valueOf(GlobalVars.getVersionCode()));
        headers.put("clientType", "customer");
        UserInfo userInfo = CurrentUserManager.getCurrentUser();
        if (userInfo != null) {
        	headers.put("session_id", userInfo.getSession_id());
            LogUtil.debug("NormalPostRequest", "session_id = " + userInfo.getSession_id());
        }
        return headers;
    }
}

package com.appheader.base.common.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.user.UserInfo;
import com.appheader.base.common.utils.LogUtil;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestWithCookie extends JsonObjectRequest {
    private Map<String, String> mHeaders = new HashMap<>();
    public JsonObjectRequestWithCookie(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String,String> map) {
        super(Method.POST, url, jsonRequest, listener, errorListener);
 
    }
    public JsonObjectRequestWithCookie(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(url, jsonRequest, listener, errorListener);
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    //拿到客户端发起的http请求的Header
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
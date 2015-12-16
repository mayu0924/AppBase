package com.appheader.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appheader.base.common.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * JPush消息推送
 * 
 * @author mayu
 * 
 */
public class PushReceiver extends BroadcastReceiver {

	static final String TAG = PushReceiver.class.getName();

	/** 客户端从另一设备登陆后，给该用户名推送session注销消息 */
	static final String PUSH_TYPE_SESSION_EXPIRED = "session_expired";

	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (mContext == null)
			mContext = context;
		/** 收到新的通知 */
		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			String extraObjStr = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
			LogUtil.debug(TAG, extraObjStr);
			saveMessageByType(extraObjStr);
			/** 点击通知栏 */
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			String extraObjStr = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
			LogUtil.debug(TAG, extraObjStr);
			processMessageByType(extraObjStr);
		}
	}
	
	private void saveMessageByType(String extraObjStr) {
		String type = "";
		JSONObject notifiyObj = null;
		try {
			notifiyObj = new JSONObject(extraObjStr);
			type = notifiyObj.optString("type");
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		switch (type) {
		case "define_type":
			// TODO 根据类型处理消息
			break;
		default:
			break;
		}
	}

	private void processMessageByType(String extraObjStr) {
		String type = "";
		JSONObject notifiyObj = null;
		try {
			notifiyObj = new JSONObject(extraObjStr);
			type = notifiyObj.optString("type");
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		switch (type) {
		case "define_type":
			// TODO 根据类型处理消息
			break;
		default:
			break;
		}
	}
}

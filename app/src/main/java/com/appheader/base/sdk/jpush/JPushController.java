package com.appheader.base.sdk.jpush;

import android.content.Context;

import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.utils.LogUtil;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * JPush注册管理类
 * 
 * @author mayu
 * 
 */
public class JPushController {
	private static final String TAG = JPushController.class.getSimpleName();

	/**
	 * 切换JPush激活状态
	 * @param ctx
	 */
	public static void toggle(Context ctx){
		if (CurrentUserManager.getCurrentUser() != null) {
			if(JPushInterface.isPushStopped(ctx)){
				JPushInterface.init(ctx);
			}
			JPushInterface.resumePush(ctx);
			JPushController.register(ctx, null);
		} else if(!JPushInterface.isPushStopped(ctx)){
			JPushController.unRegister(ctx);
			JPushInterface.stopPush(ctx);
		}
	}
	
	/**
	 * 注册JPush
	 * 
	 * @param ctx
	 */
	public static void register(Context ctx, Set<String> set) {
		JPushInterface.setDebugMode(GlobalVars.isDebug);
		JPushInterface.resumePush(ctx);
		JPushInterface.setAliasAndTags(ctx, CurrentUserManager.getCurrentUser().getUid(), set, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				System.out.println(">>>>>>> Jpush gotResult : " + "arg0:" + arg0 + "\narg1:" + arg1 + "\narg2:" + arg2);
			}
		});
	}

	/**
	 * 注销JPush
	 * 
	 * @param ctx
	 */
	public static void unRegister(final Context ctx) {
		JPushInterface.setAliasAndTags(ctx, null, null, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				LogUtil.debug(TAG, "arg0:" + arg0 + "\narg1:" + arg1 + "\narg2:" + arg2);
				JPushInterface.stopPush(ctx);
			}
		});
	}

	/**
	 * 移除所有标签
	 * @param ctx
	 */
	public static void removeAllTag(Context ctx) {
		setTag(ctx, new HashSet<String>());
	}

	/**
	 * 设置标签
	 * 
	 * @param ctx
	 * @param tag
	 */
	public static void setTag(Context ctx, Set<String> tags) {
		JPushInterface.setTags(ctx, tags, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				System.out.println(">>>>>>> Jpush gotResult : " + "arg0:" + arg0 + "\narg1:" + arg1 + "\narg2:" + arg2);
			}
		});
	}
}

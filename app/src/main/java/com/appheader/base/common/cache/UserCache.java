package com.appheader.base.common.cache;

import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.user.CurrentUserManager;

import java.io.File;

/**
 * 用户个人信息和私有数据缓存
 * 
 * @author alaowan
 * 
 */
public class UserCache extends BaseCache {

	private String mCurrentUserId;

	public UserCache() {
		mCurrentUserId = CurrentUserManager.getCurrentUser().getUid();
	}

	public String getUserId() {
		return mCurrentUserId;
	}

	@Override
	protected String getCacheDir() {
		return GlobalVars.getAppFilesDir() + File.separator + "userprofile" + File.separator + mCurrentUserId;
	}

	@Override
	protected String getTAG() {
		return "UserCache";
	}

}

package com.appheader.base.common.user;

import com.appheader.base.common.cache.CacheManager;

/**
 * 当前登录用户信息管理
 * 
 * @author alaowan
 * 
 */
public class CurrentUserManager {

	public static final String CACHE_KEY_CURRENT_USER = "current_user";

	/**
	 * 清除当前登录用户（退出登录时调用）
	 */
	public static void clearCurrentUser() {
		CacheManager.getGlobalCache().remove(CACHE_KEY_CURRENT_USER);
	}

	/**
	 * 设置当前登录用户（登录成功后调用）
	 * 
	 * @param sessionId
	 * @param userId
	 * @return
	 */
	public static UserInfo setCurrentUser(UserInfo user) {
		CacheManager.getGlobalCache().putCache(CACHE_KEY_CURRENT_USER, user);
		return user;
	}

	/**
	 * 获取当前登录用户信息
	 * 
	 * @return
	 */
	public static UserInfo getCurrentUser() {
		return (UserInfo) CacheManager.getGlobalCache().getCache(CACHE_KEY_CURRENT_USER);
	}

}

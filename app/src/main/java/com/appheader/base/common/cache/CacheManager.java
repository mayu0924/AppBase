package com.appheader.base.common.cache;

import com.appheader.base.common.user.CurrentUserManager;


public class CacheManager {

	private static GlobalCache sGlobalCache;
	private static PublicDataCache sPublicDataCache;
	private static UserCache sUserCache;

	public static GlobalCache getGlobalCache() {
		if (sGlobalCache == null) {
			sGlobalCache = new GlobalCache();
			sGlobalCache.init();
		}
		return sGlobalCache;
	}

	public static PublicDataCache getPublicDataCache() {
		if (sPublicDataCache == null) {
			sPublicDataCache = new PublicDataCache();
			sPublicDataCache.init();
		}
		return sPublicDataCache;
	}

	public static UserCache getUserCache() {
		if (sUserCache == null) {
			sUserCache = new UserCache();
			sUserCache.init();
		} else {
			if (!sUserCache.getUserId().equals(CurrentUserManager.getCurrentUser().getUid())) {
				sUserCache.close();
				sUserCache = null;
				sUserCache = new UserCache();
				sUserCache.init();
			}
		}
		return sUserCache;
	}
}

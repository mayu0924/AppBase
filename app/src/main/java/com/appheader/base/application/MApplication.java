package com.appheader.base.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.appheader.base.common.network.RequestHelper;
import com.appheader.base.common.orm.Constants;
import com.appheader.base.common.utils.ImageLoadUtil;
import com.appheader.base.sdk.jpush.JPushController;
import com.appheader.db.DaoMaster;
import com.appheader.db.DaoSession;
import com.orhanobut.logger.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Application
 * @author mayu
 */
public class MApplication extends Application {
	private static TelephonyManager sTelephonyManager;
	private static ActivityManager sActivityManager;

	private static MApplication mInstance;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable t) {
                t.printStackTrace();
                System.exit(0);
            }
        });

        Logger.init("AppBase");

		if(mInstance == null)
			mInstance = this;

		sTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		GlobalVars.init(this);
		ImageLoadUtil.initialize(this);
		RequestHelper.init(this);
		// 需要登录权限
		JPushController.toggle(this);
	}

	/**
	 * 取得DaoMaster
	 *
	 * @param context
	 * @return
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, Constants.DB_NAME, null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * 取得DaoSession
	 *
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

	public static TelephonyManager getTelephonyManager() {
		return sTelephonyManager;
	}

	public static ActivityManager getActivityManager() {
		return sActivityManager;
	}
}

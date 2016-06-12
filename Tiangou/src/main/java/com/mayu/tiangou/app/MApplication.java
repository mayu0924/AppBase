package com.mayu.tiangou.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.mayu.tiangou.common.glide.GlideManager;
import com.mayu.tiangou.common.network.RequestHelper;
import com.orhanobut.logger.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Application
 * @author mayu
 */
public class MApplication extends Application {
	private static TelephonyManager sTelephonyManager;
	private static ActivityManager sActivityManager;

	public static MApplication mInstance;
	public GlideManager sGlideManager;
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

        Logger.init("TG");

		if(mInstance == null)
			mInstance = this;

		sTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		GlobalVars.init(this);
		RequestHelper.init(this);

		sGlideManager = new GlideManager(this);
	}

	public static TelephonyManager getTelephonyManager() {
		return sTelephonyManager;
	}

	public static ActivityManager getActivityManager() {
		return sActivityManager;
	}
}

package com.mayu.tiangou.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 应用全局变量
 * 
 * @author alaowan
 * 
 */
public class GlobalVars {
	private static Context sContext;
	// 是否使用本地模拟数据
	public static boolean useSimulateData;

	private static float sDensity = -1;
	private static int sScreenWidth = -1;
	private static int sScreenHeight = -1;

	public static void init(Context c) {
		sContext = c;
	}

	public static Context getContext() {
		return sContext;
	}


	/**
	 * 获取应用私有目录
	 * 
	 * @return
	 */
	@SuppressLint("SdCardPath")
	public static String getAppFilesDir() {
		return "/data/data/" + sContext.getPackageName();
	}

	public static int getVersionCode() {
		return getPackageInfo().versionCode;
	}

	public static String getVersionName() {
		return getPackageInfo().versionName;
	}

	/**
	 * 获取应用全局设置的preference
	 */
	public static SharedPreferences getSettingsPref() {
		return sContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
	}

	/**
	 * 保存屏幕属性
	 * 
	 * @param density
	 * @param screenWidth
	 * @param screenHeight
	 */
	public static void saveScreenProps(float density, int screenWidth, int screenHeight) {
		SharedPreferences pref = getSettingsPref();
		Editor editor = pref.edit();
		editor.putFloat("density", density);
		editor.putInt("screen_width", screenWidth);
		editor.putInt("screen_height", screenHeight);
		editor.commit();
	}

	public static float getDensity() {
		if (sDensity == -1)
			sDensity = getSettingsPref().getFloat("density", -1);
		return sDensity;
	}

	public static int getScreenWidth() {
		if (sScreenWidth == -1)
			sScreenWidth = getSettingsPref().getInt("screen_width", -1);
		return sScreenWidth;
	}

	public static int getScreenHeight() {
		if (sScreenHeight == -1)
			sScreenHeight = getSettingsPref().getInt("screen_height", -1);
		return sScreenHeight;
	}

	public static PackageInfo getPackageInfo() {
		PackageManager pm = sContext.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(sContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
		}
		return info;
	}
	
	//获取手机状态栏高度
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}

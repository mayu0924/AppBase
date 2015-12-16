package com.appheader.base.common.utils;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;

import com.appheader.base.application.GlobalVars;

import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings("deprecation")
public class WakeLockUtil {
	
	private static WakeLockUtil instance;
	private PowerManager.WakeLock mWakeLock;
	private final long SLEEP_TIME = 10 * 1000;
	private Timer timer;
	
	public static WakeLockUtil getInstance() {
		if (instance == null)
			instance = new WakeLockUtil();
		return instance;
	}
	
	/**
	 * 保持屏幕常亮
	 */
	public void acquire() {
		disableKeyguard();
		PowerManager pm = (PowerManager) GlobalVars.getContext().getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
		mWakeLock.acquire();
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				release();
			}
		}, SLEEP_TIME);
	}
	
	/**
	 * 释放屏幕常亮锁
	 */
	public void release() {
		if(null != mWakeLock && mWakeLock.isHeld()) {
		    mWakeLock.release();
		}
	}
	
	/**
	 * 屏幕解锁
	 */
	public static void disableKeyguard() {
		KeyguardManager km = (KeyguardManager)GlobalVars.getContext().getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock lock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
		lock.disableKeyguard();
	}
	
}

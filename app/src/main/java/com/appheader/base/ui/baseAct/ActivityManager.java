package com.appheader.base.ui.baseAct;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Activity实例管理，所有Activity都继承BaseActivity或BaseFragmentActivity。
 * Activityoncreate时会调用此类的push方法，将Activity放入栈，onDestroy时会调用pop方法将Activity移出栈。
 * 
 * @author alaowan
 * 
 */
public class ActivityManager {

	private static Stack<Activity> activityStack;
	// 保持单实例的Activity的class
	private static Set<Class<?>> singleInstanceActClasses;

	private static ActivityManager instance;

	private ActivityManager() {
		activityStack = new Stack<Activity>();
		singleInstanceActClasses = new HashSet<Class<?>>();
	}

	public static ActivityManager getInstance() {
		if (instance == null)
			instance = new ActivityManager();
		return instance;
	}

	// 退出栈顶Activity
	public void popActivity(Activity activity) {
		if (activity != null) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	// 获得当前栈顶Activity
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty())
			activity = activityStack.lastElement();
		return activity;
	}

	public boolean hasActivityInstance(Class<?> cls) {
		if (!activityStack.empty()) {
			for (int i = 0; i < activityStack.size(); i++) {
				Activity activity = activityStack.get(i);
				if (activity.getClass().equals(cls))
					return true;
			}
		}
		return false;
	}

	public Activity getActivityInstanceByClass(Class<?> cls) {
		if (!activityStack.empty()) {
			for (int i = 0; i < activityStack.size(); i++) {
				Activity activity = activityStack.get(i);
				if (activity.getClass().equals(cls))
					return activity;
			}
		}
		return null;
	}

	public void closeActivity(Class<?> cls) {
		if (!activityStack.empty()) {
			for (int i = 0; i < activityStack.size(); i++) {
				Activity activity = activityStack.get(i);
				if (activity.getClass().equals(cls)) {
					activity.finish();
					break;
				}
			}
		}
	}

	// 将当前Activity推入栈中
	public void pushActivity(Activity activity) {
		if (singleInstanceActClasses.contains(activity.getClass())) {
			removeExistViewTaskActivity(activity.getClass());
		}
		activityStack.add(activity);
	}

	// 退出所有activity
	public void popAllActivity() {
		while (activityStack.size() > 0) {
			Activity activity = currentActivity();
			if (activity == null)
				break;
			popActivity(activity);
		}
	}

	// 退出栈中所有Activity
	public void popAllActivityExceptOne(Class<?> cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}
	
		public void popActivityUntilOne(Class<?> cls) {
			Boolean flag = true;
			while (flag) {
				Activity activity = currentActivity();
				if (activity == null) {
					
					break;
				}
				if (activity.getClass().equals(cls)) {
					flag = false;
					break;
					
				}
				popActivity(activity);
			}
		}
	/**
	 * 关闭并清除已存在的任务详细Activity
	 */
	private void removeExistViewTaskActivity(Class<?> cls) {
		for (int i = 0; i < activityStack.size(); i++) {
			Activity activity = activityStack.get(i);
			if (activity.getClass().equals(cls)) {
				popActivity(activity);
				break;
			}
		}
	}

}

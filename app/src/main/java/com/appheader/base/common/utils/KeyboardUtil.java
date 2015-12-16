package com.appheader.base.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘控制
 * @author alaowan
 *
 */
public class KeyboardUtil {
	
	/**
	 * 隐藏输入法键盘
	 * @param context
	 */
	public static void hideKeyboard(Activity context) {
		if (context == null)
			return;
		final View v = context.getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	
	public static void showKeyboard(Activity context, View targetView) {
		if (context == null)
			return;
		final View v = context.getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(targetView, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 输入法是否为显示状态
	 * @param context
	 * @return
	 */
	public static boolean isKeyboardShow(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}
}

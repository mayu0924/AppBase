package com.appheader.base.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 
 * @author mayu
 *
 */
public class MyScrollView2 extends ScrollView {
	private OnScrollViewListener onScrollListener;

	public MyScrollView2(Context context) {
		this(context, null);
	}

	public MyScrollView2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置滚动接口
	 * 
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollViewListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (onScrollListener != null) {
			onScrollListener.onScroll(t);
		}
	}

	/**
	 * 滚动的回调接口
	 */
	public interface OnScrollViewListener {
		/**
		 * 回调方法， 返回MyScrollView滑动的Y方向距离
		 * 
		 * @param scrollY
		 */
		public void onScroll(int scrollY);
	}

}

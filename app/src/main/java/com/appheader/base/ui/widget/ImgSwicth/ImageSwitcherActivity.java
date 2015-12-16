package com.appheader.base.ui.widget.ImgSwicth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.appheader.base.R;
import com.appheader.base.common.network.entity.UrlConstants;
import com.appheader.base.common.utils.ImageLoadUtil;
import com.appheader.base.common.utils.Tools;
import com.appheader.base.ui.baseAct.BaseFragmentActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class ImageSwitcherActivity extends BaseFragmentActivity {

	// 图片来源类型
	public static final String INTENT_KEY_IMAGE_SOURCE_TYPE = "source_type";
	// 图片路径
	public final static String INTENT_KEY_IMAGE_LIST = "image_path_list";
	// 点击图片的位置
	public final static String INTENT_KEY_POSITION = "position";
	// 图片来源类型 -- 本地文件
	public static final String SOURCE_TYPE_LOCAL_FILE = "local_file";
	// 图片来源类型 -- 网络图片
	public static final String SOURCE_TYPE_NETWORK = "network";

	private ArrayList<String> mImageList;
	private String mSourceType;
	private int mInitPosition;

	private ViewPager mViewPager;
	private ViewPagerAdapter mAdapter;
	private LinearLayout mLayoutIndicator;
	private List<ImageView> mIndicators;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_switcher);
		mSourceType = getIntent().getStringExtra(INTENT_KEY_IMAGE_SOURCE_TYPE);
		mImageList = getIntent().getStringArrayListExtra(INTENT_KEY_IMAGE_LIST);
		mInitPosition = getIntent().getIntExtra(INTENT_KEY_POSITION, 0);
		mLayoutIndicator = (LinearLayout) findViewById(R.id.layout_indicator);
		mIndicators = new ArrayList<ImageView>();
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new ViewPagerAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mInitPosition);
		initIndicator();
		setIndicator();
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				setIndicator();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initIndicator() {
		for (int i = 0; i < mImageList.size(); i++) {
			ImageView image = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			layoutParams.rightMargin = 5;
			layoutParams.leftMargin = 5;
			image.setBackgroundResource(R.mipmap.pub_pagerindicator_point0);
			mLayoutIndicator.addView(image, layoutParams);
			mIndicators.add(image);
		}
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 */
	private void setIndicator() {
		for (int i = 0; i < mIndicators.size(); i++) {
			if (i == mViewPager.getCurrentItem()) {
				mIndicators.get(i).setBackgroundResource(R.mipmap.pub_pagerindicator_point1);
			} else {
				mIndicators.get(i).setBackgroundResource(R.mipmap.pub_pagerindicator_point0);
			}
		}
	}

	class ViewPagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		public ViewPagerAdapter() {
			inflater = LayoutInflater.from(getApplicationContext());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mImageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			String resource = mImageList.get(position);
			View layout = inflater.inflate(R.layout.image_switcher_item, view, false);
			ImageView imgThumb = (ImageView) layout.findViewById(R.id.imgThumb);
			final ImageView imgFull = (ImageView) layout.findViewById(R.id.imgFull);
			final View loading = layout.findViewById(R.id.loading);
			// 本地文件
			if (mSourceType.equals(SOURCE_TYPE_LOCAL_FILE)) {
				imgFull.setVisibility(View.VISIBLE);
				imgThumb.setVisibility(View.GONE);
				String imagePath = "file://" + resource;
				ImageLoadUtil.displayImage(imagePath, imgFull, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						loading.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						loading.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						loading.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						loading.setVisibility(View.GONE);
					}
				});
			} else { // 网络文件
				imgFull.setVisibility(View.INVISIBLE);
				imgThumb.setVisibility(View.VISIBLE);
				String thumbUri, fullUri;
				if (resource.startsWith("http://")) {
					thumbUri = fullUri = resource;
				} else {
					thumbUri = UrlConstants.getImageThumbUrl(resource);
					fullUri = UrlConstants.getResourceUrl(resource);
				}
				// 显示缩略图
				ImageLoadUtil.displayImage(thumbUri, imgThumb);
				// 读取大图
				ImageLoadUtil.displayImage(fullUri, imgFull, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						loading.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						loading.setVisibility(View.GONE);
						Tools.showNetworkError();
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						loading.setVisibility(View.GONE);
						imgFull.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						loading.setVisibility(View.GONE);
					}
				});
			}
			view.addView(layout, 0);
			return layout;
		}

	}
}

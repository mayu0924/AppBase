package com.appheader.base.ui.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appheader.base.R;
import com.appheader.base.common.network.entity.UrlConstants;
import com.appheader.base.common.utils.ImageLoadUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 问题已解决：可以无限轮播，加载网络图片，并缓存 通过setViewPagerViews(List<String>
 * imgList)，添加要轮播的图片的url集合 新增轮播滑动动画 新增轮播页点击事件回调
 * 
 * @author mayu
 * 
 */
@SuppressLint({ "RtlHardcoded", "ClickableViewAccessibility", "HandlerLeak" })
public class MyBanner extends RelativeLayout {
	@SuppressWarnings("unused")
	private Context mCtx;
	private static final int RMP = LayoutParams.MATCH_PARENT;
	private static final int RWC = LayoutParams.WRAP_CONTENT;
	private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private ViewPager mViewPager = null;
	private MyAdapter mPagerAdapter = null;
	private List<BannerItem> imgList = null;
	private LinearLayout mPointContainer = null;
	private List<ImageView> mPoints = null;
	private boolean mPointVisibility = false;
	private boolean mAutoPlayAble = false;
	private boolean mIsAutoPlaying = false;
	private int mAutoPlayInterval = 3;
	private int mAutoPlayScrollFactor = 5;
	private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
	private int mPointSpacing = 15;
	private int mPointEdgeSpacing = 15;
	private int mPointContainerWidth = RMP;
	private int mPointContainerHeight = RWC;
	private int mCurrentPoint = 0;
	private Drawable mPointFocusedDrawable;
	private Drawable mPointUnfocusedDrawable;
	private Drawable mPointContainerBackgroundDrawable;
	private ScheduledExecutorService scheduledExecutorService;

	private CustomDurationScroller scroller = null;
	private OnBannerClickListener onBannerClickListener = null;
	private OnBannerPageChangedListener onBannerPageChangedListener = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);// 切换当前显示的图片
		};
	};
	private Runnable mAutoPlayTask = new Runnable() {
		@Override
		public void run() {
			handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
		}
	};

	public MyBanner(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.mCtx = context;
	}

	public MyBanner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
		this.mCtx = context;
		// initView(context);
	}

	public void setOnBannerPageChangedListener(OnBannerPageChangedListener listener) {
		this.onBannerPageChangedListener = listener;
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyBanner);
		final int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = typedArray.getIndex(i);
			if (attr == R.styleable.MyBanner_pointFocusedImg) {
				mPointFocusedDrawable = typedArray.getDrawable(attr);

			} else if (attr == R.styleable.MyBanner_pointUnfocusedImg) {
				mPointUnfocusedDrawable = typedArray.getDrawable(attr);

			} else if (attr == R.styleable.MyBanner_pointContainerBackground) {
				mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);

			} else if (attr == R.styleable.MyBanner_pointSpacing) {
				/**
				 * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,
				 * 如果是dp或sp的单位,将其乘以density,如果是px,则不乘;两个函数的区别是一个返回float,一个返回int.
				 * getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
				 */
				mPointSpacing = typedArray.getDimensionPixelSize(attr, mPointSpacing);

			} else if (attr == R.styleable.MyBanner_pointEdgeSpacing) {
				mPointEdgeSpacing = typedArray.getDimensionPixelSize(attr, mPointEdgeSpacing);

			} else if (attr == R.styleable.MyBanner_pointGravity) {
				mPointGravity = typedArray.getInt(attr, mPointGravity);

			} else if (attr == R.styleable.MyBanner_pointContainerWidth) {
				try {
					mPointContainerWidth = typedArray.getDimensionPixelSize(attr, mPointContainerWidth);
				} catch (UnsupportedOperationException e) {
					// 如果是指定的wrap_content或者match_parent会执行下面这一行
					mPointContainerWidth = typedArray.getInt(attr, mPointContainerWidth);
				}

			} else if (attr == R.styleable.MyBanner_pointContainerHeight) {
				try {
					mPointContainerHeight = typedArray.getDimensionPixelSize(attr, mPointContainerHeight);
				} catch (UnsupportedOperationException e) {
					mPointContainerHeight = typedArray.getInt(attr, mPointContainerHeight);
				}

			} else if (attr == R.styleable.MyBanner_pointVisibility) {
				mPointVisibility = typedArray.getBoolean(attr, mPointVisibility);

			} else if (attr == R.styleable.MyBanner_pointAutoPlayAble) {
				mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);

			} else if (attr == R.styleable.MyBanner_pointAutoPlayScrollFactor) {
				mAutoPlayScrollFactor = typedArray.getInteger(attr, mAutoPlayScrollFactor);

			} else if (attr == R.styleable.MyBanner_pointAutoPlayInterval) {
				mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
			}
		}
		typedArray.recycle();
	}

	@SuppressWarnings("deprecation")
	public void initView(Context context) {
		mViewPager = new ViewPager(context);
		addView(mViewPager, new LayoutParams(RMP, RMP));

		if (mPointVisibility) {
			if (mPointFocusedDrawable == null) {
				throw new RuntimeException("pointFocusedImg is not allowed to be NULL");
			} else if (mPointUnfocusedDrawable == null) {
				throw new RuntimeException("pointUnfocusedImg is not allowed to be NULL");
			}
			mPointContainer = new LinearLayout(context);
			mPointContainer.setOrientation(LinearLayout.HORIZONTAL);
			mPointContainer.setPadding(mPointEdgeSpacing, 0, mPointEdgeSpacing, 0);
			if (mPointContainerBackgroundDrawable != null) {
				mPointContainer.setBackgroundDrawable(mPointContainerBackgroundDrawable);
			}
			LayoutParams pointContainerLp = new LayoutParams(mPointContainerWidth, mPointContainerHeight);
			// 处理圆点在顶部还是底部
			if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
				pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			} else {
				pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			}
			int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
			// 处理圆点在左边、右边还是水平居中
			if (horizontalGravity == Gravity.LEFT) {
				mPointContainer.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			} else if (horizontalGravity == Gravity.RIGHT) {
				mPointContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			} else {
				mPointContainer.setGravity(Gravity.CENTER);
			}
			addView(mPointContainer, pointContainerLp);
		}
		// setViewPagerScroller();
	}

	/**
	 * 设置ViewPager的滑动速度和动画
	 */
	@SuppressWarnings("unused")
	private void setViewPagerScroller() {
		try {
			Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
			scrollerField.setAccessible(true);
			Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
			interpolatorField.setAccessible(true);

			scroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
			scroller.setScrollDurationFactor(mAutoPlayScrollFactor);
			scrollerField.set(mViewPager, scroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 适配数据
	 * 
	 * @param ctx
	 * @param urlList
	 */
	public void setViewPagerViews(Context ctx, List<BannerItem> urlList, OnBannerClickListener listener) {
		this.onBannerClickListener = listener;
		imgList = urlList;
		if (mViewPager == null)
			initView(ctx);
		if (mPagerAdapter == null)
			mPagerAdapter = new MyAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mPagerAdapter.notifyDataSetChanged();
		mViewPager.setOnPageChangeListener(new MyListener());
		if (mPointVisibility) {
			initPoints();
			processAutoPlay();
			if (mAutoPlayAble)
				startAutoPlay();
		}
	}

	public void setViewPagerViews(Context ctx, List<BannerItem> urlList) {
		setViewPagerViews(ctx, urlList, onBannerClickListener);
	}

	public void setOnBannerClickListener(OnBannerClickListener listener) {
		onBannerClickListener = listener;
	}

	private void initPoints() {
		mPointContainer.removeAllViews();
		mViewPager.removeAllViews();
		if (null != mPoints) {
			mPoints.clear();
		} else {
			mPoints = new ArrayList<ImageView>();
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
		int margin = mPointSpacing / 2;
		lp.setMargins(margin, 0, margin, 0);
		ImageView imageView;
		for (int i = 0; i < imgList.size(); i++) {
			imageView = new ImageView(getContext());
			imageView.setLayoutParams(lp);
			imageView.setImageDrawable(mPointUnfocusedDrawable);
			mPoints.add(imageView);
			mPointContainer.addView(imageView);
		}
	}

	private void processAutoPlay() {
		if (mAutoPlayAble) {
			// 有配置自动轮播才去实例化handler
			mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % imgList.size());
		} else {
			switchToPoint(0);
		}
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == VISIBLE) {
			startAutoPlay();
		} else if (visibility == INVISIBLE) {
			stopAutoPlay();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
	}

	private void startAutoPlay() {
		if (mViewPager != null && imgList.size() > 1) {
			if (mAutoPlayAble && !mIsAutoPlaying) {
				mIsAutoPlaying = true;

				scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
				// 当Activity显示出来后，每两秒钟切换一次图片显示
				scheduledExecutorService.scheduleAtFixedRate(mAutoPlayTask, mAutoPlayInterval, mAutoPlayInterval, TimeUnit.SECONDS);
			}
		}
	}

	private void stopAutoPlay() {

		if (mAutoPlayAble && mIsAutoPlaying && scheduledExecutorService != null) {
			mIsAutoPlaying = false;
			// 当Activity不可见的时候停止切换
			scheduledExecutorService.shutdown();
		}
	}

	private void switchToPoint(int newCurrentPoint) {
		mPoints.get(mCurrentPoint).setImageDrawable(mPointUnfocusedDrawable);
		mPoints.get(newCurrentPoint).setImageDrawable(mPointFocusedDrawable);
		mCurrentPoint = newCurrentPoint;
	}

	private final class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// 获取ViewPager的个数，这个方法是必须实现的
			return (mAutoPlayAble) ? Integer.MAX_VALUE : imgList.size();
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			// container容器就是ViewPager, position指的是ViewPager的索引
			// 从View集合中获取对应索引的元素, 并添加到ViewPager中
			ImageView img = new ImageView(getContext());
			img.setScaleType(ScaleType.CENTER_CROP);
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onBannerClickListener != null) {
						if (mAutoPlayAble) {
							onBannerClickListener.onBannerClick(position % imgList.size());
						} else {
							onBannerClickListener.onBannerClick(position);
						}
					}
				}
			});
			if (mAutoPlayAble) {
				ImageLoadUtil.displayImage(UrlConstants.getResourceUrl(imgList.get(position % imgList.size()).getPicResId()), img);
				((ViewPager) container).addView(img);
				return img;
			} else {
				ImageLoadUtil.displayImage(UrlConstants.getResourceUrl(imgList.get(position).getPicResId()), img);
				return img;
			}
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// 从ViewPager中删除集合中对应索引的View对象
			if (mAutoPlayAble) {
				((ViewPager) container).removeView((View) object);
			} else {
				((ViewPager) container).removeView((View) object);
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// view 要关联的页面, object instantiateItem()方法返回的对象
			// 是否要关联显示页面与 instantiateItem()返回值，这个方法是必须实现的
			return view == object;
		}
	}

	public List<BannerItem> getDataList() {
		return imgList;
	}

	public static interface OnBannerClickListener {
		public void onBannerClick(int position);
	}

	private final class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				// 开始滑动
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				// 当松开手时
				// 如果没有其他页显示出来：SCROLL_STATE_DRAGGING --> SCROLL_STATE_IDLE
				// 如果有其他页有显示出来（不管显示了多少），就会触发正在设置页码
				// 页码没有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING -->
				// SCROLL_STATE_IDLE
				// 页码有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING -->
				// onPageSelected --> SCROLL_STATE_IDLE
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				// 停止滑动
				break;
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// LogUtil.info("TAG", "onPageScrolled:  position=" + position
			// + "  positionOffset=" + positionOffset
			// + "  positionOffsetPixels=" + positionOffsetPixels);
		}

		@Override
		public void onPageSelected(int position) {
			if (mPointVisibility) {
				if (mAutoPlayAble) {
					switchToPoint(position % imgList.size());
				} else {
					switchToPoint(position);
				}
			}
			if (onBannerPageChangedListener != null && !imgList.isEmpty())
				onBannerPageChangedListener.onPageChanged(position, imgList.get(position));
		}
	}

	public static interface OnBannerPageChangedListener {

		public void onPageChanged(int position, BannerItem item);
	}

}
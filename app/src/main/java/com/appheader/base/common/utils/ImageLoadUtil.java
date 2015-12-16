package com.appheader.base.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.appheader.base.R;
import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.user.UserInfo;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 加载网络图片|支持本地不听uri格式的图片资源
 * {
 * 	"http://site.com/image.png" 				// from Web
"	 file:///mnt/sdcard/image.png" 				// from SD card
"	 file:///mnt/sdcard/video.mp4" 				// from SD card (video thumbnail)
" 	 content://media/external/images/media/13" 	// from content provider
"	 content://media/external/video/media/13" 	// from content provider (video thumbnail)
"	 assets://image.png" 						// from assets
"	 drawable://" + R.drawable.img 				// from drawables (non-9patch images)
 * }
 * @author mayu
 */
public class ImageLoadUtil {
	private static Context mCtx;
	
	// ---------------- 根据项目修改默认图 ------------------
	private static int DEFAULT_LOADING = R.drawable.ic_launcher;
	
	private static int DEFAULT_FAILED = R.drawable.ic_launcher;
	
	// ---------------- 圆角图片的半径 ---------------------
	private static int CORNER_RADIUS = 8;
	
	private static DisplayImageOptions roundDisplayOptions;
	private static DisplayImageOptions imageDisplayOptions;
	private static ImageLoaderConfiguration defaultLoaderConfig;

	public static void initialize(Context ctx) {
		mCtx = ctx;
		roundDisplayOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(CORNER_RADIUS)).cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(DEFAULT_LOADING).showImageForEmptyUri(DEFAULT_FAILED).showImageOnFail(DEFAULT_FAILED)
				.build();
		imageDisplayOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(DEFAULT_LOADING)
				.showImageForEmptyUri(DEFAULT_FAILED).showImageOnFail(DEFAULT_FAILED).build();
		
		defaultLoaderConfig = createConfiguration(StorageUtils.getCacheDirectory(mCtx), imageDisplayOptions);
		ImageLoader.getInstance().init(defaultLoaderConfig);
	}
	
	/**
	 * 清理图片缓存
	 * @return
	 */
	public static Boolean clearCache(){
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
		return true;
	}

	/**
	 * 圆角图片
	 * @param imageUri
	 * @param imageView
	 */
	public static void displayRoundCornerImage(String imageUri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(imageUri, imageView, roundDisplayOptions);
	}
	
	/**
	 * 正常图片
	 * @param imageUri
	 * @param imageView
	 */
	public static void displayImage(String imageUri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(imageUri, imageView, imageDisplayOptions);
	}
	
	/**
	 * 监听图片加载过程
	 * @param imageUri
	 * @param imageView
	 * @param listener
	 */
	public static void displayImage(String imageUri, ImageView imageView, ImageLoadingListener listener){
		ImageLoader.getInstance().displayImage(imageUri, imageView, imageDisplayOptions, listener);
	}
	
	/**
	 * 自定义加载失败默认图
	 * @param imageUri
	 * @param imageView
	 * @param defaltImg
	 */
	public static void displayImageWithDefault(String imageUri, final ImageView imageView, final int defaltImg){
		ImageLoadUtil.displayImage(imageUri, imageView, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				imageView.setImageResource(defaltImg);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
	}
	
	/**
	 * 缓存配置
	 * @param cacheDir
	 * @param displayOpts
	 * @return
	 */
	private static ImageLoaderConfiguration createConfiguration(File cacheDir, DisplayImageOptions displayOpts) {
		return new ImageLoaderConfiguration.Builder(mCtx)
		.diskCacheExtraOptions(480, 800, null)
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		.diskCache(new UnlimitedDiskCache(cacheDir))
		.imageDownloader(new MyImageDownloader(mCtx))
		.defaultDisplayImageOptions(displayOpts)
		.build();
	}

	public static final class MyImageDownloader extends BaseImageDownloader {

		public MyImageDownloader(Context context) {
			super(context);
		}

		public MyImageDownloader(Context context, int connectTimeout, int readTimeout) {
			super(context, connectTimeout, readTimeout);
		}

		@Override
		protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
			String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
			HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			UserInfo currentUser = CurrentUserManager.getCurrentUser();
			if(currentUser != null){
				conn.addRequestProperty("session_id", currentUser.getSession_id());
			}
			conn.addRequestProperty("platform", "android");
			return conn;
		}

	}
	
	/**
	 * 密度Dpi转像素
	 * @param context
	 * @param dip 密度Dpi值
	 * @return 像素值
	 */
	public static int dip2px(Context context, float dip){
	    return (int)(0.5F + dip * getDensity(context));
	}
	
	/**
	 * 获取屏幕密度,如0.75/1.0/1.5
	 * 
	 * @return 密度float值
	 */
	public static float getDensity(Context context) {
		DisplayMetrics dm = getScreenInfo(context);
		return dm.density;
	}
	
	/**
	 * 获取当前机器的屏幕信息对象<br/>
	 * 另外：通过android.os.Build类可以获取当前系统的相关信息
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenInfo(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		
		//int w = dm.widthPixels;//寬度（像素）
		//int h = dm.heightPixels; //高度（像素）
		//float d = dm.density; //密度（0.75 / 1.0 / 1.5）
		//int densityDpi = dm.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
		return dm;
	}
}

package com.mayu.tiangou.common.glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mayu.tiangou.R;

import java.io.File;

/**
 * Created by mayu on 16/3/3,下午2:46.
 * 【http://mrfu.me/2016/02/27/Glide_Image_Resizing_&_Scaling/】
 *
 * a.禁用缓存
 * 【.diskCacheStrategy( DiskCacheStrategy.NONE )
 * .skipMemoryCache( true )】
 *
 * b.优先级【.priority(Priority.LOW)】
 * Priority.LOW
 * Priority.NORMAL
 * Priority.HIGH
 * Priority.IMMEDIATE
 *
 * c.缩略图【.thumbnail( 0.1f )】
 *
 * d.回调：SimpleTarget 和 ViewTarget 用于自定义视图类【NotificationTarget】
 *
 * e.调试命令【adb shell setprop log.tag.GenericRequest DEBUG 】
 *
 * f.单个转换的应用【.transform( new BlurTransformation( context ) )】
 * 【.transform( new GreyscaleTransformation( context ), new BlurTransformation( context ) )】
 *
 * glide-transformations-----------------
 * 【https://github.com/wasabeef/glide-transformations】
 *
 * g.自定义加载动画【.animate( android.R.anim.slide_in_left )】
 *
 */
public class GlideManager {
    private Context mContext;
    public RequestManager mRequestManager;
    public GlideManager(Context context) {
        this.mContext = context;
        this.mRequestManager = Glide.with(mContext);
    }

    /**------------------------------------------A
     * 从资源中加载
     * @param imageView
     * @param resourceId
     */
    public void load(ImageView imageView, int resourceId){
        load(imageView, resourceId, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    /**
     * 从资源中加载
     * @param imageView
     * @param resourceId
     * @param defaultImage
     * @param errorImage
     */
    public void load(ImageView imageView, int resourceId, int defaultImage, int errorImage){
        mRequestManager.load(resourceId)
                .placeholder(defaultImage)
                .error(errorImage)
                .crossFade(200)
                .into(imageView);
    }

    /**------------------------------------------B
     * 从文件中加载
     * @param imageView
     * @param file
     */
    public void load(ImageView imageView, File file){
        load(imageView, file, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    /**
     * 从文件中加载
     * @param imageView
     * @param file
     * @param defaultImage
     * @param errorImage
     */
    public void load(ImageView imageView, File file, int defaultImage, int errorImage){
        mRequestManager.load(file)
                .placeholder(defaultImage)
                .error(errorImage)
                .crossFade(200)
                .into(imageView);
    }

    /**------------------------------------------C
     * 从 Uri 中加载
     * @param imageView
     * @param uri
     */
    public void load(ImageView imageView, Uri uri){
        load(imageView, uri, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    /**
     * 从 Uri 中加载
     * @param imageView
     * @param uri
     * @param defaultImage
     * @param errorImage
     */
    public void load(ImageView imageView, Uri uri, int defaultImage, int errorImage){
        mRequestManager.load(uri)
                .placeholder(defaultImage)
                .error(errorImage)
                .crossFade(200)
                .into(imageView);
    }

    /**
     * 加载Gif
     * @param imageView
     * @param uri
     * @param defaultImage
     * @param errorImage
     */
    public void loadGif(ImageView imageView, Uri uri, int defaultImage, int errorImage){
        mRequestManager.load(uri)
                .asGif()
                .placeholder(defaultImage)
                .error(errorImage)
                .into(imageView);
    }

    /**
     * 加载Bitmap
     * @param imageView
     * @param uri
     * @param defaultImage
     * @param errorImage
     */
    public void loadBitmap(ImageView imageView, Uri uri, int defaultImage, int errorImage){
        mRequestManager.load(uri)
                .asBitmap()
                .placeholder(defaultImage)
                .error(errorImage)
                .into(imageView);
    }

    /**
     * 显示本地视频
     * @param imageView
     * @param uri
     * @param defaultImage
     * @param errorImage
     */
    public void loadLocalVideo(ImageView imageView, Uri uri, int defaultImage, int errorImage){
        mRequestManager.load(uri)
                .asBitmap()
                .placeholder(defaultImage)
                .error(errorImage)
                .into(imageView);
    }

    private RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            // todo log exception

            // important to return false so the error placeholder can be placed
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    /**
     * 简单的从资源 id 转换成 Uri
     * @param context
     * @param resourceId
     * @return
     */
    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}

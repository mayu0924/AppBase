package com.mayu.tiangou.activity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.mayu.tiangou.R;
import com.mayu.tiangou.common.util.BitmapUtils;
import com.mayu.tiangou.common.util.L;
import com.mayu.tiangou.common.view.PinchImageView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImagesActivity extends BaseActivity {

    private static final String TAG = ImagesActivity.class.getSimpleName();
    Toolbar toolbar;
    TextView tvShowNum;
    ViewPager viewPager;
    TextView btnSave;
    TextView btnWallpaper;


    private Context mContext;

    private ArrayList<String> mDatas = new ArrayList<>();
    private int startIndex;
    private String title;
    private int size;
    private TouchImageAdapter touchImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvShowNum = (TextView) findViewById(R.id.tv_showNum);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btnSave = (TextView) findViewById(R.id.btn_save);
        btnWallpaper = (TextView) findViewById(R.id.btn_wallpaper);
        mContext = this;
        initIntent();
        initToolBar(toolbar, title, R.mipmap.ic_back);

        initData();

        //初始化ViewPager
        initViewPager();

    }

    public void initToolBar(Toolbar toolbar, String title, int icon) {
        toolbar.setTitle(title);// 标题的文字需在setSupportActionBar之前，不然会无效
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(icon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewPager() {
        touchImageAdapter = new TouchImageAdapter(mContext, mDatas);
        viewPager.setAdapter(touchImageAdapter);
        if (startIndex > 0) {
            viewPager.setCurrentItem(startIndex);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvShowNum.setText((position + 1) + "/" + mDatas.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIntent() {
        //获取传递的数据
        Intent intent = getIntent();
        mDatas = intent.getStringArrayListExtra("images");
        startIndex = intent.getIntExtra("index", 0);
        title = intent.getStringExtra("title");
        size = intent.getIntExtra("size", 0);
    }

    private void initData() {
        tvShowNum.setText((startIndex + 1) + "/" + mDatas.size());
    }

    @OnClick(R.id.btn_save)
    void btn_save() {
        PinchImageView imageView = getCurrentImageView();
        if (imageView == null) {
            showProgressError("图片保存失败");
            return;
        }
        //显示dialog
        showProgressDialog("正在保存...");
        final Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
        //save Image
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean saveBitmapToSD = BitmapUtils.saveBitmapToSD(bitmap, BasePath, System.currentTimeMillis() + ".jpg", true);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (saveBitmapToSD) {
                            showProgressSuccess("保存成功，保存目录：" + BasePath);
                        } else {
                            showProgressError("图片保存失败");
                        }
                    }
                });
            }
        }).start();

    }

    @Nullable
    private PinchImageView getCurrentImageView() {
        View currentItem = touchImageAdapter.getPrimaryItem();
        if (currentItem == null) {
            L.i("btn_save----currentItem是空");
            return null;
        }
        PinchImageView imageView = (PinchImageView) currentItem.findViewById(R.id.imageView);
        if (imageView == null) {
            L.i("btn_save----imageView是空");
            return null;
        }
        return imageView;
    }

    @OnClick(R.id.btn_wallpaper)
    void btn_wallpaper() {
        PinchImageView imageView = getCurrentImageView();
        if (imageView == null) {
            showProgressError("设置失败");
            return;
        }
        Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
        WallpaperManager manager = WallpaperManager.getInstance(mContext);
        try {
            showProgressDialog("正在设置壁纸...");
            manager.setBitmap(bitmap);
            showProgressSuccess("设置成功");
        } catch (IOException e) {
            e.printStackTrace();
            showProgressError("设置失败");
        }
    }


    static class TouchImageAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<String> mDatas;
        private LayoutInflater layoutInflater;
        private View mCurrentView;

        public TouchImageAdapter(Context mContext, ArrayList<String> mDatas) {
            this.mContext = mContext;
            this.mDatas = mDatas;
            layoutInflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentView = (View) object;
        }

        public View getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final String imageUrl = mDatas.get(position);
            View inflate = layoutInflater.inflate(R.layout.item_show_image, container, false);
            final ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_gray_bg)
                    .thumbnail(0.2f)
                    .into(imageView);
            container.addView(inflate);

            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        //清空集合
        if (mDatas != null) {
            mDatas.clear();
            mDatas = null;
        }
        super.onDestroy();
    }
}

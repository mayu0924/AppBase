package com.appheader.base;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.appheader.base.sdk.glide.RotateTransformation;
import com.appheader.base.ui.baseAct.BaseFragmentActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0104/2255.html
 * <p>
 * https://github.com/codepath/android_guides/wiki/ActiveAndroid-Guide
 *
 * @author mayu
 */
public class MainActivity extends BaseFragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.imageView)
    ImageView mImageView;

    //    private DbService mDbService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        mDbService = DbService.getInstance(this);
        mGlideManager.mRequestManager.load("http://attach.bbs.miui.com/forum/201409/08/073328srdr74a8avagtatz.jpg")
//                .thumbnail(0.2f)
                .centerCrop()
                .transform(new RotateTransformation(this, 90f))
                .crossFade(2000)
                .into(mImageView);
    }

    /*@OnClick(R.id.save)
    public void testSave(){
        GreenArticle greenArticle = new GreenArticle();
        greenArticle.setTitle("测试--" + System.currentTimeMillis());
        greenArticle.setColor("红色");
        greenArticle.setContent("这是内容");
        greenArticle.setHref("http://www.baidu.com/");
        greenArticle.setPosition(0);
        greenArticle.setDate(new Date().toLocaleString());
        mDbService.saveGreenArticle(greenArticle);
	}

    @OnClick(R.id.query)
	public void testQuery(){
        List<GreenArticle> list = mDbService.loadAllGreenArticle();
        for (int i = 0; i < list.size(); i++){
            Logger.i(list.get(i).toString());
        }
	}

    @OnClick(R.id.update)
	public void testUpdate(){
        List<GreenArticle> list = mDbService.queryGreenArticle(" where COLOR = ?", "红色");
        for(int i = 0; i < list.size(); i++){
            list.get(i).setColor("蓝色");
        }
        mDbService.saveGreenArticleLists(list);
	}

    @OnClick(R.id.delete)
    public void testDelete(){
        mDbService.deleteAllGreenArticle();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}*/
}

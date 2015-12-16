package com.appheader.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.appheader.base.common.orm.DbService;
import com.appheader.db.GreenArticle;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0104/2255.html
 * 
 * https://github.com/codepath/android_guides/wiki/ActiveAndroid-Guide
 * @author mayu
 *
 */
public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
    private DbService mDbService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
        mDbService = DbService.getInstance(this);
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testSave();
            }
        });
		findViewById(R.id.query).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testQuery();
            }
        });
		findViewById(R.id.update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testUpdate();
            }
        });
	}

    @OnClick(R.id.save)
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
	}
}

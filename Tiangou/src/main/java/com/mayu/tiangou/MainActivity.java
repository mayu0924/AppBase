package com.mayu.tiangou;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mayu.tiangou.common.network.RequestHelper;
import com.mayu.tiangou.common.network.entity.UrlConstants;
import com.mayu.tiangou.entity.Category;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String[] tabTitleArray = {"要闻", "英雄联盟", "守望先锋", "NBA", "程序员", "电竞",
            "经济"};
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private SimpleFragmentPagerAdapter mAdapter;

    private Category mCategory;
    private List<Category.TngouBean> mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getPictureMenu();
    }

    private void initTabLayout(){
        mAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void getPictureMenu(){
        RequestHelper.sendGetRequest(TAG, UrlConstants.GET_PICTURE_MENU, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mCategory = new Gson().fromJson(response.toString(), Category.class);
                if(response.optBoolean("status")){
                    mCategoryList = mCategory.getTngou();
                    initTabLayout();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error);
            }
        });
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(mCategoryList.get(position));
        }

        @Override
        public int getCount() {
            return mCategoryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCategoryList.get(position).getTitle();
        }
    }
}

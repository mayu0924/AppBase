package com.mayu.tiangou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mayu.tiangou.adapter.MyAdapter;
import com.mayu.tiangou.common.network.RequestHelper;
import com.mayu.tiangou.common.network.entity.UrlConstants;
import com.mayu.tiangou.entity.Category;
import com.mayu.tiangou.entity.ImageBean;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;

public class PageFragment extends Fragment {
    private static final String TAG = PageFragment.class.getSimpleName();
    public static final String ARG_TNGOU = "ARG_PAGE";
    private Category.TngouBean mTnGou;
    private String mId;

    private XRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ArrayList<ImageBean.TngouBean> listData;
    private int times = 1;

    private ImageBean mImageBean;

    public static PageFragment newInstance(Category.TngouBean tngou) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TNGOU, tngou);
//        args.putString(ARG_PAGE, id);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTnGou = (Category.TngouBean)getArguments().getSerializable(ARG_TNGOU);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mRecyclerView = (XRecyclerView)view.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);

//        View header =   LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
//        mRecyclerView.addHeaderView(header);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                times = 1;
                getImageList(times);
            }

            @Override
            public void onLoadMore() {
                times ++;
                getImageList(times);
            }
        });

        listData = new  ArrayList<ImageBean.TngouBean>();
        mAdapter = new MyAdapter(listData);
        mRecyclerView.setAdapter(mAdapter);
        getImageList(times);
        return view;
    }

    private void getImageList(final int page){
        String url = UrlConstants.GET_PICTURE_LIST + "?id=" + mTnGou.getId()
                + "&page="+page+"&rows=20";
        RequestHelper.sendGetRequest(TAG, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mImageBean = new Gson().fromJson(response.toString(), ImageBean.class);
                if(mImageBean.isStatus() && mImageBean.getTngou() != null){
                    if(page == 1){
                        listData.clear();
                        listData.addAll(mImageBean.getTngou());
                    } else {
                        listData.addAll(mImageBean.getTngou());
                    }
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.refreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error);
            }
        });
    }
}
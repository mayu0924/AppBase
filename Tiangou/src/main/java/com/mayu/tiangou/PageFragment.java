package com.mayu.tiangou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.mayu.tiangou.adapter.RecyclePicAdapter;
import com.mayu.tiangou.common.network.RequestHelper;
import com.mayu.tiangou.common.network.entity.UrlConstants;
import com.mayu.tiangou.common.util.L;
import com.mayu.tiangou.entity.Category;
import com.mayu.tiangou.entity.ImageBean;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class PageFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = PageFragment.class.getSimpleName();
    public static final String ARG_TNGOU = "ARG_PAGE";
    private View mRootView;
    RecyclerView swipeTarget;
    SwipeToLoadLayout swipeToLoadLayout;

    private RecyclePicAdapter recyclePicAdapter;
    private Category.TngouBean mTnGou;
    private String mId;

    private ArrayList<ImageBean.TngouBean> mListData;
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
        mTnGou = (Category.TngouBean) getArguments().getSerializable(ARG_TNGOU);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(getActivity(), mRootView);
        initRefresh();
        mListData = new ArrayList<ImageBean.TngouBean>();
        getImageList(times);
        return mRootView;
    }

    private void initRefresh() {
        swipeToLoadLayout = (SwipeToLoadLayout) mRootView.findViewById(R.id.swipeToLoadLayout);
        swipeTarget = (RecyclerView) mRootView.findViewById(R.id.swipe_target);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        swipeTarget.setLayoutManager(staggeredGridLayoutManager);
        swipeTarget.setItemAnimator(new DefaultItemAnimator());
        //到底自动刷新
//        swipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
//                        swipeToLoadLayout.setLoadingMore(true);
//                    }
//                }
//            }
//        });
    }

    private void initRecycleView() {
        if (recyclePicAdapter == null) {
            recyclePicAdapter = new RecyclePicAdapter(getActivity(), mListData);
            swipeTarget.setAdapter(recyclePicAdapter);
            //点击事件
            recyclePicAdapter.setOnItemClickLitener(new RecyclePicAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int i = 0; i < mListData.size(); i++) {
                        String url = "http://tnfs.tngou.net/image" + mListData.get(i).getImg();
                        arrayList.add(url);
                    }
//                    IntentUtils.startToImageShow(context, arrayList, position);
                }
            });

        } else {
            recyclePicAdapter.updateDatas(mListData);
        }

    }

    private void getImageList(final int page) {
        String url = UrlConstants.GET_PICTURE_LIST + "?id=" + mTnGou.getId()
                + "&page=" + page + "&rows=10";
        RequestHelper.sendGetRequest(TAG, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("------------------------\n"
                        + response.toString()
                        + "\n------------------------");
                L.json(response.toString());
                mImageBean = new Gson().fromJson(response.toString(), ImageBean.class);
                if (mImageBean.isStatus() && mImageBean.getTngou() != null) {
                    if (page == 1) {
                        mListData.clear();
                        mListData.addAll(mImageBean.getTngou());
                    } else {
                        mListData.addAll(mImageBean.getTngou());
                    }
                    initRecycleView();
                    overRefresh();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error);
            }
        });
    }

    private void overRefresh() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onLoadMore() {
        times++;
        getImageList(times);
    }

    @Override
    public void onRefresh() {
        getImageList(1);
    }
}
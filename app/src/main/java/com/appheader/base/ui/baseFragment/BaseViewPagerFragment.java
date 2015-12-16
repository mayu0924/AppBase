package com.appheader.base.ui.baseFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appheader.base.R;

/**
 * 
 * @author mayu
 *
 */
@SuppressLint("InflateParams")
public abstract class BaseViewPagerFragment extends Fragment {
	protected PagerSlidingTabStrip mTabStrip;
	protected ViewPager mViewPager;
	protected ViewPageFragmentAdapter mTabsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.base_viewpage_fragment, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pager_tabstrip);
        
        mViewPager = (ViewPager)view.findViewById(R.id.pager);

        mTabsAdapter = new ViewPageFragmentAdapter(
        		getChildFragmentManager(), mTabStrip, mViewPager);

        onSetupTabAdapter(mTabsAdapter);
        mTabsAdapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(3);
        if (savedInstanceState != null) {
        	int pos = savedInstanceState.getInt("position");
        	mViewPager.setCurrentItem(pos, true);
        }
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mViewPager.getCurrentItem());
    }
	
	protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}

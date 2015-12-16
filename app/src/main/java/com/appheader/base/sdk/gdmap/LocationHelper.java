package com.appheader.base.sdk.gdmap;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.appheader.base.common.utils.LogUtil;
import com.appheader.base.ui.dialog.LoadingDialog;

import java.util.List;

/**
 * 获取当前位置文本帮助类
 * 
 * @author mayu
 *
 */
public class LocationHelper implements AMapLocationListener, OnGeocodeSearchListener {
	private static String TAG = LocationHelper.class.getSimpleName();
	private Context mCtx;
	private LoadingDialog mLoadingDialog;
	private OnLocationListener listener;
	private OnLatLngListener listener2;
	private LocationManagerProxy mLocationManagerProxy;
	private GeocodeSearch geocoderSearch;

	private String mCity;
	private String mAddress;
	private String mLatlng;

	public LocationHelper(Context ctx, OnLocationListener listener) {
		super();
		this.mCtx = ctx;
		this.listener = listener;
		mLoadingDialog = new LoadingDialog(mCtx);
		mLocationManagerProxy = LocationManagerProxy.getInstance(mCtx);
		mLocationManagerProxy.setGpsEnable(true);
//		startLocation();
	}
	
	public LocationHelper(Context ctx, String address, OnLatLngListener listener2){
		this.mCtx = ctx;
		this.listener2 = listener2;
		geocoderSearch = new GeocodeSearch(mCtx);
		geocoderSearch.setOnGeocodeSearchListener(this);
		// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
		GeocodeQuery query = new GeocodeQuery(address.trim(), "");
		geocoderSearch.getFromLocationNameAsyn(query);
	}

	public void startLocation() {
		mLoadingDialog.show();
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 1, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		LogUtil.debug(TAG, "onLocationChanged--------------Location");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		LogUtil.debug(TAG, "onStatusChanged--------------");
	}

	@Override
	public void onProviderEnabled(String provider) {
		LogUtil.debug(TAG, "onProviderEnabled--------------");
	}

	@Override
	public void onProviderDisabled(String provider) {
		LogUtil.debug(TAG, "onProviderDisabled--------------");
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		LogUtil.debug(TAG, "onLocationChanged--------------AMapLocation");
		mLoadingDialog.dismiss();
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 获取位置信息
			mCity = amapLocation.getCity();
			mAddress = amapLocation.getAddress();
			mLatlng = amapLocation.getLongitude() + ","
					+ amapLocation.getLatitude();
			if (!TextUtils.isEmpty(mAddress) && !TextUtils.isEmpty(mLatlng)) {
				listener.onSuccess(mCity, mAddress, mLatlng);
				mLocationManagerProxy.removeUpdates(this);
			} else {
				listener.onFailed("定位失败~");
				Toast.makeText(mCtx, "定位失败~", Toast.LENGTH_SHORT).show();
				LogUtil.debug(TAG, "定位失败~-----------------");
				mLocationManagerProxy.removeUpdates(this);
			}
		} else {
			Toast.makeText(mCtx, "定位失败!", Toast.LENGTH_SHORT).show();
			listener.onFailed("定位失败");
			mLocationManagerProxy.removeUpdates(this);
		}
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int arg1) {
		if(result != null){
			List<GeocodeAddress> list = result.getGeocodeAddressList();
			if (list != null) {
				if(list.get(0) != null){
					GeocodeAddress address = list.get(0);
					LatLonPoint point = address.getLatLonPoint();
					listener2.onResult(address.getCity(), point.getLongitude()+","+point.getLatitude());
				}
			}
		} else {
			listener2.onResult("", "");
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		
	}
	
	public interface OnLocationListener {
		void onSuccess(String city, String address, String latlng);

		void onFailed(String failed);
	}
	
	public interface OnLatLngListener{
		void onResult(String city, String latlng);
	}
}

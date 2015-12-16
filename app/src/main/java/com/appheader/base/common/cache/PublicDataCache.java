package com.appheader.base.common.cache;

import com.appheader.base.application.GlobalVars;

import java.io.File;
import java.io.IOException;

import libcore.io.DiskLruCache;

/**
 * 公共数据缓存，用于缓存与用户无关的业务数据，缓存内容可清除
 * 
 * @author alaowan
 * 
 */
public class PublicDataCache extends BaseCache {

	@Override
	protected String getCacheDir() {
		return GlobalVars.getAppFilesDir() + File.separator + "cache" + File.separator + "public_data";
	}

	@Override
	protected String getTAG() {
		return "PubicDataCache";
	}
	
	/**
	 * 清除缓存
	 */
	public boolean clearPubData() {
		try {
			DiskLruCache.deleteContents(new File(getCacheDir()));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}

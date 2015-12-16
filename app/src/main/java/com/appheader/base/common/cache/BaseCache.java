package com.appheader.base.common.cache;

import com.appheader.base.common.assit.MD5;
import com.appheader.base.common.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Editor;

public abstract class BaseCache {
	
	private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb

	private DiskLruCache mCache;
	private Editor mEditor;
	private String CACHE_FILE_DIR;
	
	public void init(){
		CACHE_FILE_DIR = getCacheDir();
		try {
			mCache = DiskLruCache.open(new File(CACHE_FILE_DIR), 1, 1, MAX_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Editor putCache(String key, Object obj) {
		String cacheKey = MD5.encrypt(key);
		try {
			mEditor = mCache.edit(cacheKey);
			if (mEditor != null) {
				OutputStream outputStream = mEditor.newOutputStream(0);
				ObjectOutputStream oos = new ObjectOutputStream(outputStream);
				oos.writeObject(obj);
				mEditor.commit();
			}
			mCache.flush();
			LogUtil.info(getTAG(), "成功将" + key + "对应的内容写入缓存！");
			return mEditor;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object getCache(String key) {
		Object obj = null;
		if (key == null) {
			return null;
		}
		String cacheKey = MD5.encrypt(key);
		DiskLruCache.Snapshot snapShot = null;
		InputStream is = null;
		ObjectInputStream ois = null;
		try {
			snapShot = mCache.get(cacheKey);
			if (snapShot != null) {
				is = snapShot.getInputStream(0);
				ois = new ObjectInputStream(is);
				obj = ois.readObject();
				LogUtil.info(getTAG(), "成功将" + key + "对应的内容从缓存中读取！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
				}
		}
		return obj;
	}
	
	public void remove(String key) {
		String cacheKey = MD5.encrypt(key);
		try {
			mCache.remove(cacheKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将所有的缓存数据全部删除
	 */
	public void removeAll() {
		try {
			DiskLruCache.deleteContents(new File(CACHE_FILE_DIR));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将DiskLruCache关闭掉,关闭掉了之后就不能再进行任何操作
	 */
	public void close() {
		try {
			mCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取缓存大小
	 */
	public String getCacheSize() {
		LogUtil.info("CacheSize", mCache.size() + "");
		if ((mCache.size() / 1024) > 0) {
			return (mCache.size() / 1024) + "kb";
		} else {
			return "0kb";
		}
	}
	
	protected abstract String getCacheDir();
	
	protected abstract String getTAG();
	
}

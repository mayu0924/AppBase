package com.appheader.base.common.files;

import android.os.Environment;

import com.appheader.base.application.GlobalVars;

import java.io.File;

/**
 * 资源文件存储管理
 * @author mayu
 *
 */
public class ResourceFileManager {
	/**
	 * 获取用户资源文件目录（不存在时自动创建目录）
	 * 该目录下包含images、audios等子目录
	 * @param userId
	 * @return
	 */
	public static String createOrGetUserProfileDir(String userId) {
		String sdcardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = sdcardRoot + File.separator + "." + GlobalVars.getPackageInfo().packageName + File.separator + "cache" + File.separator + "user_profiles" + File.separator + userId;
		File rootDir = new File(path);
		//创建主目录
		if(!rootDir.exists())
			rootDir.mkdirs();
		
		//创建图片目录
		File imagesDir = new File(path + File.separator + "images");
		if(!imagesDir.exists())
			imagesDir.mkdirs();
		
		//创建音频文件目录
		File audiosDir = new File(path + File.separator + "audios");
		if(!audiosDir.exists())
			audiosDir.mkdirs();
		
		return path;
	}
	
	/**
	 * 获取用户图片目录
	 * @param userId
	 * @return
	 */
	public static String getUserImageDir(String userId){
		return createOrGetUserProfileDir(userId) + File.separator + "images";
	}
	
	/**
	 * 获取用户音频文件目录
	 * @param userId
	 * @return
	 */
	public static String getUserAudioDir(String userId){
		return createOrGetUserProfileDir(userId) + File.separator + "audios";
	}
	
	/**
	 * 获取公用数据库目录
	 * @return
	 */
	public static String getPublicDatabaseDir(){
		String path = GlobalVars.getAppFilesDir() + File.separator + "databases";
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		return path;
	}
	
	/**
	 * 获取更新包目录
	 * @return
	 */
	public static String getUpdateApkDir(){
		String sdcardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "世贸城";
		File file = new File(sdcardRoot);
		if(!file.exists())
			file.mkdirs();
		return sdcardRoot ;
	}
	
}

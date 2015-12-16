package com.appheader.base.common.network.entity;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * 服务器接口url常量类，所有访问服务器的url必须在此类中定义常量。
 * 当res/raw/config.properties文件中的“useSimulateData”
 * 为true时，调用RequestHelper访问数据时，则不访问网络，而是读取本地模拟数据的txt文件
 * txt文件放在assets/simulate_data目录下，文件名位常量名+.txt
 * 
 * @author mayu
 * 
 */
public class UrlConstants {
    public static final String HTTP_IP = "http://123.56.103.157:8888/OnlineWardrobe";
	// 存放url与常量名映射关系的map，key为服务器接口url，value为该url在本类中对应的常量名称
	public static HashMap<String, String> urlMap;

	static {
		urlMap = new HashMap<String, String>();

		// 获取此类中定义的全部变量
		Field[] fields = UrlConstants.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			// 过滤出类型为String，且有public和static修饰符的变量
			if (fields[i].getType() == String.class
					&& Modifier.isPublic(fields[i].getModifiers())
					&& Modifier.isStatic(fields[i].getModifiers())) {
				try {
					String fieldValue = (String) fields[i].get(null);
					String fieldName = fields[i].getName();
					// 放入map中，key为url，value为变量名
					urlMap.put(fieldValue, fieldName);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 读取文件资源
	public static final String READ_RESOURCE_STREAM = "/resource/client.do?method=read&resourceId=";
	// 读取图片缩略图
	public static final String READ_IMAGE_THUMB_STREAM = "/resource/client.do?method=readImageThumb&resourceId=";
	// 上传文件（image/audio）
	public static final String UPLOAD_FILE_RESOURSE = "/resource/client.do?method=upload";

	/** ------------------------------------------------------------------------*/

	// 获取登录验证码
	public static final String LOGIN_VCODE_URL = "/rest/login.do?method=sendLoginSMS";
    // 获取绑卡验证码
    public static final String BINDCARD_VCODE_URL = "";
    // 绑定手机号
    public static final String BINDCARD_MOBILE_URL = "";
	// 登录
	public static final String LOGIN_URL = "/rest/login.do?method=login";
	// 第三方登录
	public static final String LOGIN_PLATFORM = "/rest/login.do?method=thirdClientLogin";
	// 退出登录
	public static final String LOGOUT_URL = "/rest/login.do?method=logout";
    

	/**文件图片资源相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/

	/**
	 * 生成读取资源文件流的URL
	 * 
	 * @param resourceId
	 * @return
	 */
	public static String getResourceUrl(String resourceId) {
		if (resourceId.startsWith("demo:")) {
			return "assets://demo_pics/" + resourceId.substring(5);
		} else if(resourceId.startsWith("http://")){
            return resourceId;
        }
		return HTTP_IP + READ_RESOURCE_STREAM + resourceId;
	}

	/**
	 * 生成读取图片缩略图文件流的URL
	 * 
	 * @param resourceId
	 * @return
	 */
	public static String getImageThumbUrl(String resourceId) {
		if (resourceId.startsWith("demo:")) {
			return "assets://demo/" + resourceId.substring(5);
		} else if(resourceId.startsWith("http://")){
			return resourceId;
		}
		return HTTP_IP + READ_IMAGE_THUMB_STREAM + resourceId;
	}
}

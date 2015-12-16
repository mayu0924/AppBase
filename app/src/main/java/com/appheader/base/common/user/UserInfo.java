package com.appheader.base.common.user;

import android.os.Build;

import com.appheader.base.application.MApplication;

import java.io.Serializable;

/**
 * 当前登录用户信息
 * 
 * @author alaowan
 *
 */
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 634262164656793495L;

	private String session_id;
	private String uid;
	private String mobile;
	private int score;
	private String gender;
	private String avatarId;
	private String personName;
	private String address;

	public UserInfo() {
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取设备信息（设备型号 + 系统版本）
	 * 
	 * @return
	 */
	public static String getDeviceInfo() {
		return Build.MODEL + ";  Android SDK LEVEL:" + Build.VERSION.SDK_INT;
	}

	public static String getImei() {
		return MApplication.getTelephonyManager().getDeviceId();
	}

}

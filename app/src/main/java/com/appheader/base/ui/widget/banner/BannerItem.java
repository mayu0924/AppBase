package com.appheader.base.ui.widget.banner;

import java.io.Serializable;

public class BannerItem implements Serializable {

	private static final long serialVersionUID = 2528352798273436260L;

	private String id;
	private String picResId;
	private String destination;
	private String paramsStr;
	private String url;

	public BannerItem() {

	}

	public BannerItem(String id, String picResId, String destination, String paramsStr, String url) {
		this.id = id;
		this.picResId = picResId;
		this.destination = destination;
		this.paramsStr = paramsStr;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicResId() {
		return picResId;
	}

	public void setPicResId(String picResId) {
		this.picResId = picResId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getParamsStr() {
		return paramsStr;
	}

	public void setParamsStr(String paramsStr) {
		this.paramsStr = paramsStr;
	}

}

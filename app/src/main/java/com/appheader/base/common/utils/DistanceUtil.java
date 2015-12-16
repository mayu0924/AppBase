package com.appheader.base.common.utils;

public class DistanceUtil {

	/**
	 * 根据经纬度，获取两点间的距离
	 * 
	 * @param lng1
	 *            经度
	 * @param lat1
	 *            纬度
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double distanceByLngLat(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = lat1 * Math.PI / 180;
		double radLat2 = lat2 * Math.PI / 180;
		double a = radLat1 - radLat2;
		double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
		s = Math.round(s * 10000) / 10000;

		return s;
	}

	public static double lngDiff(double lat, double distance) {
		return distance / (Math.cos(Math.toRadians(39.10368347167969)) * 111.1);
	}

	public static double latDiff(double distance) {
		return distance / 111.1;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(distanceByLngLat(117.13150787353516, 39.10368347167969, 118.13150787353516, 39.10368347167969));
		System.out.println(lngDiff(39.10368347167969, 0.5));
		System.out.println(latDiff(0.5));
	}
}

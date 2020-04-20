package com.hand.handtruck.utils;

import com.amap.api.maps.model.LatLng;
import com.hand.handtruck.bean.GPS;

import java.util.List;

/**
 * 坐标点
 * @date 2015.10.1
 */
public class LatlngUtil {
	private static double EARTH_RADIUS = 6378.137;
	// 功能：判断点是否在多边形内
	// 方法：求解通过该点的水平线与多边形各边的交点
	// 结论：单边交点为奇数，成立!
	//参数：
	// POINT p   指定的某个点
	// LPPOINT ptPolygon 多边形的各个顶点坐标（首末点可以不一致）
	public static boolean PtInPolygon(LatLng point, List<LatLng> APoints) {
		int nCross = 0;
		for (int i = 0; i < APoints.size(); i++)   {
			LatLng p1 = APoints.get(i);
			LatLng p2 = APoints.get((i + 1) % APoints.size());
			// 求解 y=p.y 与 p1p2 的交点
			if ( p1.longitude == p2.longitude)      // p1p2 与 y=p0.y平行
				continue;
			if ( point.longitude <  Math.min(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
				continue;
			if ( point.longitude >= Math.max(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
				continue;
			// 求交点的 X 坐标 --------------------------------------------------------------
			double x = (double)(point.longitude - p1.longitude) * (double)(p2.latitude - p1.latitude) / (double)(p2.longitude - p1.longitude) + p1.latitude;
			if ( x > point.latitude )
				nCross++; // 只统计单边交点
		}
		// 单边交点为偶数，点在多边形之外 ---
		return (nCross % 2 == 1);
	}

//
//	/**
//	 * 判断一个地理坐标是否在圆形区域内
//	 */
//	public static boolean isInCircle(LatLng point, LatLng circleCenter, String radius) {
//		double lat1=point.latitude;
//		double lng1=point.longitude;
//		double lat2=circleCenter.latitude;
//		double lng2=circleCenter.longitude;
//		double distance = getDistance(lat1, lng1, lat2, lng2);
//		double r = Double.parseDouble(radius);
//		if (distance > r) {
//			return false;
//		} else {
//			return true;
//		}
//	}

//	/**
//	 * 通过经纬度获取距离(单位：米)
//	 *
//	 * @param lat1
//	 * @param lng1
//	 * @param lat2
//	 * @param lng2
//	 * @return
//	 */
//	public static double getDistance(double lat1, double lng1, double lat2,
//									 double lng2) {
//		double radLat1 = rad(lat1);
//		double radLat2 = rad(lat2);
//		double a = radLat1 - radLat2;
//		double b = rad(lng1) - rad(lng2);
//		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
//				Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
//		s = s * EARTH_RADIUS;
//		s = Math.round(s * 10000d) / 10000d;
//		return s;
//	}
//
//	private static double rad(double d) {
//		return d * Math.PI / 180.0;
//	}

	/**
	 *  判读一个点是否在圆内 返回boolean  true存在   false不存在 LatLng point, LatLng circleCenter, String radius
	 */
	public static boolean isInCircle(LatLng point, LatLng circleCenter,double r)
	{
		double centerX1=point.latitude;
		double centerY1=point.longitude;
		double x2=circleCenter.latitude;
		double y2=circleCenter.longitude;
		double distance=Math.sqrt((y2-centerY1)*(y2-centerY1)+(x2-centerX1)*(x2-centerX1));
		if(distance>r){
			return false;
		}
		else
		{return true;}
	}


	public static double  dohandlerDistance(LatLng car,LatLng me){

		GPS map=GPSConverterUtils.gcj02_To_Bd09(me.latitude,me.longitude);
		double mlon=map.getLon();
		double mlat=map.getLat();
		double str=distance(car.longitude,car.latitude,mlon,mlat);
		return str;
	}


	/**
	 * 经纬度转化成弧度
	 * @param d  经度/纬度
	 * @return  经纬度转化成的弧度
	 */
	private static double radian(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 返回两个地理坐标之间的距离
	 * @param firsLongitude 第一个坐标的经度
	 * @param firstLatitude 第一个坐标的纬度
	 * @param secondLongitude 第二个坐标的经度
	 * @param secondLatitude  第二个坐标的纬度
	 * @return 两个坐标之间的距离，单位：公里/千米
	 */
	public static double distance(double firsLongitude,double firstLatitude,double secondLongitude,double secondLatitude)
	{
		double firstRadianLongitude = radian(firsLongitude);
		double firstRadianLatitude = radian(firstLatitude);
		double secondRadianLongitude = radian(secondLongitude);
		double secondRadianLatitude = radian(secondLatitude);

		double a = firstRadianLatitude - secondRadianLatitude;
		double b = firstRadianLongitude - secondRadianLongitude;
		double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(firstRadianLatitude) * Math.cos(secondRadianLatitude)
				* Math.pow(Math.sin(b / 2), 2)));
		cal = cal * EARTH_RADIUS;

		return Math.round(cal * 10000d) / 10000d;

	}
}

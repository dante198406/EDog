package com.hdsc.edog.entity;

public class GpsInfo {
	private int lng; // 经度
	private int lat; // 纬度
	private int altitude; // 海拔
	private int speed; // 速度
	private int bearing; // 方向 */
	private int satelliteCount; // 卫星个数
	private int gpsDate; //日期 
	
	private int gpsTimeS; // GPS秒数
	
	private int gpsFixTime; //GPS有效 计数
	public GpsInfo() {
	}

	public int getLng() {
		return lng;
	}

	public void setLng(int lng) {
		this.lng = lng;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getBearing() {
		return bearing;
	}

	public void setBearing(int bearing) {
		this.bearing = bearing;
	}

	public int getSatelliteCount() {
		return satelliteCount;
	}

	public void setSatelliteCount(int satelliteCount) {
		this.satelliteCount = satelliteCount;
	}

	public int getGpsDate() {
		return gpsDate;
	}

	public void setGpsDate(int gpsDate) {
		this.gpsDate = gpsDate;
	}
	
	public int getGpsTimeS() {
		return gpsTimeS;
	}
	
		public void setGpsTimeS(int gpsTimeS) {
		this.gpsTimeS = gpsTimeS ;
	}
	
	public int getgpsFixTime() {
		return gpsFixTime;
	}
	
		public void setgpsFixTime(int gpsFixTime) {
		this.gpsFixTime = gpsFixTime ;
	}	
	

}

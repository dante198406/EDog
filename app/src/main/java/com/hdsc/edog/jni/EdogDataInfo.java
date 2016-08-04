package com.hdsc.edog.jni;

public class EdogDataInfo {//电子狗数据的封装
	
	private int mSpeed; // 速度 km/h
	private int mSpeedLimit; // 当前点限速，速度
	private int mDistance; // 离当前限速点的距离
	private int mDirection; // 方向
	private boolean mIsAlarm; // 是否有报警
	private int mPosition;//位置
	private int mFirstFindCamera;
	private int mAlarmType; // 报警类型
	private int mgpsDate; // GPS秒数
	private boolean mGpsState; // true为定到位
	private float mMileageKM; // 里程数 km
	
	
	
	
	private int mBlockSpeed ;
	private int mPercent;
	private int mBlockSpace ;
	private String mVersion;
//	private int mRadarState; // 雷达状态 1正常，2不正常 3关闭
//	private int mRadarMode; // 雷达模式
//	private String mRaderType; // 雷达类型

	

	
	
	public EdogDataInfo(int speed, int speedLimit, int distance,
			int direction, int firstFindCamera, boolean isAlarm, int position, int alarmType, int gpsDate,
			boolean gpsState, float mileageKM ,int blockSpeed, int percent, int blockSpace, String version /*, int radarState,
			int radarMode, String raderType*/) {
		this.mSpeed = speed;
		this.mSpeedLimit = speedLimit;
		this.mDistance = distance;
		this.mDirection = direction;
		this.mFirstFindCamera = firstFindCamera;
		this.mIsAlarm = isAlarm;
		this.mPosition = position;
		this.mAlarmType = alarmType;
		this.mgpsDate = gpsDate;
		this.mGpsState = gpsState;
		this.mMileageKM = mileageKM;
	
		
		this.mBlockSpeed = blockSpeed;
		this.mPercent = percent;
		this.mBlockSpace = blockSpace;
		this.mVersion = version;
		/*this.radarState = radarState;
		this.radarMode = radarMode;
		this.raderType = raderType;*/
		
	}

	public int getmSpeed() {
		return mSpeed;
	}

	public void setmSpeed(int mSpeed) {
		this.mSpeed = mSpeed;
	}

	public int getmSpeedLimit() {//
		return mSpeedLimit;
	}

	public void setmSpeedLimit(int mSpeedLimit) {
		this.mSpeedLimit = mSpeedLimit;
	}

	
	public int getmDistance() {
		return mDistance;
	}

	public void setmDistance(int mDistance) {
		this.mDistance = mDistance;
	}

	public String getmDirection() {
		return GetStrDir(mDirection);
	}

	public void setmDirection(int mDirection) {
		this.mDirection = mDirection;
	}

	public int getmFirstFindCamera() {
		return mFirstFindCamera;
	}

	public void setmFirstFindCamera(int mFirstFindCamera) {
		this.mFirstFindCamera = mFirstFindCamera;
	}

	public boolean ismIsAlarm() {
		return mIsAlarm;
	}

	public void setmIsAlarm(boolean mIsAlarm) {
		this.mIsAlarm = mIsAlarm;
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}

	public int getmAlarmType() {
		return mAlarmType;
	}

	public void setmAlarmType(int mAlarmType) {
		this.mAlarmType = mAlarmType;
	}

	public int getmgpsDate() {
		return mgpsDate;
	}

	public void setmgpsDate(int mgpsDate) {
		this.mgpsDate = mgpsDate;
	}

	public boolean getmGpsState() {
		return mGpsState;
	}

	public void setmGpsState(boolean mGpsState) {
		this.mGpsState = mGpsState;
	}

	public float getmMileageKM() {
		return mMileageKM;
	}

	public void setmMileageKM(float mMileageKM) {
		this.mMileageKM = mMileageKM;
	}

	
	public int getMgpsDate() {
		return mgpsDate;
	}

	public void setMgpsDate(int mgpsDate) {
		this.mgpsDate = mgpsDate;
	}
	
		
	public int getmBlockSpeed() {
		return mBlockSpeed;
	}

	public void setmBlockSpeed(int mBlockSpeed) {
		this.mBlockSpeed = mBlockSpeed;
	}
	
	public int getmPercent() {
		return mPercent;
	}

	public void setmPercent(int mPercent) {
		this.mPercent = mPercent;
	}
	
	
	public int getmBlockSpace() {
		return mBlockSpace;
	}

	public void setmBlockSpace(int mBlockSpace) {
		this.mBlockSpace = mBlockSpace;
	}
	
	public String getmVersion() {
		return mVersion;
	}

	
	public void setmVersion(String mVersion) {
		this.mVersion = mVersion;
	}
	
	
/*
	public int getmRadarState() {
		return mRadarState;
	}

	public void setmRadarState(int mRadarState) {
		this.mRadarState = mRadarState;
	}

	public int getmRadarMode() {
		return mRadarMode;
	}

	public void setmRadarMode(int mRadarMode) {
		this.mRadarMode = mRadarMode;
	}

	public String getmRaderType() {
		return mRaderType;
	}

	public void setmRaderType(String mRaderType) {
		this.mRaderType = mRaderType;
	}
	*/
	
	private String GetStrDir(int direction)
	{
		if (direction >= 337.5 && direction <=22.5){
			return " 北 ";
		}else if (direction >= 22.5 && direction <= 67.5){
			return " 东北";
		}else if (direction >= 67.5 && direction <= 112.5){
			return " 东 ";
		}else if (direction >= 112.5 && direction <= 157.5){
			return " 东南";
		}else if (direction >= 157.5 && direction <= 202.5){
			return " 南 ";
		}else if (direction >= 202.5 && direction <= 247.5){
			return " 西南";
		}else if (direction >= 247.5 && direction <= 292.5){
			return " 西 ";
		}else if (direction >= 292.5 && direction <= 337.5){
			return " 西北";
		}
		return " 北 ";
	}
}

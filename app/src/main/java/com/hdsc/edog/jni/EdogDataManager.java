package com.hdsc.edog.jni;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.hdsc.edog.entity.EdogData;
import com.hdsc.edog.entity.GpsInfo;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;

import android.util.Log;
public class EdogDataManager {
	private static final String TAG = "EdogDataManager";
	
	private Context mContext;
	private SharedPreUtils sp;
	private static Map<String, String> ptTypeMap = null;
	private static Map<String, String> ptDirectMap = null;
	public static String eDogFilePath = null;
//	public static String logoPath = null;
	//public static String eDoglibPath = null;
	
//	public static	int  SWKTime = 0 ;
	
	public EdogDataManager(Context context){
		this.mContext = context;
		sp = SharedPreUtils.getInstance(context);
	}
	
	//是否开启红灯提醒 0：提醒 1：不提醒
	public boolean isRedLightOn(){
		return sp.getIntValue(Constants.W_RED_LIGHT)==0;
	}
	
	public void setRedLightOn(boolean state){
		sp.commitIntValue(Constants.W_RED_LIGHT, state==true? 0:1);
	}
	
	
	
	//是否开启限速提醒 0：提醒 1：不提醒
	public boolean isSpeedLimitOn(){
		return sp.getIntValue(Constants.W_SPEED_LIMIT)==0;
	}
	
	
	public void setSpeedLimitOn(boolean state){
		sp.commitIntValue(Constants.W_SPEED_LIMIT, state==true? 0:1);
	}
	
	
	//是否开启交通违规提醒 0：提醒 1：不提醒
	public boolean isTrafficRuleOn(){
		return sp.getIntValue(Constants.W_TRAFFIC_RULE)==0;
	}
	
	public void setTrafficRuleOn(boolean state){
		sp.commitIntValue(Constants.W_TRAFFIC_RULE, state==true? 0:1);
	}
	
	
	
	//是否开启安全提醒 0：提醒 1：不提醒
	public boolean isSafeOn(){
		return sp.getIntValue(Constants.W_SAFE)==0;
	}
	
	public void setSafeOn(boolean state){
		sp.commitIntValue(Constants.W_SAFE, state==true? 0:1);
	}
	
	
	
	//设置最高限速
	public void setMaxLimitSpeed(int speed){
		sp.commitIntValue(Constants.W_MAX_SPEED, speed);
	}
	//获取最高限速
	public int getMaxLimitSpeed(){
		int maxLimitSpeed = sp.getIntValue(Constants.W_MAX_SPEED);
		//JT_XZY
			if((maxLimitSpeed < 80 ) || (maxLimitSpeed >160) ){
				maxLimitSpeed = 120;
				setMaxLimitSpeed(maxLimitSpeed) ;
				setSpeedModify(0) ;
		}
		return maxLimitSpeed;
	}
	
	

	//设置速度微调
	public void setSpeedModify(int percent){
		sp.commitIntValue(Constants.W_SPEED_MODIFY, percent);
	}
	
	//获取速度微调值
	public int getSpeedModify(){
		return sp.getIntValue(Constants.W_SPEED_MODIFY);
	}
	
	//获取微调后的速度
	public float getModifiedSpeed(float speed){
		int value = sp.getIntValue(Constants.W_SPEED_MODIFY);
		
		
	//	Log.e("rd", "GPSADJ_value=" +  String.valueOf(value) );	
	//	Log.e("rd", "GPSADJ_speed=" +  String.valueOf(speed) );	
		//double percent = 1 + value/100;
		speed += ( speed* value)/100 ;
	//	Log.e("rd", "GPSADJ_speed2=" +  String.valueOf(speed) );			
		return  speed  ;   //   (float) (speed*percent);
	}

	public EdogDataInfo getEdogData(GpsInfo gpsInfo) {    //500MS 一次
		
		
		
		
		init();
		
		
		/*	
		if(gpsInfo==null){  //不会进入 这里
			
		
			SWKTime ++ ;
			if(SWKTime < 2){
				
			//	SWKTime = 0;
				return null;
					
			}
			
		//	gpsInfo.setGpsTimeS( (int)(System.currentTimeMillis()/1000) );  //=   System.currentTimeMillis();	
			Log.e("rd", "GPS_TimeS0 =" +  String.valueOf(System.currentTimeMillis()/1000) );	
		}
		SWKTime = 0;
		*/
		
//		Log.e("rd", "GPS_TimeS1 =" +  String.valueOf(gpsInfo.getGpsTimeS()) );	
		EdogData edogData = getEdogData(eDogFilePath,  gpsInfo);  //eDoglibPath,logoPath, 
		
				
		int speed =  gpsInfo.getSpeed(); // 速度 km/h
		int speedLimit = edogData.getmSpeedLimite(); // 当前点限速，速度
		int distance = edogData.getmDisToCamera(); // 离当前限速点的距离
		int direction = gpsInfo.getBearing(); // 方向
		int firstFindCamera = edogData.getmFirstFindCamera();
		boolean isAlarm = edogData.getmFindCamera() == 1 ? true : false; // 是否有报警
		int position = edogData.getmTaCode();
		int alarmType = edogData.getmVoiceCode(); // 报警类型
     	
		int blockSpeed = edogData.getmBlockSpeed(); // 区间平均速度
		int percent = edogData.getmPercent(); //区间完成%比 ;
			int blockSpace = edogData.getmBlockSpace();//区间距离 ;
			
		
		int gpsDate = gpsInfo.getGpsDate(); //当前时间
		boolean gpsState = false;
		if(gpsInfo.getSatelliteCount()>=3){
			gpsState = true;
		}
		
		
		
		
		
		
		 // true为定到位
		int  mileageKM = 0;// ；、、666.66f; // 里程数 kmfloat
		String version = String.valueOf(edogData.getmVersion()); // 版本号,数据
		

			EdogDataInfo edogDataInfo = new EdogDataInfo(speed, speedLimit,
					distance, direction, firstFindCamera, isAlarm, position, alarmType, gpsDate, gpsState,
					mileageKM, blockSpeed,  percent,blockSpace, version) ;
			
			
		return edogDataInfo;
			
	
		
		
	}
/*
	private static int getDistance(int distance) {
		if (distance <= 10) {
			return Math.round(distance);
		} else if (distance > 10 && distance <= 100) {
			return (distance / 10) * 10;
		} else if (distance > 100 && distance <= 1000) {
			return (distance / 10) * 10;
		} else {
			return distance;
		}
	}
*/
	
//	private public
	private	 static void init() {
		if (ptDirectMap == null) {
			ptDirectMap = new HashMap<String, String>();
			ptDirectMap.put("0", "TA0.mp3");
			ptDirectMap.put("1", "TA1.mp3");
			ptDirectMap.put("2", "TA2.mp3");
			ptDirectMap.put("3", "TA3.mp3");
			ptDirectMap.put("4", "TA4.mp3");
			ptDirectMap.put("5", "TA5.mp3");
			ptDirectMap.put("6", "TA6.mp3");
			ptDirectMap.put("7", "TA7.mp3");
			ptDirectMap.put("8", "TA8.mp3");
			ptDirectMap.put("9", "TA9.mp3");
			ptDirectMap.put("10", "TAA.mp3");
			ptDirectMap.put("11", "TAB.mp3");
			ptDirectMap.put("12", "TAC.mp3");
			ptDirectMap.put("13", "TAD.mp3");
			ptDirectMap.put("14", "TAE.mp3");
			ptDirectMap.put("15", "TAF.mp3");

		}
		if (ptTypeMap == null) {
			ptTypeMap = new HashMap<String, String>();
			ptTypeMap.put("0", "TD0.mp3");
			ptTypeMap.put("固定测速照相", "1");
			ptTypeMap.put("NC", "2");
			ptTypeMap.put("限高", "3");
			ptTypeMap.put("流动照相区", "4");
			ptTypeMap.put("违章监控路段", "5");
			ptTypeMap.put("为区间测速终点", "6");
			ptTypeMap.put("安全车距测速照相", "7");
			ptTypeMap.put("有高架测速照相", "8");
			// ptTypeMap.put("限重", "9");
			ptTypeMap.put("隧道内测速照相", "A");
			ptTypeMap.put("隧道口测速照相", "B");
			ptTypeMap.put("区间测速照相", "C");
			ptTypeMap.put("多雾路段", "D0");
			ptTypeMap.put("易落石路段", "D1");
			ptTypeMap.put("事故多发路段", "D2");
			ptTypeMap.put("急下坡路段", "D3");
			ptTypeMap.put("交流道", "D4");
			ptTypeMap.put("为系统交流道", "D5");
			ptTypeMap.put("机场路段", "D6");
			ptTypeMap.put("学校路段", "D7");
			ptTypeMap.put("商场路段", "D8");
			ptTypeMap.put("遵守交通规则路段", "D9");
			ptTypeMap.put("有加气站", "DA");
			ptTypeMap.put("有隧道，请开头灯", "DB");
			ptTypeMap.put("地下道", "DC");
			ptTypeMap.put("禁止左转弯", "DD");
			ptTypeMap.put("禁止右转弯", "DE");
			ptTypeMap.put("禁止调头", "DF");
			ptTypeMap.put("警察局", "E0");
			ptTypeMap.put("火车站", "E1");
			ptTypeMap.put("有加油站", "E2");
			ptTypeMap.put("有收费站", "E3");
			ptTypeMap.put("有休息服务区", "E4");
			ptTypeMap.put("禁停路段", "E5");
			ptTypeMap.put("越线违章拍照路段", "E6");
			ptTypeMap.put("单行道请勿逆向行驶", "E7");
			ptTypeMap.put("右则为公交车道照相", "E8");
			ptTypeMap.put("有汽车美容装饰中心", "E9");
			ptTypeMap.put("有汽车修理厂", "EA");
			ptTypeMap.put("有汽车站", "EB");
			ptTypeMap.put("为风景区", "EC");
			ptTypeMap.put("山区路段", "ED");
			ptTypeMap.put("冰雪路段", "EE");
			ptTypeMap.put("为检查站", "EF");
			ptTypeMap.put("K 频雷达测速区", "F0");
			ptTypeMap.put("为市政府", "F1");
			ptTypeMap.put("闯红灯拍照", "F2");
			ptTypeMap.put("闯红灯压黄线拍照", "F3");
			ptTypeMap.put("尾号限行路段", "F4");
			ptTypeMap.put("有减速带", "F5");
			ptTypeMap.put("有高架出口", "F6");
			ptTypeMap.put("违规监控照相", "F7");
			ptTypeMap.put("急转弯路段", "F8");
			ptTypeMap.put("高速公路出口", "F9");
			ptTypeMap.put("Ka 频雷达", "FA");
			ptTypeMap.put("铁路道口", "FB");
			ptTypeMap.put("禁行路段", "FC");
			ptTypeMap.put("有立交桥", "FD");
			ptTypeMap.put("私家车限时通行路段", "FE");
		}

	}

	public native static EdogData getEdogData(String filePath, GpsInfo gpsInfo); //String logoPath,  String libPath,

     static {
		System.loadLibrary("tuzhi_edog");// 载入tuzhi_edog.so
	}

}

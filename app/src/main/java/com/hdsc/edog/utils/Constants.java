
package com.hdsc.edog.utils;

public class Constants {
	
	public static final String MUTE = "mute";//静音 0表示不静音，1表示静音
	public static final String MUTE_FORVER = "mute_forver";
	
	public static final String W_RED_LIGHT       = "闯红灯报警提示   :   开启、关闭 闯红灯 报警";
	public static final String W_SPEED_LIMIT     = "超速报警提示音  :   车速超过相应车速，超速“当当” 音提醒";
	public static final String W_TRAFFIC_RULE    = "监控类报警提示  :   开启、关闭监控类的 报警";
	public static final String W_SAFE            = "安全信息类提示  :   一般指不带测速的报警点（如：隧道。）";
	public static final String W_RADAR_MODE      = "流动雷达模式       :   一般指可以随意在不同地点测速的设备 。";
	public static final String W_MAX_SPEED       = "行车最高限速       :   只要行车速度超过这个值，就报警提醒。 ";
	public static final String W_RADAR_MUTE_SPEED= "雷达静音速度       :   车速低于这个值时，有雷达的话也不会发音。 ";
	public static final String W_SPEED_MODIFY    = "GPS速度微调         :   GPS测速与仪表盘车速不一致时，校正误差%比  ";
	
	public final static String BOOT_START = "开机直接进入后台运行";//0表示不是，1表示是
	public final static String SMARTTCLOUND_AUTOUPDATE = "自动升级";
	public final static String SMARTTCLOUND_AUTOPOST = "自动上传修正数据";
	public final static String SMARTTCLOUND_GAINFLOW = "省流量模式";
	
	public static final String RECOVERY = "恢复出厂设置";
	//public static final String EDOG_FILE_VERSION = "电子狗数据文件版本";
	
	public static final String RADAR_COM = "雷达COM口";
	public static final String RADAR_COM_P = "雷达COM口位置";
	
	public static final String IS_UPDATED = "程序是否为刚刚更新";//0表示不是，1表示是
	
	public static final String IS_FIRST_USE = "程序是否是第一次使用";//false表示不是，true表示是
	
	public static final String coms[] = {"ttyS0","ttyS1", "ttyS2", "ttyS3", "ttyS4", "ttyS5", "ttyS6", "ttyS7", "ttyMT1",
			"ttyMT2", "ttyMT3", "ttyMT4","ttyMT5","ttyMT6", "ttyAMA0", "ttyAMA1", "ttyAMA2", "ttyAMA3" , 
			"ttyCOM0" ,"ttyCOM1" ,"ttyCOM2" ,"关闭com口" };
}




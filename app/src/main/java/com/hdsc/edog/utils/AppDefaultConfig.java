package com.hdsc.edog.utils;

/**
 * 请严格按照注释修改app默认设置
 * @author Duyuan
 * 2015年10月20日 下午3:48:02
 */
public class AppDefaultConfig { 

	/**
	 * 设置当前app是否为公版，false 表示 私人 家，///     true表示是 公版 不修改配置
	 */
	public static final boolean isCommonVersion = false;

	/**
	 * 设置app是否开机启动，false表示 不启动，true表示是开机启动
	 * 
	 * 华芯未来 ：false
	 * 车连连  ：false  ; 
	 * 卡仕特  ：false  ;
	 */
	public static final boolean autoStarted =  true;  // true; false

	/**
	 * 设置app第一次安装后的默认雷达串口号， 0 代表"ttyS0", 1 代表"ttyS1", 2 代表"ttyS2", 3 代表"ttyS3",
	 * 4 代表"ttyS4", 5 代表"ttyS5", 6 代表"ttyS6", 7 代表"ttyS7", 8 代表"ttyMT1",
	 * 9 代表"ttyMT2", 10 代表"ttyMT3",  11 代表"ttyMT4", 12 代表"ttyMT5", 13 代表"ttyMT6",              11+ 代表"ttyAMA0", 12 代表"ttyAMA1",
	 * 13 代表"ttyAMA2", 14 代表"ttyAMA3" 15 代表"ttyAMA4"  16 代表"ttyAMA5"  17 代表"ttyAMA6"            xxx 18 代表"ttyAMA3"
	 * 18 代表"ttyCOM0"     19 代表"ttyCOM1"   20 代表"ttyCOM2"   
	 * 
	 * 
	 * 
	 *  14  +3  代表"关闭com口",  
	 *  
	 *  
	 * 瑞芯微     0 代表"ttyS0",
	 * 华芯未来 ： 启程 ：1 代表"ttyS1",
      * 启程 ：   2 代表"ttyS2",  1 代表"ttyS1", 
	 *  
	 * 卡仕特  ： 3 代表"ttyS3", 
	 * 路盛   ：4 代表"ttyS4 ", 
	 * 智慧： 
	 * 
	 	 *  车连连  ：10 ; //   ttyMT3 ;  、
	 *  
	 *  普方达  ：8; //   ttyMT1  ;  、增加 开启 雷达电源 。*   
	 *  骏泰达  ：8; //   ttyMT1  ;  、*   
	 *  九     ：19; //   ttyCOM1  ; 
	 */
	public static final int ttyNo = 1 ; //   

}

package com.hdsc.edog.jni;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.util.Log;

import com.hdsc.edog.TuzhiService;
import com.hdsc.edog.entity.GpsInfo;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;

public class RadarDataManager {
	private static final String TAG = "RadarDataManager";

	private static FileDescriptor mFd;
	private static FileInputStream mFileInputStream;
	private static FileOutputStream mFileOutputStream;

	private SharedPreUtils sp;
	private Context mContext;

	public RadarDataManager(Context context) {
		this.mContext = context;
		sp = SharedPreUtils.getInstance(context);
	}

	// 设置雷达模式  1 2 3 4
	public void setRadarMode(int mode) {
		sp.commitIntValue(Constants.W_RADAR_MODE, mode);
	}

	// 获取雷达模式
	public int getRadarMode() {

		int RadarMode = sp.getIntValue(Constants.W_RADAR_MODE);
		if ((RadarMode < 1 )||( RadarMode >4)) {
			RadarMode = 1;
			setRadarMode(RadarMode);
			
			setRadarMuteSpeed(40);
			

		}
		return RadarMode;

	}
	
	
		// 设置雷达静音速度
	public void setRadarMuteSpeed(int speed) {
		sp.commitIntValue(Constants.W_RADAR_MUTE_SPEED, speed);
	}

	// 获取雷达静音速度
	public int getRadarMuteSpeed() {

		int RadarMuteSpeed = sp.getIntValue(Constants.W_RADAR_MUTE_SPEED);
		//if (RadarMuteSpeed == 0) {
		//	RadarMuteSpeed = 40;
		//	setRadarMuteSpeed(RadarMuteSpeed);
			
			

	//	}
		return RadarMuteSpeed;
	}

	private String noSingle = "AA55";
	private String laser = "CC80";
	private String[] xBan = { "CC43", "CC42", "CC41", "CC40" };
	private String[] kUBan = { "CC4B", "CC4A", "CC49", "CC48" };
	private String[] kBan = { "CC53", "CC52", "CC51", "CC50" };
	private String[] kABan = { "CC5B", "CC5A", "CC59", "CC58" };
	private String CC55Single = "CC55";
	private String SADSingle = "CC55F7";
	
	private String radarData =  ""   ;
	private int hasRead_all = 0 ;
	

	// 解析雷达信号数据

	public int parseRadarData(byte[] data,int hasRead) {  //long MainLoop,
		// byte Last_RD_CONT = 0;
		// byte RD_CONT = 0;
		// byte RD_ChkSum = 0;
		// byte RD_Temp = 0;
	
		
		
		if(hasRead_all == 0) {			
			radarData = ""  ;
		}
		
//	 Log.e(TAG,"hasRead =" +String.valueOf(hasRead));
		
		 
		String  TradarData = ToolUtils.bytesToHexString(data,hasRead);
		
		
		hasRead_all += hasRead ;
		
		
		if(hasRead_all >64 ) {  // 32
			
			
			hasRead_all = 0 ;
		}
		else{
			radarData += TradarData ;
		}
		
		
			
		
	//	 Log.e(TAG,"hasRead_all =" +String.valueOf(hasRead_all));
		
	//	 Log.e(TAG, "TradarData=" + TradarData);
	//	 Log.e(TAG, "radarData=" + radarData);
		 
		if (!radarData.contains(noSingle)) { 
		 
		 
			
			if (radarData.contains(laser)) {
				hasRead_all = 0 ;
				return (1 << 4);// Laser
			}
			
			for (int i = 0; i < kBan.length; i++) {
				if (radarData.contains(kBan[i])) {
					
				//	 Log.e("dy1","SADRDkBan.length =" + String.valueOf(kBan.length) );
				//	  Log.e("dy1","SADRD_I =" + String.valueOf(i) );
					hasRead_all = 0 ;
					return (2 << 4) + i;// KBan
				}
			}
			for (int i = 0; i < kABan.length; i++) {
				if (radarData.contains(kABan[i])) {
					
				//	 Log.e("dy1","SADRDkABan.length =" + String.valueOf(kABan.length) );
				//	  Log.e("dy1","SADRD_I =" + String.valueOf(i) );
					hasRead_all = 0 ;
					return (3 << 4) + i;// KABan
				}
			}
			for (int i = 0; i < kUBan.length; i++) {
				if (radarData.contains(kUBan[i])) {
					hasRead_all = 0 ;
					return (4 << 4) + i;// kUBan
				}
			}
			for (int i = 0; i < xBan.length; i++) {
				if (radarData.contains(xBan[i])) {
					hasRead_all = 0 ;
					return (5 << 4) + i;// XBan
				}
			}
			
		 
	} 
		 
		 
		 

		
		
		//Log.e(TAG, "radarData=" + radarData);
		
		// radarData = "AA 55 AA 55 AA 55 AA 55 CC 55 F7 11 22 33 44 CC 55 F7 11 22 33 44";
		// 0 1 2 3 -4 5 6 7 8 9 10
		if (radarData.contains(noSingle)) { //
			
			 
			if(hasRead_all < 22 ) {  // 22
				
					
				return -1;
			}
			
			
			

		// 	hasRead_all = 0 ;
			

			// Log.e(TAG,"checkRadar:"+ checkData );

	         //	Log.e(TAG, "radarData==" + radarData);

		      int lastIndex = radarData.lastIndexOf(noSingle);   //最后1个AA55
					
			//	 Log.e(TAG,"lastIndex 0 =" +String.valueOf(lastIndex));
			
			String checkData = radarData.substring(lastIndex+4);// 取得AA55AA55AA55AA55后的14位数据

		//	Log.e(TAG, "str  checkData=" + checkData);

			// Log.e(TAG,"data 0 =" +String.valueOf(data[0]));

			// Log.e(TAG,"data 0 =" +String.valueOf(data[1]));
			// Log.e(TAG,"data 0 =" +String.valueOf(data[2]));

			// Log.e(TAG,"data 0 =" +String.valueOf(data[3]));
			// Log.e(TAG,"data 10 =" +String.valueOf(data[10]));
			
			
	//		Log.e(TAG, "str  checkDatalength= " + String.valueOf(checkData.length() ));
			

			
		
			
			
			
			

			if (checkData.contains(CC55Single)) {
				
				
				
				
				if (checkData.contains(SADSingle)) { // &&
														// checkData.contains(SADSingle)

				//	Log.e(TAG, "SADSingle   SADSingle -ok :");
					
					
					
					if(checkData.length() >= 28 ){
						
						hasRead_all = 0 ;	
					//	return -1;
					}
					
					
					

					// result为返回的结果
					int result = RadarDataManager.checkRadar(
							checkData);// TuzhiService.getGpsInfo()EdogDataManager.eDoglibPath, 

					return result;
				}
				
			//	if(MainLoop < 120 )  return 8;
					
			//	else  return 0 ;// 0;
				return 0;
				
			}
			return -1;// 0;// 没有雷达信号
		}

		
	//	Log.e(TAG, "SADRD_radarData==" + radarData);
		
//==========
		
		
		return -1;// 鏈煡淇″彿

		/*
		 * 
		 * if (radarData.contains(laser)) { return 0x10;// Laser }
		 * 
		 * for (int i = 0; i < kBan.length; i++) { if
		 * (radarData.contains(kBan[i])) { return 2;// KBan } } for (int i = 0;
		 * i < kABan.length; i++) { if (radarData.contains(kABan[i])) { return
		 * 3;// KABan } } for (int i = 0; i < kUBan.length; i++) { if
		 * (radarData.contains(kUBan[i])) { return 4;// kUBan } } for (int i =
		 * 0; i < xBan.length; i++) { if (radarData.contains(xBan[i])) { return
		 * 5;// XBan } } return -1;// 未知信号
		 */
	}
	
	
	
	

	/*
	 * private String[] strong0 = { "CC43", "CC4B", "CC53", "CC5B" }; private
	 * String[] strong1 = { "CC42", "CC4A", "CC52", "CC5A" }; private String[]
	 * strong2 = { "CC41", "CC49", "CC51", "CC59" }; private String[] strong3 =
	 * { "CC40", "CC48", "CC50", "CC58" };
	 * 
	 * 
	 * 
	 * // 解析雷达信号数据 public int getRadarSingle(byte[] data) { String radarData =
	 * ToolUtils.bytesToHexString(data); for (int i = 0; i < strong0.length;
	 * i++) { if (radarData.contains(strong0[i])) { return 0;// weak } } for
	 * (int i = 0; i < strong1.length; i++) { if
	 * (radarData.contains(strong1[i])) { return 1;// middle } } for (int i = 0;
	 * i < strong2.length; i++) { if (radarData.contains(strong2[i])) { return
	 * 2;// strong } } for (int i = 0; i < strong3.length; i++) { if
	 * (radarData.contains(strong3[i])) { return 3;// strong } } return -1;//
	 * 雷达异常 }
	 */

	public FileDescriptor initRadar() {
		
	  //雷达串口
		
		 //	File device = new File("/dev/ttyS1");
	//	 	File device = new File("/dev/ttyS2");	
        	
      //	File device = new File("/dev/ttyS3");	
      	
		String devName = sp.getStringValue(Constants.RADAR_COM);
		
		if(devName==null){
		
		
			
		devName="关闭com口";  // 2015-02-24  ttyS0
	
		
		}
		
	//	 Log.e(TAG,"devName  =" + devName);  //.replace("ttys", "")
		
		if(devName=="关闭com口") {
			
			mFd = null;
			
		}
		 else {	 
		 File device = new File("/dev/"+devName);
			
		device.getAbsolutePath();
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					// throw new SecurityException();
				}
			} catch (Exception e) {
			//	e.printStackTrace();
				// throw new SecurityException();
			}
		}
		mFd = openRadar(device.getAbsolutePath(), 9600, 0);

	}
	
		
		
		return mFd;
	}

	

	static {

		System.loadLibrary("tuzhi_edog");// 载入tuzhi_edog.so
	}

	
	
	// 打开雷达串口
	private native static FileDescriptor openRadar(String path, int baudrate,
			int flags);

	// 检查雷达信号

	// private native static int checkRadar(String radarData, GpsInfo gpsInfo);

	private native static int checkRadar(String radarData);// ,String libPath, 
																			// GpsInfo
																			// gpsInfo
}

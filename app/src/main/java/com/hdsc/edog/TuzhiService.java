package com.hdsc.edog;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hdsc.edog.entity.GpsInfo;
import com.hdsc.edog.jni.DataPlay;
import com.hdsc.edog.jni.DataShow;
import com.hdsc.edog.jni.EdogDataInfo;
import com.hdsc.edog.jni.EdogDataManager;
import com.hdsc.edog.jni.RadarDataManager;
import com.hdsc.edog.net.HttpRequestManager;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;

public class TuzhiService extends Service {
	private final String TAG = "TuzhiService";
	
	/**
	 * 是否是开机启动
	 */
	public static boolean isSystemBoot;
	
	public static int KEY_ISSHOW_DIALOG = 0;
		
	public static long GPRSTOTAL = 0;// 流量统计总数

	private LocationManager mLoctionManager;
	private GpsLocationListener mLocationListener;
	private final long minTime = 500;
	private final float minDistance = 0;
//	public static double lat = 22.564531, lon = 113.958646;// 当前的经纬度，实时路况页面用�?
	private Calendar calendar;
	private SimpleDateFormat format;
	
	private  GpsInfo gpsInfo = null;  //static
	private EdogDataInfo edogDataInfo = null;
	//private boolean run_thread = true;
	private boolean radar_run = true;
	private boolean edog_run = true;

//	private int radarType = -1 ;
	private int Last_radarType = -1 ;
	private int radarDisp = -1 ;
	
	private boolean radarCFOK = false  ;    // == 0  雷达 连线 ok ； ==雷达 CC55 错误  ； == 2：雷达CC55F7  错误 ；
	
	private int 	Rd_city_cont = 0 ;
		
		private	 int   TIME_ERR = -1 ;
		private	 int   TIME_RD = -1  ;
		private int Rx_Rdsingle = 0  ; 
	private  long  MainLoop = 0 ;	//static
		

	 private	int	 BlockSpeedLimit 	= 0 ;  ////区间报警 限速值  
	private	int	 BlockSpeedTime 	= 0 ;
	private	int	 MAINLoopCont  	= 0 ;
		private	int	 SAD_RD_ERR  	= 0 ;
	private	int	 SAD_RD_Time  	= 5000 ;
		
	private	int	speed_chk = 0;
	
	// 雷达线程
	private Thread radarThread;
//	private Thread radarMonitorThread;
	private int radarType = 0;
	// 雷达串口文件流
	private  FileDescriptor mFd;  //static
	private  FileInputStream mFileInputStream;  //static
	// 电子狗播报线程
	private Thread edogThread;
	
	private DataShow dataShow;
	private DataPlay dataPlay;
	private RadarDataManager radarDataManager;
	private EdogDataManager edogDataManager;
	
	public final static String ACTION_CRREATE_SPEED_FLOATVIEW = "tuzhi.edog.androidapp.createspeedfloatview";
	public final static String ACTION_REMOVE_SPEED_FLOATVIEW = "tuzhi.edog.androidapp.removespeedfloatview";
	public final static String ACTION_CRREATE_RADAR_FLOATVIEW = "tuzhi.edog.androidapp.createradarfloatview";
	public final static String ACTION_REMOVE_RADAR_FLOATVIEW = "tuzhi.edog.androidapp.removeradarfloatview";
	public final static String ACTION_CRREATE_ALL_FLOATVIEW = "tuzhi.edog.androidapp.createallfloatview";
	public final static String ACTION_REMOVE_ALL_FLOATVIEW = "tuzhi.edog.androidapp.removeallfloatview";
	float x1, x2, y1, y2;
	private FloatViewBroadcastReciver folatViewReceiver;
	// 定义浮动窗口布局
	RelativeLayout mSpeedFloatLayout;
	RelativeLayout mRadarFloatLayout;   //LinearLayout mRadarFloatLayout;
	WindowManager.LayoutParams wmSpeedParams;
	WindowManager.LayoutParams wmRadarParams;
	// // 创建浮动窗口设置布局参数的对�?
	WindowManager mWindowManager;
	public static ImageView fImage;
	public static TextView fSpeed, fDistance;
	public static LinearLayout llWarn, llSpeed;
	
	public static ImageView mRadarFloatView;
	public static boolean speedfvIsVisible = false;
	public static boolean radarfvIsVisible = false;
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	//	Log.v(TAG, "BiaPing1");
		
		super.onCreate();
	//	Log.v(TAG, "BiaPing2");
		SharedPreUtils sp = SharedPreUtils.getInstance(this);
		
	//	Log.v(TAG, "BiaPing3");
//		sp.commitIntValue(SharedPreUtils.KEY_ISSHOW_DIALOG, 1);
		KEY_ISSHOW_DIALOG = 1;
		Notification notification = new Notification(R.drawable.ic_launcher,
				"wf update service is running", System.currentTimeMillis());
		startForeground(0, notification);
	//	Log.v(TAG, "oncreate");
	//	Log.v(TAG, "BiaPing5");
		
		//悬浮窗口广播
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(ACTION_CRREATE_SPEED_FLOATVIEW);
				intentFilter.addAction(ACTION_REMOVE_SPEED_FLOATVIEW);
				intentFilter.addAction(ACTION_CRREATE_RADAR_FLOATVIEW);
				intentFilter.addAction(ACTION_REMOVE_RADAR_FLOATVIEW);
				intentFilter.addAction(ACTION_CRREATE_ALL_FLOATVIEW);
				intentFilter.addAction(ACTION_REMOVE_ALL_FLOATVIEW);
				folatViewReceiver = new FloatViewBroadcastReciver();
				registerReceiver(folatViewReceiver, intentFilter);

				
		dataShow = new DataShow(this);
		dataPlay = new DataPlay(this);
		radarDataManager = new RadarDataManager(this);
		edogDataManager = new EdogDataManager(this);
		
		calendar=java.util.Calendar.getInstance();    
	    format=new java.text.SimpleDateFormat("yyyyMMdd");    
	    
		mLoctionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gpsInfo = new GpsInfo();
//		gpsInfo.setLat(2628701);
		// 开始定位
		location(mLoctionManager);
		
		ReceiveBroadCast receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("radar.com.change");
        registerReceiver(receiveBroadCast, filter);
        
		// 启动雷达线程
     //   final int KEY_RARAR_OPEN = 0x15 ;
        
		radarThread = new Thread(radarRunnable);
		radarThread.start();
		//启动雷达状态监控线程
	//	radarMonitorThread = new Thread(radarMonitorRunnable);
	//	radarMonitorThread.start();
		// 启动电子狗线程
		edogThread = new Thread(edogRunnable);
		edogThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
	
		/*
		if (TuzhiApplication.isCurrent(this)) {
			ToolUtils.getInstance().exitNotify(this);
			// 雷达不可用
		//	if (mFd == null) {
	//			radarType = -1;
		//		mHandler.sendEmptyMessage(1);
		//	}
		}
		*/
		
		if (TuzhiApplication.isCurrent(this)) {
			ToolUtils.getInstance().exitNotify(this);
			removeSpeedFloatView();
		}
		
		
		if(isSystemBoot){
			
			
		Log.e("dd", "isMinMode 0 ");
		//	TuzhiService.
			isSystemBoot = false;
			
			MainActivity.isMinMode = true;
			
		//	speedfvIsVisible = false;
		//	radarfvIsVisible = false;
			
	//		TuzhiApplication.operateFloatView(this, TuzhiService.ACTION_CRREATE_SPEED_FLOATVIEW);
	//		TuzhiApplication.operateFloatView(this, TuzhiService.ACTION_CRREATE_RADAR_FLOATVIEW);
			//	TuzhiApplication.operateFloatView(this, TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
			//remove float
			
			
		TuzhiApplication.operateFloatView(this,TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
			
			ToolUtils.getInstance().showRunBgNotify(this);
			TuzhiApplication.exit();
			
		//	TuzhiApplication.operateFloatView(this,TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
			
		}
		
		
	//	Log.e("dd", "isMinMode 0 ");
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 开始定位，此定位是用GPS定位
	 * 
	 * @param gpsManager
	 */
	private void location(LocationManager gpsManager) {
		Location location = gpsManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		printGpsLocation(location);
		
	/*	
		if (location == null) {
			Toast.makeText(this, "no valid GPS information ",Toast.LENGTH_SHORT).show();
		}
	*/	
		
		
		mLocationListener = new GpsLocationListener();
		gpsManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				minTime, minDistance, mLocationListener);
//		gpsManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//				minTime, minDistance, mLocationListener);
		gpsManager.addGpsStatusListener(statusListener);  //
	}

	//long currentTime;

	public void printGpsLocation(Location location) {
		
	//gps没变化 不会执行到这里 。	
	//	currentTime = System.currentTimeMillis();
		// Message msg = EeyesActivity.locationHandler.obtainMessage();
		// msg.obj = location;
		// msg.sendToTarget();
		if (location != null) {
			/*
			 * location.getAccuracy(); 精度 location.getAltitude(); 高度 : 海拔
			 * location.getBearing(); 导向 location.getSpeed(); 速度
			 * location.getLatitude(); 纬度 location.getLongitude(); 经度
			 * location.getTime(); UTC时间 以毫秒计
			
			Log.d(TAG,
					"Accuracy : " + location.getAccuracy() + "\nAltitude : "
							+ location.getAltitude() + "\nBearing : "
							+ location.getBearing() + "\nSpeed : "
							+ location.getSpeed() + "\nLatitude : "
							+ location.getLatitude() + "\nLongitude : "
							+ location.getLongitude() + "\nTime : "
							+ location.getTime());
			
			 */
			
			//gpsInfo = new GpsInfo();
			gpsInfo.setAltitude((int) location.getAltitude());
			//gpsInfo.setBearing((float) location.getBearing());
			gpsInfo.setLat((int) (location.getLatitude() * 100000));
			gpsInfo.setLng((int) (location.getLongitude() * 100000));
			
		gpsInfo.setGpsTimeS((int) (location.getTime() / 1000));

		
		//int 
		int Tspeed = (int)( edogDataManager.getModifiedSpeed( location.getSpeed()) *3.60f );
  	if (gpsInfo.getgpsFixTime() > 0  && Tspeed > 10){
		
  	    if((Tspeed - gpsInfo.getSpeed() < 8 ) || ( speed_chk > 4) ){
  	
  		 gpsInfo.setSpeed(Tspeed); 
  	   	speed_chk = 0;
  		 
     	 }
  		
		
	}
  	else if(gpsInfo.getgpsFixTime() >0   ){	 //v1.2.4
  		gpsInfo.setSpeed(Tspeed);
		//	gpsInfo.setSpeed((int)( edogDataManager.getModifiedSpeed( location.getSpeed())*3.60f));
  	}
  	
  	speed_chk ++ ;
  	
			gpsInfo.setGpsDate(Integer.parseInt(format.format(calendar.getTime())));
			
		

		if(	gpsInfo.getSpeed() >=5 ){			
			gpsInfo.setBearing((int) location.getBearing());
			}
			

			/*
			 * if(EeyesActivity.tvBear != null)
			 * EeyesActivity.tvBear.setText(String
			 * .format(getResources().getString
			 * (R.string.eeyes_bear),gpsinfo.m_Bearing));
			 * if(EeyesActivity.tvLate != null)
			 * EeyesActivity.tvLate.setText(String
			 * .format(getResources().getString
			 * (R.string.eeyes_late),gpsinfo.m_Lat)); if(EeyesActivity.tvLongi
			 * != null)
			 * EeyesActivity.tvLongi.setText(String.format(getResources(
			 * ).getString(R.string.eeyes_longi),gpsinfo.m_Lng));
			 */
		
			
		} else {
			gpsInfo = null;
			
			
			
			
			/*
			 * if(EeyesActivity.tvBear != null)
			 * EeyesActivity.tvBear.setText(String
			 * .format(getResources().getString(R.string.eeyes_bear),0));
			 * if(EeyesActivity.tvLate != null)
			 * EeyesActivity.tvLate.setText(String
			 * .format(getResources().getString(R.string.eeyes_late),0));
			 * if(EeyesActivity.tvLongi != null)
			 * EeyesActivity.tvLongi.setText(String
			 * .format(getResources().getString(R.string.eeyes_longi),0));
			 */
		}

	}

	public class GpsLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
		//	Log.d(TAG, "Location = " + location);
			printGpsLocation(location);
		}

		public void onProviderDisabled(String provider) {
			Log.d(TAG, "ProviderDisabled : " + provider);
		}

		public void onProviderEnabled(String provider) {
			Log.d(TAG, "ProviderEnabled : " + provider);
		}

		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "StatusChanged : " + provider + status);
			switch (status) {
			// GPS状态为可见�?
			case LocationProvider.AVAILABLE:
				Log.i(TAG, "当前GPS状态为可见状态");
				break;
			// GPS状态为服务区外�?
			case LocationProvider.OUT_OF_SERVICE:
				Log.i(TAG, "当前GPS状态为服务区外状态");
				break;
			// GPS状态为暂停服务�?
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i(TAG, "当前GPS状态为暂停服务状态");
				break;
			}
		}
	}

		
	/**
	 * 卫星状态监听器
	 */
	
	
	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号

	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		@SuppressLint("ShowToast")
		public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			GpsStatus status = locationManager.getGpsStatus(null); // 取当前状�?
			
		//	Log.d(TAG, "status卫星：" + status );
				
				if ( (gpsInfo != null )&&( MainActivity.ivGps!=null)) {	
				gpsInfo.setSatelliteCount(updateGpsStatus(event, status));
//				Toast.makeText(TuzhiService.this,
//						"搜索到卫星个数：" + String.valueOf(gpsInfo.getSatelliteCount()),
//						Gravity.CENTER_HORIZONTAL).show();
				if(gpsInfo.getSatelliteCount()<3){
//					Toast.makeText(TuzhiService.this,
//							"卫星状态不正常",
//							Gravity.CENTER_HORIZONTAL).show();
					MainActivity.ivGps.setImageResource(R.drawable.no_gps);
				}else{
//					Toast.makeText(TuzhiService.this,
//							"卫星状态正常",
//							Gravity.CENTER_HORIZONTAL).show();
					gpsInfo.setgpsFixTime(8);
					
					
					MainActivity.ivGps.setImageResource(R.drawable.has_gps);
				}
		//		Log.d(TAG, "A搜索到卫星个数：" + gpsInfo.getSatelliteCount());
				
			}
			
			
		}
	};
	
	
	
	
	
	
	
/*
	public static GpsInfo getGpsInfo(){
		
		return gpsInfo;
	}
	
	
		public static int getGpsDate(){
		
		return gpsInfo.getGpsDate();
	}
	
	*/
	
	private int updateGpsStatus(int event, GpsStatus status) {
		int result = 0;
		if (status == null) {
			result = 0;
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			numSatelliteList.clear();
			int count = 0;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				numSatelliteList.add(s);
				count++;
			}
			result = numSatelliteList.size();
		}

		return result;
	}

	
	
	@Override
	public void onDestroy() {  //退出程序 
		Log.v(TAG, "onDestroy");
		KEY_ISSHOW_DIALOG = 0;
		GPRSTOTAL = 0;
		stopForeground(true);
		ToolUtils.getInstance().exitNotify(this);
		HttpRequestManager.getInstance().shutDownThreadPool();
		if (mLoctionManager != null && mLocationListener != null)
			mLoctionManager.removeUpdates(mLocationListener);
		//run_thread = false;
		radar_run = false;
		edog_run = false;
		radarThread.interrupt();
	//	radarMonitorThread.interrupt();
		edogThread.interrupt();
		
		dataPlay.close();  //关闭所有语音
		
	//	final int KEY_RARAR_CLOSE = 0x16 ;
		
		
		unregisterReceiver(folatViewReceiver);
	//	stopForeground(0,notification);  //  startForeground(0, notification);
		super.onDestroy();
	}

	
	// 电子狗播报线程,接收电子狗信号
	Runnable edogRunnable = new Runnable() {
		@Override
		public void run() {
	
		//	List<String> audioList = new ArrayList<String>();
			
		//	Log.e("SADA", "SAD-run "   );
			
		//	edogDataManager.init();
		//	try {
			
			while (edog_run) {   //长时间这个 会 退出
				
		    		
			//	 (System.currentTimeMillis() - MainLoop ) < 500 ) && ( (System.currentTimeMillis() - MainLoop ) >= 0 )
				
		//	if(( System.currentTimeMillis() - MainLoop ) >= 500  || ( System.currentTimeMillis() - MainLoop ) < 0 ) {	
				
				MainLoop =	System.currentTimeMillis();	
				
		    	MAINLoopCont ++ ;
				
		    	if(MainActivity.startMuteTime > 0){
		    	
		    		MainActivity.startMuteTime -- ;
		    		if(MainActivity.startMuteTime == 0 && DataPlay.isPlaying){
		    		
		    		//	MainActivity.setMute2(false); 
						
						 DataPlay.isPlaying  =  false;
					//	muteHandler.sendEmptyMessage(0);
						 //mHandler.sendEmptyMessage(0);
					}
				 }
		    	
				
		    	
		    	
		    	
		    	
		    	int TgpsFixTime = gpsInfo.getgpsFixTime() ;
		    	
		      if(	(TgpsFixTime > 1) && ( TgpsFixTime <= 8) ) {
		    	  TgpsFixTime --;
		    	  gpsInfo.setgpsFixTime(TgpsFixTime); 
		      }		    	
		    	
		     // edogDataInfo.setmMileageKM
				edogDataInfo = edogDataManager.getEdogData(gpsInfo);
	
		     // edogDataInfo.setmMileageKM(SAD_RD_Time)   ; //    getmMileageKM()
		//      edogDataInfo.setmVersion(String.valueOf(SAD_RD_Time));    //     getmVersion(SAD_RD_Time) ;//         setVersion(SAD_RD_Time) ;
		      
	/*
				edogDataInfo.setmIsAlarm(true) ;
				edogDataInfo.setmFirstFindCamera(1) ;
				edogDataInfo.setmBlockSpace(15300);
				edogDataInfo.setmAlarmType(0xC);
				edogDataInfo.setmSpeedLimit(120) ;
				edogDataInfo.setmPercent(85);
				edogDataInfo.setmBlockSpeed(130);
	*/	
		
				
				if(edogDataInfo.getmFirstFindCamera()==1  ){
									
				    if(  edogDataInfo.getmAlarmType()== 0xC  )  {
				    	BlockSpeedLimit = edogDataInfo.getmSpeedLimit() ;
				    	BlockSpeedTime = 1 ;
				    }
				    else if(  edogDataInfo.getmAlarmType()== 0x6  )  {
				    	BlockSpeedTime = 0 ;
				    	BlockSpeedLimit = 0 ;//xff ;
				    	}
				    }
				
				
				
				
				
			
       if( BlockSpeedTime > 0  ){
    	   
    	   BlockSpeedTime ++  ;
    	   
    	   if( BlockSpeedTime  >=  2400 ){  //20分钟 区间测速 取消 。
    	   
    		   BlockSpeedLimit = 0;//xff ; 
    		   BlockSpeedTime = 0 ;
    		   
    	   }
       }	
			
       
   	mHandler.sendEmptyMessage(0);
       
				//if(edogDataInfo!=null && edogDataInfo.ismIsAlarm()){
				if( (edogDataInfo.getmFirstFindCamera() != 0) || edogDataInfo.ismIsAlarm() ){	
					dataPlay.edogDataPlayer(edogDataInfo);
				}
				

		//		Log.e("T-TgpsFixTime", "TgetGpsDate=" +  String.valueOf(gpsInfo.getGpsDate() ));
				if( (SAD_RD_Time > 0 )&& (SAD_RD_Time < 5000 )){  //&&  radarCFOK
					SAD_RD_Time -- ;
					
					
					/*
				  	if(SAD_RD_Time == 0)  {
				  		
				  	//	Log.e("SADA", "SAD-run "   );
					     	radarCFOK = false  ;
					      	radarDisp = 11  ;  //非速安达 雷达no_rd-type-CLR
							mHandler.sendEmptyMessage(1);  
					    }
				*/	
				}
				
				
					if( (Rd_city_cont >0 )&& ( Rd_city_cont < 7 )){ 
						Rd_city_cont ++ ;
					}
				
				
				if( Rx_Rdsingle >0 ){  
					
					Rx_Rdsingle -- ;
					
					if( Rx_Rdsingle == 0 )
						Rd_city_cont = 0;
					
				
				}
				
				
				
				
				
				
	
					if(TIME_RD > 0) {
						
						TIME_RD -- ;}
					
			     	if( ( TIME_RD == 0 )/*&& ( Last_radarType != -1) */&&( radarDisp <0 ) )   //2S钟 没有 RD QINCHU
		         	{
			     		TIME_RD = -1 ;
					Last_radarType = -1  ;
					
					
					radarDisp = 6  ;  //rd-type-CLR
					mHandler.sendEmptyMessage(1);  //  set 雷达 显示
				   	}
			     	

				if( TIME_ERR > 0  ) {
						TIME_ERR -- ;//2分钟 没有 CC55 雷达出错
				}
				
				
			     		if((TIME_ERR ==0 )&& radarCFOK && (radarDisp < 0) )  // radarCFOK == 1 && 
				     	{
			     			
			     			
			     		radarCFOK = false  ;	
					       //显示报雷达异常	
						radarDisp = 8  ;  //no_rd-type-CLR
						mHandler.sendEmptyMessage(1);  
					
				     	}
				
				
		
// 		mHandler.sendEmptyMessage(0);
	  		
	  		
	  		/*
	  		Log.e("getGpsDate", "TgetGpsDate=" +  String.valueOf(gpsInfo.getGpsDate() ));
	  		if(gpsInfo.getGpsDate() > 20160610){
	  			Log.e("getGpsDate", "getGpsDate - ok"   );
	  		}
	  		else Log.e("getGpsDate", "getGpsDate-NG"   );
	  		*/
	  			
	  			
	  		
				
			//雷达 报警didi	
				
			if(	(Last_radarType >= 0x10)  && radarCFOK ) {
		
				
				

			  	if(SAD_RD_Time == 0)  {
			  		
			  	//	Log.e("SADA", "SAD-run "   );
				     	radarCFOK = false  ;
				     	
				     	if(gpsInfo.getGpsDate() > 20160610){
				      	radarDisp = 11  ;  //非速安达 雷达no_rd-type-CLR
						mHandler.sendEmptyMessage(1); 
				     	}
						
				    }
				
				
				
				
				
				
				if ( ( Last_radarType & 0xf) <  2   )        //>=2  是 弱 
				  {
					  
		
					  if (  MAINLoopCont % 2 == 0  )	{ 
						  
						  
						  if(SAD_RD_Time == 0  )  // && gpsInfo.getGpsDate() > 20151001
					    	  dataPlay.radarDataPlayer(19);	
						  else	 
							  dataPlay.radarDataPlayer(8);	
						  
						  
					  }
						  
						  
				  }
				  else if (  MAINLoopCont % 6 == 0  ){
					 
					  
					    if(SAD_RD_Time == 0 ) 		// && gpsInfo.getGpsDate() > 20151001			    	
					           dataPlay.radarDataPlayer(19);	
						  else	 
							  dataPlay.radarDataPlayer(8);	
				  }
					
				
				
			} //if(	Last_radarType > 0x10  && radarCFOK )
				
				
			
				
			// 超速 报警 
				
			
       int SpeedOverFlg	 = 0;

 
     if( edogDataManager.isSpeedLimitOn()){
       
    	  //报警点 限速 
    	 
				if( (gpsInfo.getSpeed() > edogDataInfo.getmSpeedLimit() ) && (  edogDataInfo.getmSpeedLimit() >= 20 ) )	
					SpeedOverFlg = 1 ;
   	
				//区间 限速 	
				
				else if( (edogDataInfo.getmBlockSpace() >0   ) &&(  edogDataInfo.getmBlockSpeed()   >  BlockSpeedLimit ) &&  (BlockSpeedLimit > 20) ) 
					SpeedOverFlg = 2 ;
				
				else if( (gpsInfo.getSpeed() > BlockSpeedLimit ) &&  (BlockSpeedLimit > 20) ) 
					SpeedOverFlg = 2 ;
				
     } 
     
   //最大 限速 
       if(gpsInfo.getSpeed() >  edogDataManager.getMaxLimitSpeed() )
					SpeedOverFlg = 3 ;
				
				
			//	Log.e("SpeedOverFlg", "SpeedOverFlg =" +  String.valueOf(SpeedOverFlg) );	
				
				
					dataPlay.SpeedOverPlayer(SpeedOverFlg ,MAINLoopCont);
				
					//此处会进入死循环，导致程序占用cpu非常高
			//		Log.e("rd", "DEL5555=");	
					
					
			     		while(  ( (System.currentTimeMillis() - MainLoop ) < 500 ) && ( (System.currentTimeMillis() - MainLoop ) >= 0 )) //;
			     		{	
			     			ToolUtils.sleep(10);//mS
			     		//	Log.e("TIME_MAIN", "System.LOOP=" +  String.valueOf(System.currentTimeMillis()) );	 		
			     		}
			     	//	Log.e("TIME_MAIN", "System.LOOP=" +  String.valueOf(System.currentTimeMillis()/100) );	
			     	
			     		
			     		
			     	//	Log.e("rd", "DEL66666=");	
			     
		
			     		
			     	/*	
						edogDataInfo = edogDataManager.getEdogData(gpsInfo);
			
						if(edogDataInfo.getmFirstFindCamera()==1  ){
											
						    if(  edogDataInfo.getmAlarmType()== 0xC  )  {
						    	BlockSpeedLimit = edogDataInfo.getmSpeedLimit() ;
						    	BlockSpeedTime = 1 ;
						    }
						    else if(  edogDataInfo.getmAlarmType()== 0x6  )  {
						    	BlockSpeedTime = 0 ;
						    	BlockSpeedLimit = 0 ;//xff ;
						    	}
						    }		
			     		
			     		*/
			     		
			}  ///while
			
			
	//	} catch (Throwable e) {  //Throwable  IOException
	//		e.printStackTrace();
	//	}
			
			
			
			
		}
	};

	// 雷达线程,接收雷达信号
	Runnable radarRunnable = new Runnable() {
		@Override
		public void run() {
			
			
			mFd = radarDataManager.initRadar();   //打开雷达串口
			if (mFd != null) {
				mFileInputStream = new FileInputStream(mFd);
				new FileOutputStream(mFd);
				// 创建一个长度为1024的竹筒
				byte[] bbuf = new byte[32];   //[8]
				
				
				// 用于保存实际读取的字节数
				int hasRead = 0;
				// 使用循环来重复“取水”的过程
			//	 long   TIME_ERR = -1 ;
			//	 long   TIME_RD = -1 ;
				try {
					while (radar_run) {  //run_thread   true
						
						//Log.e("Last_radarType", "Last_radarType=" +  String.valueOf(Last_radarType) );	
				//		ToolUtils.sleep(100); 
						if ((hasRead = mFileInputStream.read(bbuf)) > 0) {
						
			
							radarType = radarDataManager.parseRadarData(bbuf,hasRead);   //MainLoop,
				
							
						//增加清除 bbuf里的内容
							  bbuf = new byte[32] ;
				  
		      	if(( radarType >= 0) &&( radarDataManager.getRadarMode()!= 4  ) ){
							
		      		TIME_ERR = 120 ;  //	1min  System.currentTimeMillis() ; 
	
		 
		      		
		      		if( (radarType == 8 || radarType == 0  )&& (radarDisp < 0 )){// !radarCFOK
		      			
		      	
		      			
						    radarCFOK =  true ;
						    
					
					
							if(radarType == 8)  {
							radarDisp= 10 ;
							mHandler.sendEmptyMessage(1);  //  set 雷达 cf显示
							
							}
		      		 	//}
					}
			
		      		
		      		if( radarType == 8 ){
		      			
		      			SAD_RD_Time  = 1000 ;  // 2MIN
		      			
		      			      			
		      		}
		      			
		      		else if( radarType == 9  ){   //  c++有  &&  gpsInfo.getGpsDate() > 20151001 
		      			
		      			radarDisp = 9 ;  //严重错误  rd-yes
						mHandler.sendEmptyMessage(1);  //  set 雷达 cf显示
		      			
		      			SAD_RD_ERR ++ ; 
		      			
		      			if(SAD_RD_ERR >= 10 && ( MAINLoopCont %5 ==0 )  ){
		      				
		      				dataPlay.radarDataPlayer(19);	 //播报雷达	err  雷达 显示 严重错误
		      				
		      			}
		      			
		      			
		      			 
		      		}
					  
					  else	if( radarType >= 0x10 /* && radarDataManager.getRadarMode()!= 4   */)	{
						
						  
						  
						      TIME_RD = 6 ;  // System.currentTimeMillis() ;  //开始 雷达播报计数  
						
						     
							  Rx_Rdsingle =   4  ; //连续 2S钟  信号，清除市区 计数
							  
							  
					  	if(Rd_city_cont == 0)	  Rd_city_cont = 1 ; //++ ;  市区模式 时间
						   
						      radarDisp =   radarType >> 4 ;  //rd-type
								mHandler.sendEmptyMessage(1);  //  set 雷达 显示
						
			// { "智能模式 1", "市区模式2", "郊区模式3", "关闭4" };		  
						  
						  if( (gpsInfo.getSpeed()>= radarDataManager.getRadarMuteSpeed() ) || !edogDataInfo.getmGpsState()  )
						  {
						  
							  
							if(  (radarDataManager.getRadarMode() == 2 &&  Rd_city_cont >= 6 ) || (radarDataManager.getRadarMode() == 3) 
									 || ( radarDataManager.getRadarMode() == 1  && !edogDataInfo.ismIsAlarm() ) )  {  // 市区
							
								
						  
					  			  
							  
						if(Last_radarType == -1 ){
							
						Last_radarType =  radarType  ;  
						
				//		Log.e("Last_radarType", "Last_radarType=" +  String.valueOf(Last_radarType) );			
						
						if( radarCFOK)
					  	dataPlay.radarDataPlayer(Last_radarType  >> 4);	 //播报雷达		
							
						
							
						}
						
			
				   						
							}
							
						}
						  
				//	 Last_radarType =  radarType  ;    
						  
		                 	} //if( radarType >= 0x10     radarType = -1 ;	  
					
		      		ToolUtils.sleep(100);   //2000 --》2S
		      		
						}
							
		
		      	if (radarDataManager.getRadarMode() == 4  )
		      	{
		      		
	    	//	Log.e("DY1", "getRadarMode XXX= "  );
		      		SAD_RD_Time =5000 ;
		      		Last_radarType = -1 ;
		      		radarCFOK = false  ;
		      		radarDisp = 7 ;  //rd-yes
					mHandler.sendEmptyMessage(1);  //  set 雷达 cf显示	
		      		
		      	}
		      	
			
					}
					
					}
					
					
					mFileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				radarType = -1;
			//	mHandler.sendEmptyMessage(1);
			}
		}
	};
	/*
	//雷达状态监控线程
	private Runnable radarMonitorRunnable = new Runnable(){
		@Override
		public void run() {
			while (run_thread) {
				if((System.currentTimeMillis()-radarNoSingleTime)>2*60*1000){
					radarType = -1;
					mHandler.sendEmptyMessage(1);
				}
			}
		}
	};
	
	*/
	public class ReceiveBroadCast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("radar.com.change")) {
				radarThread.interrupt();
				radarThread = new Thread(radarRunnable);
				radarThread.start();
			}
		}

	}
	
	private Handler mHandler = new Handler() {
		@Override
		
	//	Log.e("SAD0", "SAD-handleMessage "   );
		
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
		//	Log.e("SAD0", "SAD-handleMessage "   );
			
			switch (msg.what) {
			case 0:// 显示电子狗数据
				
			//	Log.e("SAD1", "SADedogDataShow "   );
			//	edogDataInfo.setmVersion(SAD_RD_Time)  ;
				dataShow.edogDataShow(0, edogDataInfo,BlockSpeedLimit);
				
				
				break;
			case 1:// 显示雷达数据      有雷达信号 ，才会进入 
				
			//	Log.e("SAD2", "SADradarDispShow " + String.valueOf(radarDisp)  );
				
				
			 	dataShow.radarDataShow(radarDisp);
			 	radarDisp = -1 ;
			 	
				break;
			default:
				break;
			}
		}
	};
	
	
	
	private class FloatViewBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CRREATE_ALL_FLOATVIEW)) {
				Log.e("dy","ACTION_CRREATE_ALL_FLOATVIEW...");
				createAllFloatView();
			} else if (intent.getAction().equals(ACTION_REMOVE_ALL_FLOATVIEW)) {
				Log.e("dy","ACTION_REMOVE_ALL_FLOATVIEW...");
				removeAllFloatView();
			}else if (intent.getAction().equals(ACTION_CRREATE_SPEED_FLOATVIEW)) {
				createSpeedFloatView();
			} else if (intent.getAction().equals(ACTION_REMOVE_SPEED_FLOATVIEW)) {
				Log.e("dy","ACTION_REMOVE_SPEED_FLOATVIEW...");
				removeSpeedFloatView();
			} else if (intent.getAction().equals(ACTION_CRREATE_RADAR_FLOATVIEW)) {
				Log.e("dy","ACTION_CRREATE_RADAR_FLOATVIEW...");
				createRadarFloatView();
			} else if (intent.getAction().equals(ACTION_REMOVE_RADAR_FLOATVIEW)) {
				Log.e("dy","ACTION_REMOVE_RADAR_FLOATVIEW...");
				removeRadarFloatView();
			}
		}

	}

	private void createAllFloatView() {
		createSpeedFloatView();
		createRadarFloatView();
	}

	private void removeAllFloatView() {
		removeSpeedFloatView();
		removeRadarFloatView();
	}
	
	private void createRadarFloatView() {
		// 创建之前先移除避免异�?
		removeRadarFloatView();

		wmRadarParams = new WindowManager.LayoutParams();
		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mRadarFloatLayout = (RelativeLayout) inflater.inflate(R.layout.radar_float_layout, null);
		mRadarFloatView = (ImageView) mRadarFloatLayout.findViewById(R.id.radar_float_id);
//		mRadarFloatView.setImageResource(R.drawable.cam_k);
		createFloatView(wmRadarParams, mRadarFloatLayout, mRadarFloatView, 200, 200);

		radarfvIsVisible = true;
	}

	private void removeRadarFloatView() {
		if (mRadarFloatLayout != null && mWindowManager != null) {
			// 移除悬浮窗口
			mWindowManager.removeView(mRadarFloatLayout);
			mRadarFloatLayout = null;
			radarfvIsVisible = false;
			System.out.println("floatview is remove *****************");
		}
	}

	private void createSpeedFloatView() {
		Log.e("dy","createSpeedFloatView...");
		// 创建之前先移除避免异�?
		removeSpeedFloatView();

		wmSpeedParams = new WindowManager.LayoutParams();
		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mSpeedFloatLayout = (RelativeLayout) inflater.inflate(
				R.layout.speed_float_layout, null);

		llWarn = (LinearLayout) mSpeedFloatLayout.findViewById(R.id.ll_img_dis);
		llSpeed = (LinearLayout) mSpeedFloatLayout.findViewById(R.id.ll_speed);

		fImage = (ImageView) mSpeedFloatLayout.findViewById(R.id.iv_png);
		fDistance = (TextView) mSpeedFloatLayout.findViewById(R.id.tv_distance);
		fSpeed = (TextView) mSpeedFloatLayout.findViewById(R.id.tv_speed);

		createFloatView(wmSpeedParams, mSpeedFloatLayout, fImage, 400, 200);

		speedfvIsVisible = true;
	}

	private void removeSpeedFloatView() {
		if (mSpeedFloatLayout != null && mWindowManager != null) {
			// 移除悬浮窗口
			mWindowManager.removeView(mSpeedFloatLayout);
			mSpeedFloatLayout = null;
			speedfvIsVisible = false;
			System.out.println("speedfloatview is remove *****************");
		}
	}

	private void createFloatView(final WindowManager.LayoutParams wmParams,
			final RelativeLayout layout, final View view, int x, int y) {
		Log.e("dy","createFloatView");
		// 获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(
				WINDOW_SERVICE);
		
	//	Log.i(TAG, "mWindowManager--->" + mWindowManager);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置�?
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = x;
		wmParams.y = y;

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		// 添加mFloatLayout
		mWindowManager.addView(layout, wmParams);

		layout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	//	Log.i(TAG, "Width/2--->" + view.getMeasuredWidth() / 2);
	//	Log.i(TAG, "Height/2--->" + view.getMeasuredHeight() / 2);
		// 设置监听浮动窗口的触摸移�?
		layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐�?
				wmParams.x = (int) event.getRawX() - view.getMeasuredWidth()
						/ 2;
		//		Log.i(TAG, "RawX" + event.getRawX());
		//		Log.i(TAG, "X" + event.getX());
				// �?5为状态栏的高�?
				wmParams.y = (int) event.getRawY() - view.getMeasuredHeight()
						/ 2 - 25;
			//	Log.i(TAG, "RawY" + event.getRawY());
			//	Log.i(TAG, "Y" + event.getY());
				// 刷新
				mWindowManager.updateViewLayout(layout, wmParams);

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					x2 = event.getRawX();
					y2 = event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					x2 = event.getRawX();
					y2 = event.getRawY();
					if (Math.abs(x1 - x2) < 20 && Math.abs(y1 - y2) < 20) {
						Intent intent = new Intent(TuzhiService.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra(MainActivity.SHOW_DIALOG_FLAG, false);// 如果是最小化，不弹框
						startActivity(intent);
					}

					break;
				case MotionEvent.ACTION_DOWN:
					x1 = event.getRawX();
					y1 = event.getRawY();
					break;

				}

				return true; // 此处必须返回false，否则OnClickListener获取不到监听
			}
		});

	}
	
	
	
	

}

package com.hdsc.edog.jni;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdsc.edog.utils.ToolUtils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

//import com.hdsc.edog.TuzhiService;
//import com.hdsc.edog.utils.SharedPreUtils;
//import com.hdsc.edog.utils.ToolUtils;

public class DataPlay {
	private final static String TAG = "DataPlay";

	private  Context mContext;
	private EdogDataManager edogDataManager;
	private RadarDataManager radarDataManager;
    private List<String> audioList;
	private Thread playerThread;
	private boolean player_run = true;
	private boolean startPlay = false;
	private boolean   Playing_One = false;
	
	public static   boolean isPlaying = false;
	private static MediaPlayer mMediaPlayer = null;

	static  boolean flgSPEEDOVER = false;  // private
//	private boolean isAlarm = false;
	
	private static Map<String, String> warnTypeMap = null;
	private static Map<String, String> positionMap = null;
	
  //  private List<String> audioList = new ArrayList<String>();
//	private int playno=0 ;
	
//	public static int audioList_size ;
	public DataPlay(Context context){
		this.mContext = context;
		init();
		
		audioList = new ArrayList<String>();
		playerThread = new Thread(playerRunnable);
		playerThread.start();
		
		if(edogDataManager == null){
			edogDataManager = new EdogDataManager(context);
		}
		if(radarDataManager == null){
			radarDataManager = new RadarDataManager(context);
		}
	}
	
	
	//超速 报警 
	
	public  void SpeedOverPlayer(int SpeedOverFlg ,int MAINLoopCont ) {

	
			
		
	//	List<String> audioList = new ArrayList<String>();

	//	audioList = new ArrayList<String>();
	//	playno=0 ;
		
	//		Log.e("rd", "SpeedOverPlayer=1");
		
		if(startPlay)  return ;
	//	Log.e("rd", "SpeedOverPlayer=2");
		
		if( (SpeedOverFlg != 0 ) ) {  // && !startPlay
			
			//List<String> audioList = new ArrayList<String>();
	
			if(flgSPEEDOVER == false ) 
		    	{
			
			flgSPEEDOVER = true;
			 if(SpeedOverFlg == 1 )	{		
					audioList.add("SPEEDOVER.mp3");
					}
			 
			 else 	 if(SpeedOverFlg == 2 )	{		
					audioList.add("SPEEDOVER.mp3");	
					}
					
			 else 	 if(SpeedOverFlg == 3 )	{	
				 
				
				 
					audioList.add("SPEEDOVER.mp3");
					
					}
			 
					
					
					
		    	}
		    else{ 
		    	if(MAINLoopCont % 6  == 0)   //3是一次6
					audioList.add("DIDI.mp3");
				}
		
		
		startPlay = true;	

		
   	}
		else if(SpeedOverFlg == 0  ){
			flgSPEEDOVER = false ;
		
		}
		
		
	
		
		
			//	playAudio(audioList);
		
	}
	
	
	//雷达数据播报
	public  void radarDataPlayer(int radarType ) {
		
	


	//List<String> 
//	audioList = new ArrayList<String>();
//	playno=0 ;
		/*		//audioList.add("dindong.mp3");// 开始提示音
		if (radarType == 8 ) {
			// 雷达异常
			
			
			
			audioList.add("RDERR.mp3");
		}

		
		
		if (radarType == 0) {
			return;
		}

		
		if (radarType == 0) {
			// 无雷达信�?
		}
		*/
	//	Log.e("rd", "radarDataPlayer=1");
		
		if(startPlay)  return ;
	//	Log.e("rd", "radarDataPlayer=2");
		
		if (radarType == 1) {
			// 镭射�?
			audioList.add("LASER.mp3");
		}
		else  if (radarType == 2) {
			// K�?
			audioList.add("K.mp3");
		}
		else  if (radarType == 3) {
			// KA�?
			audioList.add("KA.mp3");
		}
		else  if (radarType == 4) {
			// KU�?
			audioList.add("KU.mp3");
		}
		else  if (radarType == 5) {
			// X�?
			audioList.add("X.mp3");
		}
		else  if (radarType == 8) {
			// RD 
			audioList.add("RFDIDI.mp3");
		}
		
		else  if (radarType == 19) {
			// 雷达异常
				
			audioList.add("RDERR.mp3");
		}
		
		
	//	audioList.add("10km.mp3");
	//		audioList.add("20km.mp3");
	//		audioList.add("30km.mp3");
	//		audioList.add("40km.mp3");
			
	//		Log.e(TAG, "player_runa2 ==" + String.valueOf(audioList.size()));
			
		startPlay = true;	
		
		
		
		
	//	audioList.add("START.mp3");
		//audioList.add("end.mp3");// 结束提示�?
//		Log.e("rd", "DELcccccc=");  //一下出错e
	//	playAudio(audioList);
//		Log.e("rd", "DELddddddd=");  //一下出错
		
	}

	
	
	
	// 电子狗数据播�
	public void edogDataPlayer(EdogDataInfo info) {
	//	int gpsspeed = (int)info.getmSpeed();
		
		int position = info.getmPosition();
	//	int distance = getDistanceNo(info.getmDistance());
		int distance = info.getmDistance();
		
		distance = (int) (distance + 40) / 100;
		distance *= 100;

		
		int warnType = info.getmAlarmType();
		int speedLimit = info.getmSpeedLimit();
		boolean isAlarm = info.ismIsAlarm();
		int FirstFindCamera = info.getmFirstFindCamera();

//		audioList = new ArrayList<String>();
//		playno=0 ;
		
	/*
		audioList.add("dindong.mp3");
		if (position == 0 || position == 6 || position == 7) {
			audioList.add(positionMap.get(String.valueOf(position)));
			} else {
			audioList.add(positionMap.get(String.valueOf(0)));// 正前�?
			audioList.add(positionMap.get(String.valueOf(position)));
			}
				if ( distance  != 0) {
			audioList.add(String.valueOf(distance) + ".mp3");
			audioList.add("m.mp3");// �?
		}
				audioList.add(warnTypeMap.get(String.valueOf(warnType)));
		
		
		
*/
		
		
		
		if(warnType==0 && !edogDataManager.isRedLightOn()){
			//关闭闯红灯提醒
			return ;
		}else if(  (warnType==5 || warnType == 230 )&& !edogDataManager.isTrafficRuleOn()){
			//关闭交通违规提醒
			return ;
		}else if(warnType>=208 && warnType<=223 && !edogDataManager.isSafeOn()){
			//关闭安全提醒
			return ;
		}
		
		
		
		
		
		if (isAlarm) {   //isAlarm
		//	List<String> 
			/*
			if(startPlay ){
				
			mMediaPlayer.stop();	
			startPlay = false;	
			audioList = new ArrayList<String>();
			
			}
		*/
			
			if ((FirstFindCamera == 1) || (FirstFindCamera == 5)) {
			
				
		if((warnType == 0xC )||( warnType == 6 )){
				audioList.add(positionMap.get(String.valueOf(0)));   //区间 只有  报前方
		}
		else{		
		audioList.add(positionMap.get(String.valueOf(position)));   // 正前�?
		}
		
		
					if ( (distance > 100) && (distance < 1000 )&& (FirstFindCamera == 1) && (warnType != 0xC )) {
				
			audioList.add(String.valueOf(distance) + "M.mp3");
			
	//		Log.e(TAG,"distance:"+String.valueOf(distance)+ "m.mp3");
		//	audioList.add("m.mp3"); // �?
					}
		
					
					

				//	Log.e(TAG,"alm_speedLimit:"+String.valueOf(warnType));
					
					audioList.add(warnTypeMap.get(String.valueOf(warnType)));
				
		
		if( (speedLimit>=30) && (speedLimit<=120)){
		//	audioList.add("MaxSpeed.mp3");// 限�?
		//  audioList.add(String.valueOf(getDistanceNo(speedLimit)) + "km.mp3");
		//	Log.e(TAG,"speedLimit:"+String.valueOf(speedLimit)+ "km.mp3");
			//audioList.add("km.mp3");// 公里
			
					audioList.add(String.valueOf(speedLimit) + "km.mp3");
				}
		else {
		audioList.add("BEWARE_DRIVER.mp3");//请小心驾�?
			}

		
		startPlay = true;	
		
			}//if(FirstFindCamera ==1 )
		else if(FirstFindCamera == 2 )  {
				
			
				audioList.add("SECOND.mp3");
				startPlay = true;	
			}
/*
		else{ 
			if( gpsspeed > speedLimit ) //&& edogDataManager.isSpeedLimitOn()  
	
			{
			    if(flgSPEEDOVER == false ) 
			    	{
						audioList.add("SPEEDOVER.mp3");
						flgSPEEDOVER = true;
			    	}
			    else{
						audioList.add("DIDI.mp3");
					}
			
				}
			else {
				flgSPEEDOVER = false ;
			
			}
			
			
		}
		
		*/
			
			
	//		audioList_size =	audioList.size() ;

	//	startPlay = true;		
			
		}
		
		if (FirstFindCamera == 3 && !startPlay) {
			
			
			
			
		audioList.add("PASS.mp3") ;// 结束提示�?
		startPlay = true;		
		
		}
		
	//	Log.e("rd", "DEL_GGGGG=");  //一下出错
//		playAudio(audioList);
		
	//	Log.e("rd", "DEL_HHHHHHH=");  //一下出错
	}

		
	/*
	private static int getDistanceNo(int distance) {
		if (distance <= 10) {
			return Math.round(distance);
		} else if (distance > 10 && distance <= 100) {
			return (distance / 10) * 10;
		} else if (distance > 100 && distance <= 1000) {
			return (distance / 100) * 100;
		} else {
			return distance;
		}
	}
	

	
	
	public void playAudio(List<String> audioList) {
		for (int i = 0; i < audioList.size();) {
			while (!isPlaying) {
				if (i >= audioList.size())
					return;
				play(audioList.get(i));
				i++;
			}
		}
	}
	
	

	/*
	
	
		
	public  void playAudio(List<String> audioList) {
		
		if(audioList.size() == 0){
		//	playno=0 ;
			return ;
		}
				
	
			for (int i = 0; i < audioList.size();i++) {
				
				
				while (isPlaying)  ;
				
				play(audioList.get(i));
				
				
				
			}
		
		}
	
	*/
	
	
	
	/*
	public void PLAYALL(){
		playAudio(audioList);
	}
	*/	
		
	


	
	
	

	private static void init() {
		if (positionMap == null) {
			positionMap = new HashMap<String, String>();
			positionMap.put("0", "TA0.mp3");
			positionMap.put("1", "TA1.mp3");
			positionMap.put("2", "TA2.mp3");
			positionMap.put("3", "TA3.mp3");
			positionMap.put("4", "TA4.mp3");
			positionMap.put("5", "TA5.mp3");
			positionMap.put("6", "TA6.mp3");
			positionMap.put("7", "TA7.mp3");
			positionMap.put("8", "TA8.mp3");
			positionMap.put("9", "TA9.mp3");
			positionMap.put("10", "TAA.mp3");
			positionMap.put("11", "TAB.mp3");
			positionMap.put("12", "TAC.mp3");
			positionMap.put("13", "TAD.mp3");
			positionMap.put("14", "TAE.mp3");
			positionMap.put("15", "TAF.mp3");

		}

		
		
		
		
		
		
		
		if (warnTypeMap == null) {
			warnTypeMap = new HashMap<String, String>();
			warnTypeMap.put("0", "TD00.mp3");
			warnTypeMap.put("1", "TD01.mp3");
			warnTypeMap.put("2", "TD02.mp3");
			warnTypeMap.put("3", "TD03.mp3");
			warnTypeMap.put("4", "TD04.mp3");
			warnTypeMap.put("5", "TD05.mp3");
			warnTypeMap.put("6", "TD06.mp3");
			warnTypeMap.put("7", "TD07.mp3");
			warnTypeMap.put("8", "TD08.mp3");
			warnTypeMap.put("10", "TD0A.mp3");
			warnTypeMap.put("11", "TD0B.mp3");
			warnTypeMap.put("12", "TD0C.mp3");
			warnTypeMap.put("208", "TDD0.mp3");
			warnTypeMap.put("209", "TDD1.mp3");
			warnTypeMap.put("210", "TDD2.mp3");
			warnTypeMap.put("211", "TDD3.mp3");
			warnTypeMap.put("212", "TDD4.mp3");
			warnTypeMap.put("213", "TDD5.mp3");
			warnTypeMap.put("214", "TDD6.mp3");
			warnTypeMap.put("215", "TDD7.mp3");
			warnTypeMap.put("216", "TDD8.mp3");
			warnTypeMap.put("217", "TDD9.mp3");
			warnTypeMap.put("218", "TDDA.mp3");
			warnTypeMap.put("219", "TDDB.mp3");
			warnTypeMap.put("220", "TDDC.mp3");
			warnTypeMap.put("221", "TDDD.mp3");
			warnTypeMap.put("222", "TDDE.mp3");
			warnTypeMap.put("223", "TDDF.mp3");
			warnTypeMap.put("224", "TDE0.mp3");
			warnTypeMap.put("225", "TDE1.mp3");
			warnTypeMap.put("226", "TDE2.mp3");
			warnTypeMap.put("227", "TDE3.mp3");
			warnTypeMap.put("228", "TDE4.mp3");
			warnTypeMap.put("229", "TDE5.mp3");
			warnTypeMap.put("230", "TDE6.mp3");
			warnTypeMap.put("231", "TDE7.mp3");
			warnTypeMap.put("232", "TDE8.mp3");
			warnTypeMap.put("233", "TDE9.mp3");
			warnTypeMap.put("234", "TDEA.mp3");
			warnTypeMap.put("235", "TDEB.mp3");
			warnTypeMap.put("236", "TDEC.mp3");
			warnTypeMap.put("237", "TDED.mp3");
			warnTypeMap.put("238", "TDEE.mp3");
			warnTypeMap.put("239", "TDEF.mp3");
			warnTypeMap.put("240", "TDF0.mp3");
			warnTypeMap.put("241", "TDF1.mp3");
			warnTypeMap.put("242", "TDF2.mp3");
			warnTypeMap.put("243", "TDF3.mp3");
			warnTypeMap.put("244", "TDF4.mp3");
			warnTypeMap.put("245", "TDF5.mp3");
			warnTypeMap.put("246", "TDF6.mp3");
			warnTypeMap.put("247", "TDF7.mp3");
			warnTypeMap.put("248", "TDF8.mp3");
			warnTypeMap.put("249", "TDF9.mp3");
			warnTypeMap.put("250", "TDFA.mp3");
			warnTypeMap.put("251", "TDFB.mp3");
			warnTypeMap.put("252", "TDFC.mp3");
			warnTypeMap.put("253", "TDFD.mp3");
			warnTypeMap.put("254", "TDFE.mp3");
		}
	}

	// 同步播放声音，要主意这里不是主线�?
	private  void play(String audioName) {
//		if ( !isPlaying ) {  // && false
			try {
				if (mMediaPlayer == null) {
					mMediaPlayer = new MediaPlayer();
				}

				AssetFileDescriptor fileDescriptor = mContext.getAssets()
						.openFd("sound/" + audioName);
				/* 重置MediaPlayer */
				mMediaPlayer.reset();
				/* 设置要播放的文件的路�?*/
				mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				/* 准备播放 */
				mMediaPlayer.prepare();
				/* 开始播�?*/
				mMediaPlayer.start();
			
				Playing_One  =  true;
				
			//	Log.e(TAG, "player_tttt1 ==" + String.valueOf(Playing_One));
				
			//	Log.e("rd", "mMediaPlayer=1");
	//WZJ1.0.9						isPlaying = true;
				// 监听音频播放完的代码，实现音频的自动循环播放
				mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
				{
							@Override
							
							
							public void onCompletion(MediaPlayer arg0) {
		//WZJ1.0.9					 isPlaying = false;	
							
						Playing_One  =  false;
							//	audioList = new ArrayList<String>();
								
							//	Log.e(TAG, "player_runa2 ==" + String.valueOf(audioList.size()));
								
							//	startPlay = false;
								
							
						}							
						}
					);
			
			//	Log.e(TAG, "player_tttt2 ==" + String.valueOf(Playing_One));
				
			//	Log.e("rd", "mMediaPlayer=2");	
				
			} catch (IOException e) {
				// Log.e(TAG, "audioName==" + audioName);
				e.printStackTrace();
			}
			
		//	Log.e(TAG, "player_tttt3 ==" + String.valueOf(Playing_One));
			
	//	}

	}
	

	Runnable playerRunnable = new Runnable(){
		
		@Override
		public void run() {
			while(player_run){
				
				

				
				if(startPlay ){
				
		
				for (int i = 0; i < audioList.size();) {
			//	Log.e(TAG, "player_runa1 = size=" + String.valueOf(audioList.size())+"-i=" + String.valueOf(i) + audioList.get(i));
		
					// Log.e(TAG, "player_run ==" + "5");
					//	if(startPlay ){     //&& !Playing_One
						//	playAudio(audioList);
							
						//	 Log.e(TAG, "player_run ==" + "9");
				

	
									play(audioList.get(i));
									i++;


						
									
									
									
									
							//	}
					//		}
					
					
					
				// 监听音频播放完的代码，实现音频的自动循环播放
				//	 Log.e(TAG, "player_run ==" + "2");	
	//	Log.e(TAG, "player_runa3 ==" + String.valueOf(audioList.size()));		
			while(Playing_One)	{
					
			    if(isPlaying){
						
						
							
						//	Log.e(TAG, "player_run ==" + "isPlaying");	
							
								
						//		 Log.e(TAG, "player_run ==" + "stop");	
							mMediaPlayer.stop();	
							
						
							
							audioList = new ArrayList<String>();
							
							
							startPlay = false;	
							Playing_One = false;
						}
					
					
			    ToolUtils.sleep(10); 
					// Log.e(TAG, "player_run ==" + "3");	
	
				
				}
			
		//	Log.e(TAG, "player_runa3 ==" + String.valueOf(audioList.size()));
			//while (!isPlaying) {
			if ( i >= (audioList.size() )){
			//	 Log.e(TAG, "player_run ==" + "6");
				audioList = new ArrayList<String>();
				
		//		Log.e(TAG, "player_runa2 ==" + String.valueOf(audioList.size()));
				
				startPlay = false;
				
		//	 break ; //	return;
			}	
			
			
			
				
			//	 Log.e(TAG, "player_run ==" + "7");
				 
		
					
					//List<String> 
					 
				}  //for 
				
				audioList = new ArrayList<String>();
				startPlay = false;
				
				}
				
				//	audioList = new ArrayList<String>();
					
				//	Log.e(TAG, "player_runa2 ==" + String.valueOf(audioList.size()));
					
				//	startPlay = false;
				
			//	 Log.e(TAG, "player_run ==" + "8");
				
				ToolUtils.sleep(100); 	
				
			}
		}
		
	};
	
	
	
	public void close() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		isPlaying = false;
		player_run = false;
	}

	

}

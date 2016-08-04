package com.hdsc.edog.jni;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

//import com.hdsc.edog.FloatWindowActivity;
//import com.hdsc.edog.MainActivity;
//import com.hdsc.edog.R;
//import com.hdsc.edog.TuzhiApplication;
//import com.hdsc.edog.TuzhiService;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdsc.edog.FloatWindowActivity;
import com.hdsc.edog.MainActivity;
import com.hdsc.edog.R;
import com.hdsc.edog.TuzhiApplication;
import com.hdsc.edog.TuzhiService;

import android.util.Log;

public class DataShow {
	private static final String TAG = "DataShow";
	private static Context mContext;
	private EdogDataManager edogDataManager;

	private DecimalFormat fnum = new DecimalFormat("##0.00"); // 保留两位小数
	// V1.2.6 、 private int mLimitSpeed = 0;
	private int count = 0;

	// private int mBlockSpeedLimit = 0;//区间测试限速值

	public DataShow(Context context) {
		this.mContext = context;
		if (edogDataManager == null) {
			edogDataManager = new EdogDataManager(context);
		}
	}

	// 显示雷达数据
	public void radarDataShow(int radarType) {

	Log.e("dy","radarType ="+String.valueOf(radarType)  );
		switch (radarType) {
		case 0:
		
			// 无雷达信�?  && MainActivity.radarImg != null
			if( MainActivity.radarImg != null ){
			MainActivity.radarImg.setImageResource(R.drawable.rd1_yes);
			
			}
			/*
			MainActivity.radarImg.setImageResource(R.drawable.rd1_yes);

			// 悬浮窗口
			if (TuzhiService.mRadarFloatView != null) {
				TuzhiService.mRadarFloatView
						.setImageResource(R.drawable.rd1_yes);
			}
			if(MainActivity.isMinMode){
				TuzhiApplication.operateFloatView(mContext,
						TuzhiService.ACTION_REMOVE_RADAR_FLOATVIEW);
			}
			*/
			
			
			
			
			break;
		case 1:
			// 镭射�?
			
				
			
			if(MainActivity.isMinMode){
				if (TuzhiService.mRadarFloatView != null) 
					TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_la);
				}
		
				else {
			
				MainActivity.radarTypeImg.setImageResource(R.drawable.cam_la);
				}
				
				
			break;
		case 2:
			// K�?
			
		/*
			MainActivity.radarTypeImg.setImageResource(R.drawable.cam_k);
			
			
			
			if (TuzhiService.mRadarFloatView != null) {
				TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_k);
			}
			
			*/
			if(MainActivity.isMinMode){
			//	TuzhiApplication.operateFloatView(mContext,
			//			TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
				
				if (TuzhiService.mRadarFloatView != null) 
				TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_k);
			}
		//	else if(MainActivity.isMidMode)	{
		//		TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_k);
		//	}
			else {
			MainActivity.radarTypeImg.setImageResource(R.drawable.cam_k);
			}
			
			break;
		case 3:
			// KA�?


			
			if(MainActivity.isMinMode){
			if (TuzhiService.mRadarFloatView != null) 
				TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_ka);
			}
	
			else {
		
			MainActivity.radarTypeImg.setImageResource(R.drawable.cam_ka);
			}
			
			
			
			
			break;
		case 4:
			// KU�?

			
			
			if(MainActivity.isMinMode){
				if (TuzhiService.mRadarFloatView != null) 
					TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_ku);
				}
		
				else {
			
				MainActivity.radarTypeImg.setImageResource(R.drawable.cam_ku);
				}
				
			break;
		case 5:
			// X�?


			
			if(MainActivity.isMinMode){
				if (TuzhiService.mRadarFloatView != null) 
					TuzhiService.mRadarFloatView.setImageResource(R.drawable.cam_x);
				}
		
				else {
			
				MainActivity.radarTypeImg.setImageResource(R.drawable.cam_x);
				}
			
			break;
		case 6:
			
		//	if (null != MainActivity.radarTypeImg)
		//	MainActivity.radarTypeImg.setImageResource(0);
			
			if(MainActivity.isMinMode){
			
			if (TuzhiService.mRadarFloatView != null) 
					
				TuzhiService.mRadarFloatView.setImageBitmap(null);//TuzhiService.mRadarFloatView.setImageResource(null);
			}
			
			/*
			if(MainActivity.isMinMode){
				
			TuzhiApplication.operateFloatView(mContext,
						TuzhiService.ACTION_REMOVE_ALL_FLOATVIEW);
			
			
			TuzhiApplication.operateFloatView(mContext,
						TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
			
				}
		*/
			//	else {
			
			
			else	if (MainActivity.radarTypeImg != null) {
				MainActivity.radarTypeImg.setImageBitmap(null); //MainActivity.radarTypeImg.setImageResource(0);
				}
			
			
			break;
		case 7:
			// CLR LEIDA 连线标志
			// MainActivity.radarImg.setImageResource(0);

			// Log.e("rd", "radarDataShow=7");
			if (null != MainActivity.radarImg)
				MainActivity.radarImg.setImageResource(0);

		//	if (null != MainActivity.radarTypeImg)
		//		MainActivity.radarTypeImg.setImageResource(0);

			break;
		case 8:
			// 雷达异常
			if (null != MainActivity.radarImg) {
				MainActivity.radarImg.setImageResource(R.drawable.rd_no);
			}
		//	if (TuzhiService.mRadarFloatView != null) {
		//		TuzhiService.mRadarFloatView.setImageResource(R.drawable.rd_no);
		//	}

			break;
		case 9:
			// 雷达异常
			if (null != MainActivity.radarImg) {
				MainActivity.radarImg.setImageResource(R.drawable.rd_no_err);
			}
	//		if (TuzhiService.mRadarFloatView != null) {
	//			TuzhiService.mRadarFloatView
	//					.setImageResource(R.drawable.rd_no_err);
	//		}
			break;
		case 10:
			// 无雷达信�?
			
		if (null != MainActivity.radarImg)	
			MainActivity.radarImg.setImageResource(R.drawable.rd_yes);
		
			
			//TuzhiApplication.operateFloatView(mContext,
			//		TuzhiService.ACTION_REMOVE_ALL_FLOATVIEW);
			
			break;

		case 11:
			MainActivity.muteTv.setText("请使用原厂正品雷达,请注意!!!"); // 、、、fSpeed,
																// fDistance
			// fDistance.setText("你使用的雷达非原厂正品，请注意！！！"); //;

			break;
		default:
			break;
		}

		// ====================

	}

	static int[] resId = new int[] {R.drawable.icon_spd_30,
			R.drawable.icon_spd_40, R.drawable.icon_spd_50,
			R.drawable.icon_spd_60, R.drawable.icon_spd_70,
			R.drawable.icon_spd_80, R.drawable.icon_spd_90,
			R.drawable.icon_spd_100, R.drawable.icon_spd_110,
			R.drawable.icon_spd_120,R.drawable.icon_traffic_light, R.drawable.icon_ty };

	// 显示电子狗数�?
	public void edogDataShow(int nType, EdogDataInfo info, int BlockSpeedLimit) {
		if (info == null){
			return;
		}
		count++;

		Log.e("rd", "RESNO=5B");
		
		// info.setmBlockSpace(120);
		// info.setmBlockSpeed(77);

		// Log.e("rd",
		// "RESNO=  MainActivity.isMinMode   :"+String.valueOf(MainActivity.isMinMode));
		// Log.e("rd",
		// "RESNO=  MainActivity.isMidMode   :"+String.valueOf(MainActivity.isMidMode));

		// //AAAAAAAAA
/*
		if (MainActivity.isMinMode) {

			// Log.e("rd", "RESNO=0"+String.valueOf(999));
			// MininWindow(false);速度浮动小窗口隐�?
			// RadAlarmWindow(false);雷达浮动小窗口隐�?
//			if (TuzhiService.speedfvIsVisible)
//				TuzhiApplication.operateFloatView(mContext,
//						TuzhiService.ACTION_REMOVE_SPEED_FLOATVIEW);
//			if (TuzhiService.radarfvIsVisible)
//				TuzhiApplication.operateFloatView(mContext,
//						TuzhiService.ACTION_REMOVE_RADAR_FLOATVIEW);
		} else {
			// Log.e("rd", "RESNO=0"+String.valueOf(888));
			// ShowWindow(GetMininWindow()->getSafehWnd(),SW_SHOW);//速度浮动小窗口变为可�?
			if (TuzhiService.speedfvIsVisible)
				TuzhiApplication.operateFloatView(mContext,
						TuzhiService.ACTION_CRREATE_SPEED_FLOATVIEW);
			if (TuzhiService.radarfvIsVisible)
				TuzhiApplication.operateFloatView(mContext,
						TuzhiService.ACTION_CRREATE_RADAR_FLOATVIEW);
		}
*/
		
		
		if (info.getmBlockSpace() != 0 /* && !MainActivity.isMinMode */) {
			// if(mBlockSpeedLimit == 0){
			// mBlockSpeedLimit = info.getmSpeedLimit();
			// mBlockSpeedLimit = 133;
			int TresNo = getSpeedLimitImg(BlockSpeedLimit);
			MainActivity.blockLimitSpeedIv.setImageResource(resId[TresNo - 3]);
			// }

			MainActivity.blockSpeedLbTv.setVisibility(View.VISIBLE);
			MainActivity.blockSpaceLbTv.setVisibility(View.VISIBLE);
			MainActivity.blockSpeedTv.setText(String.valueOf(info
					.getmBlockSpeed()) + "Km/h");
			MainActivity.blockSpaceTv.setText(String.valueOf((int) (info
					.getmBlockSpace() / 1000 + 0.5)) + "公里");
			if (info.getmBlockSpeed() < BlockSpeedLimit) {
				MainActivity.pbG.setVisibility(View.VISIBLE);
				MainActivity.pbR.setVisibility(View.INVISIBLE); // /View.GONE
				MainActivity.pbG.setProgress(info.getmPercent());
			} else {

				if (count % 2 == 0) {

					MainActivity.pbG.setVisibility(View.INVISIBLE); // /View.GONE
					MainActivity.pbR.setVisibility(View.VISIBLE);

					MainActivity.pbR.setProgress(info.getmPercent());
				} else {
					MainActivity.pbG.setVisibility(View.INVISIBLE); // /View.GONE
					MainActivity.pbR.setVisibility(View.INVISIBLE);
					// MainActivity.pbG.setProgress(info.getmPercent());

				}

			}
		} else {

			MainActivity.blockSpeedLbTv.setVisibility(View.GONE);
			MainActivity.blockSpaceLbTv.setVisibility(View.GONE);
			MainActivity.pbG.setVisibility(View.INVISIBLE);// View.GONE
			MainActivity.pbR.setVisibility(View.INVISIBLE);// View.GONE
			// mBlockSpeedLimit = 0;
			MainActivity.blockLimitSpeedIv.setImageResource(0);
			MainActivity.blockSpeedTv.setText("");
			MainActivity.blockSpaceTv.setText("");

		}

		// count++; 类型
		// if(1==1){
		if (info.ismIsAlarm() && (info.getmAlarmType() != 12)) { // (info.getmAlarmType()
																	// != 6 ) &&

			// 限速
			int resNo = getSpeedLimitImg(info.getmSpeedLimit());

			// Log.e("rd",
			// "RESNO=SET:getmSpeedLimit"+String.valueOf(info.getmSpeedLimit()));
			// Log.e("rd", "RESNO=SET:"+String.valueOf(resNo));

			if (resNo <= 12 && resNo >= 3) {

				if (MainActivity.isMinMode) {
					Log.e("rd", "RESNO=1B");

				//	TuzhiApplication.operateFloatView(mContext,
				//	TuzhiService.ACTION_CRREATE_SPEED_FLOATVIEW);
					//TuzhiApplication.operateFloatView(mContext,
					//TuzhiService.ACTION_CRREATE_RADAR_FLOATVIEW);

					//TuzhiService.llSpeed.setVisibility(View.VISIBLE);
				//	TuzhiService.llWarn.setVisibility(View.GONE);
//TuzhiService.fImage.setImageResource(resId[resNo-3]);
					
					
			if(info.getmAlarmType()==0) 
				TuzhiService.fImage.setImageResource(resId[resId.length - 2]);
			else
					TuzhiService.fImage.setImageResource(resId[resNo-3]);
					
					

					Log.e("rd", "RESNO=1B-OVER");
					
					
				}

				else if (MainActivity.isMidMode) {
					// Log.e("rd", "RESNO=1A");

					FloatWindowActivity.fwWarnSpeed
							.setImageResource(resId[resNo - 3]);
				} else {
					// Log.e("rd", "RESNO=1");

					MainActivity.wsImg.setImageResource(resId[resNo - 3]);
				}

			} else {

				
				 Log.e("rd", "RESNO=1A  --A");
				
				if (MainActivity.isMinMode) {
					
					// FloatWindowActivity.fwWarnSpeed.setImageResource(resId[resId.length - 1]);
 
					 
					
 
					// FloatWindowActivity.fwWarnSpeed.setImageBitmap(null);

				//	TuzhiService.llSpeed.setVisibility(View.GONE);
				//	TuzhiService.llWarn.setVisibility(View.VISIBLE);
				//	TuzhiService.fImage.setImageBitmap(null);
					
					if(info.getmAlarmType()==0) 
						TuzhiService.fImage.setImageResource(resId[resId.length - 2]);
					else	
					TuzhiService.fImage.setImageResource(resId[resId.length - 1]);
					
					 Log.e("rd", "RESNO=1A  --B");
 

				}

				else if (MainActivity.isMidMode) {
					FloatWindowActivity.fwWarnSpeed.setImageBitmap(null);
				} else {
					MainActivity.wsImg.setImageBitmap(null);
				}

				/*
				 * //悬浮窗口 if (TuzhiService.fImage != null) { if (count % 2 == 0)
				 * { //TuzhiService.fImage.setImageBitmap(null);
				 * TuzhiService.fImage.setImageResource(R.drawable.ic_launcher);
				 * } else {
				 * TuzhiService.fImage.setImageResource(resId[resId.length -
				 * 1]); } }
				 */
			}
			 Log.e("rd", "RESNO=6B");
			
			
			if (MainActivity.isMinMode) {

				// FloatWindowActivity.fwDis.setText(String.valueOf(info.getmDistance())
				// + "m");

				// FloatWindowActivity.fwWarnType.setImageDrawable(getWarnTypeImg(info.getmAlarmType()));
				//FloatWindowActivity.fwWarnType
					//	.setImageDrawable(getWarnTypeImg(info.getmAlarmType()));

			}

			else if (MainActivity.isMidMode) {

				FloatWindowActivity.fwDis.setText(String.valueOf(info
						.getmDistance()) + "m");

				FloatWindowActivity.fwWarnType
						.setImageDrawable(getWarnTypeImg(info.getmAlarmType()));

			} else {

				MainActivity.tvDis.setText(String.valueOf(info.getmDistance())
						+ "m");

				MainActivity.wtImg.setImageDrawable(getWarnTypeImg(info
						.getmAlarmType()));
			}

			/*
			 * if (MainActivity.tvDis != null && info.getmDistance() != 0) {
			 * MainActivity.tvDis.setText(String.valueOf(info.getmDistance())+
			 * "m"); } if (MainActivity.wtImg != null) {
			 * MainActivity.wtImg.setImageDrawable
			 * (getWarnTypeImg(info.getmAlarmType())); } if
			 * (FloatWindowActivity.fwDis != null) {
			 * FloatWindowActivity.fwDis.setText
			 * (String.valueOf(info.getmDistance()) + "m"); } if
			 * (FloatWindowActivity.fwWarnType != null) {
			 * FloatWindowActivity.fwWarnType
			 * .setImageDrawable(getWarnTypeImg(info.getmAlarmType())); }
			 * 
			 * 
			 * if (FloatWindowActivity.fwKilometer != null) {// 总里程数
			 * FloatWindowActivity.fwKilometer.setText(String.format(mContext
			 * .getResources().getString(R.string.current_kilometer),
			 * fnum.format(info.getmMileageKM()))); }
			 */

		} else { // 没报警

			if (MainActivity.isMinMode) {

			//	Log.e("rd", "RESNO=1C");
				
			//	TuzhiService.llSpeed.setVisibility(View.GONE);
			//	TuzhiService.llWarn.setVisibility(View.VISIBLE);
			//	TuzhiService.fImage.setImageDrawable(null);
				if(TuzhiService.fImage!=null){
					
				//	TuzhiService.fImage.setImageDrawable(null);
					TuzhiService.fImage.setImageResource(R.drawable.ic_launcher);
				}
				 
				
	//Log.e("rd", "RESNO=2C-A");
				// FloatWindowActivity.fwWarnSpeed.setImageResource(0);
				// FloatWindowActivity.fwDis.setText("");
				// FloatWindowActivity.fwWarnType.setImageDrawable(null);

			}

			else if (MainActivity.isMidMode) {

				FloatWindowActivity.fwWarnSpeed.setImageResource(0);
				FloatWindowActivity.fwDis.setText("");
				FloatWindowActivity.fwWarnType.setImageDrawable(null);

				// FloatWindowActivity.fwSpeed.setText(String.valueOf(info.getmSpeed())
				// + " km/h");
				// FloatWindowActivity.fwDireciton.setText(info.getmDirection());
			} else {

				MainActivity.tvDis.setText("");
				MainActivity.wtImg.setImageDrawable(null);
				MainActivity.wsImg.setImageResource(0);
			}
			/*
			 * MainActivity.wsImg.setImageResource(0);
			 * if(FloatWindowActivity.fwWarnSpeed!=null){
			 * FloatWindowActivity.fwWarnSpeed.setImageResource(0); }
			 * MainActivity.tvDis.setText("");
			 * MainActivity.wtImg.setImageDrawable(null);
			 * if(FloatWindowActivity.fwDis!=null){
			 * FloatWindowActivity.fwDis.setText(""); }
			 * 
			 * if(FloatWindowActivity.fwWarnType!=null){
			 * FloatWindowActivity.fwWarnType.setImageDrawable(null); }
			 */

		}

		
		
		
		if (MainActivity.isMinMode) {
			
			Log.e("rd", "RESNO=1D");
		}
		
		else if (MainActivity.isMidMode) {

			// FloatWindowActivity.fwWarnSpeed.setImageResource(0);
			// FloatWindowActivity.fwDis.setText("");
			// FloatWindowActivity.fwWarnType.setImageDrawable(null);

			FloatWindowActivity.fwSpeed
					.setText(String.valueOf(info.getmSpeed()) + " km/h");
			FloatWindowActivity.fwDireciton.setText(info.getmDirection());
		}

		else {
			if (MainActivity.startMuteTime == 1) {
				MainActivity.muteTv.setText(" ");
				MainActivity.spkBtn
						.setBackgroundResource(R.drawable.land_btn_spk_on);
				// MainActivity.muteTv.setText("3分钟静音，点击取消");
				// MainActivity.spkBtn.setBackgroundResource(R.drawable.land_btn_spk_off);
			}
		}

		/*
		 * if (FloatWindowActivity.fwSpeed != null) {
		 * FloatWindowActivity.fwSpeed .setText(String.valueOf(info.getmSpeed())
		 * + " km/h"); } if (FloatWindowActivity.fwDireciton != null) {
		 * FloatWindowActivity.fwDireciton.setText(info.getmDirection()); }
		 */

		if (MainActivity.isMinMode != true ) {  //nType == 0
			TextView dirTv = MainActivity.directionTv;
			ImageView dirIv = MainActivity.directionIv;
			if (dirTv != null) {
				dirTv.setText(info.getmDirection());
			}
			if (dirIv != null) {
				dirIv.setImageResource(getDirImg(info.getmDirection()));
			}
			int bw = info.getmSpeed() / 100;
			int sw = (info.getmSpeed() % 100) / 10; // - bw * 100
			int gw = (info.getmSpeed() % 10); // - bw * 100 - sw * 10);

			// V1.2.6 mLimitSpeed =
			// edogDataManager.getMaxLimitSpeed();//获取播报设置中的最高限�

			TextView speed1Tv = MainActivity.speed1;
			TextView speed2Tv = MainActivity.speed2;
			TextView speed3Tv = MainActivity.speed3;

			int color = Color.WHITE;

			// V1.2.6
			/*
			 * if (info.getmSpeed() > mLimitSpeed) { color = Color.RED; } else {
			 * color = Color.WHITE; }
			 */

			if (speed1Tv != null) {
				speed1Tv.setText(String.valueOf(bw));
				speed1Tv.setTextColor(color);
			}
			if (speed2Tv != null) {
				speed2Tv.setText(String.valueOf(sw));
				speed2Tv.setTextColor(color);
			}
			if (speed3Tv != null) {
				speed3Tv.setText(String.valueOf(gw));
				speed3Tv.setTextColor(color);
			}

			// V1.2.6
			// Log.e("rd",
			// "RESNO=SET:getmSpeed == "+String.valueOf(info.getmSpeed()));
			if (MainActivity.ringView != null)
				MainActivity.ringView.restoreRingView(/* mLimitSpeed, */
				info.getmSpeed());

			/*
			 * if (MainActivity.kilometerTv != null)
			 * MainActivity.kilometerTv.setText(String.format(mContext
			 * .getResources().getString(R.string.current_kilometer),
			 * fnum.format(info.getmMileageKM())));// 当前行驶的公里数
			 */

			if (MainActivity.dataVersionTv != null)
				MainActivity.dataVersionTv.setText(String.format(
						mContext.getResources().getString(
								R.string.current_dataversion), "TEST01"));// info.getmVersion()"1094"

			// info.getmVersion() +";"+ info.getmBlockSpeed()+"KM/h;"
			// +String.valueOf( DataPlay.audioList_size ) ));
		}
		// }

		/*
		 * ///AAAAAAAAA
		 * 
		 * if( IsWindowVisible(1) ){
		 * 
		 * // Log.e("rd", "RESNO=0"+String.valueOf(999));
		 * //MininWindow(false);速度浮动小窗口隐�? //RadAlarmWindow(false);雷达浮动小窗口隐�?
		 * if(TuzhiService.speedfvIsVisible)
		 * TuzhiApplication.operateFloatView(mContext,
		 * TuzhiService.ACTION_REMOVE_SPEED_FLOATVIEW);
		 * if(TuzhiService.radarfvIsVisible)
		 * TuzhiApplication.operateFloatView(mContext,
		 * TuzhiService.ACTION_REMOVE_RADAR_FLOATVIEW); } else { // Log.e("rd",
		 * "RESNO=0"+String.valueOf(888));
		 * //ShowWindow(GetMininWindow()->getSafehWnd(),SW_SHOW);//速度浮动小窗口变为可�?
		 * if(!TuzhiService.speedfvIsVisible)
		 * TuzhiApplication.operateFloatView(mContext,
		 * TuzhiService.ACTION_CRREATE_SPEED_FLOATVIEW);
		 * if(TuzhiService.radarfvIsVisible)
		 * TuzhiApplication.operateFloatView(mContext,
		 * TuzhiService.ACTION_CRREATE_RADAR_FLOATVIEW); }
		 * 
		 * //浮动窗口 if(info.getmDistance() == 0){ if(TuzhiService.llSpeed != null
		 * && TuzhiService.llWarn != null){
		 * TuzhiService.llSpeed.setVisibility(View.VISIBLE);
		 * TuzhiService.llWarn.setVisibility(View.GONE); }
		 * if(FloatWindowActivity.fwLlNormal != null &&
		 * FloatWindowActivity.fwLlWarn != null){
		 * FloatWindowActivity.fwLlNormal.setVisibility(View.VISIBLE);
		 * FloatWindowActivity.fwLlWarn.setVisibility(View.GONE); }
		 * if(MainActivity.mainWarnLayout != null)
		 * MainActivity.mainWarnLayout.setVisibility(View.GONE); }else{
		 * if(TuzhiService.llSpeed != null && TuzhiService.llWarn != null){
		 * TuzhiService.llSpeed.setVisibility(View.GONE);
		 * TuzhiService.llWarn.setVisibility(View.VISIBLE); }
		 * if(FloatWindowActivity.fwLlNormal != null &&
		 * FloatWindowActivity.fwLlWarn != null){
		 * FloatWindowActivity.fwLlNormal.setVisibility(View.GONE);
		 * FloatWindowActivity.fwLlWarn.setVisibility(View.VISIBLE); }
		 * if(MainActivity.mainWarnLayout != null)
		 * MainActivity.mainWarnLayout.setVisibility(View.VISIBLE); }
		 * 
		 * 
		 * 
		 * 
		 * // TextView distance = TuzhiService.fDistance; // TextView fspeed =
		 * TuzhiService.fSpeed; //if () { if( info.getmDistance() >0 &&
		 * TuzhiService.fDistance != null){ // distance.setText(""); //
		 * distance.setBackgroundResource(R.drawable.map_location_d); //mini_log
		 * // }else{
		 * 
		 * Log.e("rd", "TuzhiService=0"); //
		 * TuzhiService.fDistance.setBackgroundResource
		 * (R.drawable.mini_log_back);
		 * TuzhiService.fDistance.setText(String.valueOf(info.getmDistance()));
		 * } // }
		 * 
		 * 
		 * else if (TuzhiService.fSpeed != null && info.getmSpeed()> 6 ) { //
		 * if(info.getmSpeed()<6){ // fspeed.setText(""); //
		 * fspeed.setBackgroundResource(R.drawable.mini_log); //
		 * land_btn_setting_d // }else{ Log.e("rd", "TuzhiService=1"); //
		 * TuzhiService.fSpeed.setBackgroundResource(R.drawable.mini_log_back) ;
		 * TuzhiService.fSpeed.setText(String.valueOf(info.getmSpeed())); } else
		 * if (TuzhiService.fSpeed != null ){ Log.e("rd", "TuzhiService=2");
		 * TuzhiService.fSpeed.setText("");
		 * TuzhiService.fSpeed.setBackgroundResource(R.drawable.mini_log); //
		 * land_btn_setting_d }
		 * 
		 * 
		 * 
		 * /* int temp = info.getmSpeedLimit()%10; if(temp <= 12 && temp >= 3){
		 * if(TuzhiService.fImage != null){
		 * TuzhiService.fImage.setImageResource(resId[temp-1]); }
		 * if(MainActivity.wsImg != null){
		 * MainActivity.wsImg.setImageResource(resId[temp-1]); }
		 * if(FloatWindowActivity.fwWarnSpeed != null){
		 * FloatWindowActivity.fwWarnSpeed.setImageResource(resId[temp-1]); }
		 * }else{ if(TuzhiService.fImage != null){
		 * 
		 * Log.e("rd", "RESNO=0"+String.valueOf(5));
		 * //TuzhiService.fImage.setImageResource(resId[resId.length-1]); if
		 * (count % 2 == 0) {
		 * //FloatWindowActivity.fwWarnSpeed.setImageResource(
		 * R.drawable.mini_log);
		 * TuzhiService.fImage.setImageResource(R.drawable.mini_log); } else {
		 * TuzhiService.fImage.setImageResource(resId[resId.length - 1]); }
		 * 
		 * } if(MainActivity.wsImg != null){
		 * 
		 * Log.e("rd", "RESNO=0"+String.valueOf(6)); if(count % 2 == 0){
		 * MainActivity.wsImg.setImageBitmap(null); }else{
		 * MainActivity.wsImg.setImageResource(resId[resId.length-1]); } }
		 * if(FloatWindowActivity.fwWarnSpeed != null){ Log.e("rd",
		 * "RESNO=0"+String.valueOf(7)); if(count % 2 == 0){
		 * FloatWindowActivity.fwWarnSpeed.setImageBitmap(null); }else{
		 * FloatWindowActivity
		 * .fwWarnSpeed.setImageResource(resId[resId.length-1]); } }
		 * 
		 * }
		 */

	}

	public static boolean IsWindowVisible(int winID) {
		if (winID == 1) {
			return TuzhiApplication.isCurrent(mContext)
					|| MainActivity.isMinMode;
		} else {
			// return TuzhiService.radarfvIsVisible &&
			// TuzhiService.speedfvIsVisible;
			return TuzhiService.speedfvIsVisible;
		}
	}

	/*
	 * private Drawable getWarnTypeImg(int warnType) { //static String
	 * strWarnType = "cam_" + String.valueOf(warnType) + ".png"; // Log.e(TAG,
	 * strWarnType); InputStream is = null; try { is =
	 * mContext.getAssets().open("imgs/" + strWarnType); if ((is == null) &&(
	 * warnType != 0)) { is = mContext.getAssets().open("imgs/cam_all.png"); } }
	 * catch (IOException e) { // e.printStackTrace(); } Drawable drawable =
	 * Drawable.createFromStream(is, null); return drawable; }
	 */

	private Drawable getWarnTypeImg(int warnType) {
		String strWarnType = "cam_" + String.valueOf(warnType) + ".png";
		InputStream is = null;
		try {
			is = mContext.getAssets().open("imgs/" + strWarnType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (is == null && warnType != 0) {
			try {
				is = mContext.getAssets().open("imgs/cam_all.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Drawable drawable = Drawable.createFromStream(is, null);
		return drawable;
	}

	private int getSpeedLimitImg(int speedLimit) {// static

		/*
		 * if (speedLimit <= 10) { return Math.round(speedLimit); } else if
		 * (speedLimit > 20 && speedLimit <= 100) { return (speedLimit / 10); }
		 * else if (speedLimit > 100 && speedLimit <= 1000) { return (speedLimit
		 * / 10); } else { return speedLimit; }
		 */

		if (speedLimit >= 30 && speedLimit <= 120) {
			return (speedLimit / 10);
		}

		else {
			return 13;
		}

	}

	/*
	 * //V1.2.6 public int getMaxLimitSpeed(){ //SAD return mLimitSpeed; }
	 */

	private int getDirImg(String dir) {
		if (dir.trim().equals("北")) {
			return R.drawable.dir0;
		}
		if (dir.trim().equals("东北")) {
			return R.drawable.dir1;
		}
		if (dir.trim().equals("东")) {
			return R.drawable.dir2;
		}
		if (dir.trim().equals("东南")) {
			return R.drawable.dir3;
		}
		if (dir.trim().equals("南")) {
			return R.drawable.dir4;
		}
		if (dir.trim().equals("西南")) {
			return R.drawable.dir5;
		}
		if (dir.trim().equals("西")) {
			return R.drawable.dir6;
		}
		if (dir.trim().equals("西北")) {
			return R.drawable.dir7;
		}
		return 0;
	}
}

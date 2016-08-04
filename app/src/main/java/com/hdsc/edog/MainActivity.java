package com.hdsc.edog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

//import android.content.ContextWrapper;

import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hdsc.edog.entity.VersionInfo;
import com.hdsc.edog.jni.DataPlay;
import com.hdsc.edog.jni.DataShow;
import com.hdsc.edog.net.HttpRequestManager;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.DownloadUtils;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;
import com.hdsc.edog.view.RingView;
//import com.hdsc.edog.QuitReceiver;


public class MainActivity extends Activity implements OnClickListener {

	public static final String POWER_ON_RADAR_ACTION = "landsem.intent.action.RADAR_POWER_ON";
	public static final String POWER_OFF_RADAR_ACTION = "landsem.intent.action.RADAR_POWER_OFF";

	private final String TAG = "MainActivity";

	private RelativeLayout guideRl, blueRl;
	// private Button spkBtn;
	private LinearLayout directionLayout;
	private LinearLayout speedLayout;

	public static Button spkBtn;

	public static TextView kilometerTv, dataVersionTv;
	public static RingView ringView;
	public static TextView speed1, speed2, speed3;// 仪表盘上的速度
	public static TextView directionTv, tvDis, muteTv;// 方向
	public static ImageView directionIv;// 方向箭头
	public static ImageView ivGps, wtImg, wsImg;
	public static ImageView radarImg;
	public static ImageView radarTypeImg;
	public static LinearLayout mainWarnLayout;
	// public static TextView blockSpeedTv, blockSpaceTv;
	public static TextView blockSpeedTv, blockSpeedLbTv, blockSpaceTv,
			blockSpaceLbTv;
	public static ImageView blockLimitSpeedIv;

	private ScaleAnimation scaleAnimation;
	private final int imitateTime = 1000 * 20 * 4;
	public static final String SHOW_DIALOG_FLAG = "isShowDialog";
	private Dialog mDialog;

	public static ProgressBar pbG;
	public static ProgressBar pbR;

	private int[] eyeArray = new int[] { R.string.eeye_traffic_light,
			R.string.eeye_camera, R.string.eeye_flow_speed,
			R.string.eeye_speed_30, R.string.eeye_speed_40,
			R.string.eeye_speed_50, R.string.eeye_speed_60,
			R.string.eeye_speed_70, R.string.eeye_speed_80,
			R.string.eeye_speed_90, R.string.eeye_speed_100,
			R.string.eeye_speed_110, R.string.eeye_speed_120 };
	public static int width, height;
	public static float xdpi, ydpi;

public static Intent serviceIntent;  //
//private Intent serviceIntent; 

	// private boolean isMute = false;
	// private boolean isMute_f = false;
	// private Thread muteThread = null;
	// private int startMuteTime = 0;
	// private int preAudioIndex = 0;

	private DataShow dataShow;
	public static boolean isMinMode = false;// 最小化的时候浮动窗口不显示
	public static boolean isMidMode = false;// 简洁窗口不显示
	public static int startMuteTime = 0;

	

	// public void setMute2(boolean state) ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "oncreate");

		setContentView(R.layout.activity_main);

		DisplayMetrics dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);

		width = dm.widthPixels;// 宽度
		height = dm.heightPixels;// 高度
		xdpi = dm.xdpi;
		ydpi = dm.ydpi;

		/*
		 * Log.d(TAG, dm.widthPixels + "==" + dm.heightPixels + "==" +
		 * dm.density + "==" + dm.densityDpi + "==" + dm.scaledDensity + "==" +
		 * dm.xdpi + "==" + dm.ydpi);
		 */

		TuzhiApplication.addActivity(this);
		initViews();
		initAnimation();
		dataShow = new DataShow(this);
		serviceIntent = new Intent(this, TuzhiService.class);
		SharedPreUtils sp = SharedPreUtils.getInstance(this);
		// 如果是最小化的不要弹�?
		boolean a = getIntent().getBooleanExtra(SHOW_DIALOG_FLAG, true);
		int b = TuzhiService.KEY_ISSHOW_DIALOG;
		if (getIntent().getBooleanExtra(SHOW_DIALOG_FLAG, true)
				&& TuzhiService.KEY_ISSHOW_DIALOG == 0) {
			// 判断是否开启GPS设置

			mDialog = ToolUtils
					.getInstance()
					.showGpsDialog(
							(LocationManager) getSystemService(Context.LOCATION_SERVICE),
							this);

			// 自动更新按钮如果开启就自动更新
			if (sp.getIntValue(Constants.SMARTTCLOUND_AUTOUPDATE) == 0) {
				HttpRequestManager manager = HttpRequestManager.getInstance();
				manager.updateVersion(this, mHandler);
			}

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					netHandler.sendEmptyMessage(1); // 给UI主线程发送消息
				}
			}, 1); // 等待6秒钟6000

			startService(serviceIntent);

			// startMuteTime = 180 ;//System.currentTimeMillis();
			// 1.2.4 muteThread = new Thread(muteRunnable);
			// 1.2.4 muteThread.start();
		}
		isMinMode = false;
		isMidMode = false;
		Log.e("dd", "isMinMode 1 ");
		Log.e("dd", "send power on ");
		Intent intent = new Intent();
		intent.setAction(POWER_ON_RADAR_ACTION);
		sendBroadcast(intent);
		
		//receiver tts to exit
		IntentFilter filter = new IntentFilter("com.dx.intent.colse_edog");
		registerReceiver(mReceiver, filter);
	}
	//receiver tts to exit
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
	
			
			if(intent.getAction().equals("com.dx.intent.colse_edog"))
			{	
				Edogexit();				
			}
		}
	};

	private Handler netHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				ToolUtils.getInstance().dismissSetNetDialog();
				break;
			}
		}
	};

	private void initAnimation() {
		scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
		scaleAnimation.setDuration(imitateTime);
		scaleAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// mHandler.removeCallbacks(runnable);
				exitImitate(true);
			}
		});
	}

	private void initViews() {

		// Log.e("dd", "initViews  1 ");

		wtImg = (ImageView) findViewById(R.id.main_iv_warntype);
		wsImg = (ImageView) findViewById(R.id.main_iv_warnspeed);
		tvDis = (TextView) findViewById(R.id.main_tv_distance);
		ivGps = (ImageView) findViewById(R.id.iv_gps_bg);
		findViewById(R.id.main_btn_floatwin).setOnClickListener(this);
		spkBtn = (Button) findViewById(R.id.main_btn_spk);
		spkBtn.setOnClickListener(this);
		findViewById(R.id.main_btn_setting).setOnClickListener(this);
		findViewById(R.id.main_btn_add_monitor).setOnClickListener(this); // V1.1.5
		findViewById(R.id.main_btn_imitate).setOnClickListener(this);
		findViewById(R.id.main_btn_guide_exit).setOnClickListener(this);
		findViewById(R.id.main_btn_updatepoint).setOnClickListener(this);
		mainWarnLayout = (LinearLayout) findViewById(R.id.ll_speed_km);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) RingView.dip2px(this, 100), 0, 0);
		mainWarnLayout.setLayoutParams(params);
		directionLayout = (LinearLayout) this
				.findViewById(R.id.main_layout_direction);
		directionLayout.setOnClickListener(this);
		speedLayout = (LinearLayout) this.findViewById(R.id.main_layout_speed);
		speedLayout.setOnClickListener(this);

		speed1 = (TextView) findViewById(R.id.main_tv_speed_one);
		speed2 = (TextView) findViewById(R.id.main_tv_speed_two);
		speed3 = (TextView) findViewById(R.id.main_tv_speed_three);
		kilometerTv = (TextView) findViewById(R.id.main_tv_kilometer);
		dataVersionTv = (TextView) findViewById(R.id.main_tv_dataversion);
		// currentSpeedTv = (TextView) findViewById(R.id.main_tv_current_speed);
		directionTv = (TextView) findViewById(R.id.main_tv_direction);
		directionIv = (ImageView) findViewById(R.id.main_iv_direction);
		guideRl = (RelativeLayout) findViewById(R.id.main_rl_guide);
		blueRl = (RelativeLayout) findViewById(R.id.main_layout_blue);
		blueRl.setOnClickListener(this);

		ringView = (RingView) findViewById(R.id.main_ringview);

		muteTv = (TextView) this.findViewById(R.id.main_tv_muteinfo);

		radarImg = (ImageView) this.findViewById(R.id.main_iv_radar);
		// radarImg.setImageResource(R.drawable.rd_yes); //ttttsad
		radarTypeImg = (ImageView) this.findViewById(R.id.main_iv_warnradar);

		pbR = (ProgressBar) this.findViewById(R.id.pb_red);
		pbG = (ProgressBar) this.findViewById(R.id.pb_green);
		blockSpeedTv = (TextView) this.findViewById(R.id.block_tv_speed);
		blockSpeedLbTv = (TextView) this.findViewById(R.id.block_tv_speed_lb);
		blockSpaceTv = (TextView) this.findViewById(R.id.block_tv_space);
		blockSpaceLbTv = (TextView) this.findViewById(R.id.block_tv_space_lb);
		blockLimitSpeedIv = (ImageView) this
				.findViewById(R.id.block_iv_limitspeed);

		MainActivity.pbG.setVisibility(View.VISIBLE);
		MainActivity.pbG.setProgress(50);
		MainActivity.pbR.setVisibility(View.VISIBLE);
		MainActivity.pbR.setProgress(80);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.main_btn_floatwin:// 退�?  右下
			showExitDialog();
			break;
		case R.id.main_btn_setting:// 设置
			startActivity(new Intent(MainActivity.this, SettingActivity.class));
			break;
		case R.id.main_btn_spk:// 静音按钮
			if (DataPlay.isPlaying) {
				setMuteForver2(false);
			} else {
				setMuteForver2(true);
			}
			break;

		case R.id.main_btn_add_monitor:// 娣诲姞鐢靛瓙鐪
			// Toast.makeText(this, "璇ユ満鍨嬩笉鎻愪緵姝ゅ姛鑳?, Toast.LENGTH_SHORT).show();
			// V21 startActivity(new Intent(MainActivity.this,
			// EeyesActivity.class));
			break;
		/*
		 * case R.id.main_btn_add_monitor:// 添加电子�? //V1.1.5 //
		 * Toast.makeText(this, "该机型不提供此功能", Toast.LENGTH_SHORT).show();
		 * 
		 * 
		 * 
		 * edogJavaSImp.context = this; if(edogCppEngine.EnableAddPt()){
		 * EeyesActivity.flag = 0; startActivity(new Intent(MainActivity.this,
		 * EeyesActivity.class)); }
		 * 
		 * break;
		 */
		case R.id.main_btn_imitate:// 全屏---> 浮动窗口

			// Log.v(TAG, "isMinMode = true111");
/*
			// isMinMode = true;
			isMidMode = true;
			Log.e("dd", "isMinMode 2 ");
			// 创建 原来窗口
			ToolUtils.getInstance().showRunBgNotify(MainActivity.this);
			exit();
			startActivity(new Intent(this, FloatWindowActivity.class));
*/
			Backexit();
			
			
			// 创建浮动窗口
			// isMinMode = true;
			// TuzhiApplication.operateFloatView(this,
			// TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
			// exit();

			break;

		case R.id.main_btn_guide_exit: // 退出模拟驾�?
			// exitImitate(false);
			break;

		case R.id.main_btn_updatepoint:
			// Toast.makeText(this, "璇ユ満鍨嬩笉鎻愪緵姝ゅ姛鑳?, Toast.LENGTH_SHORT).show();
			// V21 startActivity(new Intent(MainActivity.this,
			// EeyesActivity.class));
			break;

		// case R.id.main_btn_updatepoint: //坐标修改
		// Toast.makeText(this, "该机型不提供此功能", Toast.LENGTH_SHORT).show();
		// break;

		case R.id.main_layout_direction:
			break;
		case R.id.main_layout_speed:
			break;
		case R.id.main_layout_blue: // 蓝色去点击 3分
			if (DataPlay.isPlaying) {
				setMute2(false);
			} else {
				setMute2(true);
			}
			break;
		case R.id.popup_ll_camera:
		case R.id.popup_ll_traffice:
		case R.id.popup_ll_flow_spd:
		case R.id.popup_ll_speed120:
		case R.id.popup_ll_speed110:
		case R.id.popup_ll_speed100:
		case R.id.popup_ll_speed90:
		case R.id.popup_ll_speed80:
		case R.id.popup_ll_speed70:
		case R.id.popup_ll_speed60:
		case R.id.popup_ll_speed50:
		case R.id.popup_ll_speed40:
		case R.id.popup_ll_speed30:
			Toast.makeText(this,
					eyeArray[Integer.parseInt(arg0.getTag().toString())],
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void startImitate() {
		guideRl.setVisibility(View.VISIBLE);
		// inBar.startAnimation(scaleAnimation);
		// RadarDataManager.setTestWarning(true);
	}

	private void exitImitate(boolean auto) {
		isStartImitate = false;
		// RadarDataManager.setTestWarning(false);
		// V1.2.6 ringView.restoreRingView(dataShow.getMaxLimitSpeed(), 0);

		kilometerTv.setText(String.format(
				getResources().getString(R.string.current_kilometer), 0));
		guideRl.setVisibility(View.GONE);
		// mHandler.removeCallbacks(runnable);
		if (scaleAnimation != null && !auto)
			scaleAnimation.cancel();
	}

	private void showPopupWindow(View v) {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		final View view = inflater.inflate(R.layout.popupwindow_layout, null);
		view.findViewById(R.id.popup_ll_traffice).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_camera).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_flow_spd).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed30).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed40).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed50).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed60).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed70).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed80).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed90).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed100).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed110).setOnClickListener(this);
		view.findViewById(R.id.popup_ll_speed120).setOnClickListener(this);

		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, 410, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.showAtLocation(v, Gravity.CENTER, 0, 20);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
	//		Log.e("dy", "keyCode:" + String.valueOf(keyCode) );
		switch (keyCode) {
		
	
		
		case KeyEvent.KEYCODE_BACK:
			showExitDialog();
			break;

			case KeyEvent.KEYCODE_HOME:
				Backexit();
				break;		
			
			
		default:
			break;
			
			
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_alert);
		builder.setMessage(R.string.dialog_exit_content);

		// 退出按钮 *TEMP
		builder.setNegativeButton(R.string.dialog_exit,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
						Edogexit();
						
/*
						Log.e("dd", "send power off ");
						Intent intent = new Intent();
						intent.setAction(POWER_OFF_RADAR_ACTION);
						sendBroadcast(intent);

						exit();
						if (serviceIntent != null)
							stopService(serviceIntent);
*/
					}
				});

		// 后台运行
		builder.setNeutralButton(R.string.dialog_run_background,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						 Backexit();
					}
				});
		builder.setPositiveButton(R.string.dialog_cancel, null);
		builder.create().show();
	}

	private void startActivitySafely(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
		} catch (SecurityException e) {
			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * @Override protected void onRestart() { super.onRestart(); Log.v(TAG,
	 * "onRestart"); }
	 * 
	 * public static boolean isStartImitate = false;
	 */
	private boolean isStartImitate = false;

	@Override
	protected void onResume() {
		if (isStartImitate) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			startImitate();
		}

		// Log.v(TAG, "isMinMode = true3333");
		// 移除浮动窗口

		isMinMode = false;
		isMidMode = false;
		Log.e("dd", "isMinMode 3 ");

	//	TuzhiApplication.operateFloatView(this,
	//			TuzhiService.ACTION_REMOVE_ALL_FLOATVIEW);

		//TuzhiApplication.operateFloatView(mContext,
		//		TuzhiService.ACTION_REMOVE_ALL_FLOATVIEW);
		
		TuzhiApplication.operateFloatView(MainActivity.this,
			TuzhiService.ACTION_REMOVE_ALL_FLOATVIEW);
		
		//	TuzhiApplication.operateFloatView(MainActivity.this,
			//	TuzhiService.ACTION_REMOVE_SPEED_FLOATVIEW);
		
		//	TuzhiApplication.operateFloatView(MainActivity.this,
		//		TuzhiService.ACTION_REMOVE_RADAR_FLOATVIEW);
		
		
		
		super.onResume();
	}

	/*
	 * @Override protected void onStop() { super.onStop(); Log.v(TAG, "onStop");
	 * }
	 */
	@Override
	public void onDestroy() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		DataPlay.isPlaying = false;
		// Log.v(TAG, "onDestroy");
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	/**
	 * 后台运行
*/	 
	private void Backexit() {
		


		isMinMode = true;

		// Log.v(TAG, "isMinMode = true222");
	//	Intent intent = new Intent();
	//	intent.setAction(POWER_ON_RADAR_ACTION);
		//sendBroadcast(intent);

		//ToolUtils.getInstance().showRunBgNotify(
		//		MainActivity.this);
		TuzhiService.isSystemBoot = true;
	//	TuzhiService.speedfvIsVisible = false;
	//	TuzhiService.radarfvIsVisible = false;
//		Intent serviceIntent = new Intent(MainActivity.this, LogoActivity.class);
//		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//startActivity(serviceIntent);
		startService(serviceIntent);
//		startActivity(new Intent(MainActivity.this,
//				TuzhiService.class));

		
		///remove float
		//TuzhiApplication.operateFloatView(MainActivity.this,
		//		TuzhiService.ACTION_CRREATE_ALL_FLOATVIEW);
		
	//	TuzhiApplication.operateFloatView(MainActivity.this,
	//			TuzhiService.ACTION_CRREATE_SPEED_FLOATVIEW);
		
	//	TuzhiApplication.operateFloatView(MainActivity.this,
	//			TuzhiService.ACTION_CRREATE_RADAR_FLOATVIEW);
		
		Intent startIntent = new Intent(Intent.ACTION_MAIN);
		startIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		PackageManager pm = getPackageManager();
		ResolveInfo homeInfo = pm.resolveActivity(new Intent(
				Intent.ACTION_MAIN)
				.addCategory(Intent.CATEGORY_HOME), 0);
		
		Log.e("homeInfo","homeInfo 1:"+homeInfo );
		
		
		if (homeInfo == null) {
			
			// Log.v(TAG, "isMinMode = true222");
			Log.e("homeInfo","homeInfo 2:"+homeInfo );
					
			startIntent.setComponent(new ComponentName(
							"com.daxun.launcher.activity",
							"com.daxun.launcher.activity.MainActivity"));
		} else {
			
			Log.e("homeInfo","homeInfo 3:"+homeInfo );
			
			ActivityInfo ai = homeInfo.activityInfo;
			startIntent.setComponent(new ComponentName(
					ai.packageName, ai.name));
		}

		//startActivitySafely(startIntent);
	
	}
	
	/**
	 * 退出应�?
 
	private void exit() {
		
	TuzhiApplication.exit();
	
	}	
	
*/	
	
	
	//private  void Edogexit() {  // static
	public void  Edogexit() {	
	//TuzhiApplication.exit();
			Log.e("dd", "send power off ");
      Intent intent = new Intent();  //
		intent.setAction(POWER_OFF_RADAR_ACTION);
		sendBroadcast(intent);

	  TuzhiApplication.exit();//	exit();
		if (serviceIntent != null)
			stopService(serviceIntent);
	}	
	
	
	

	

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HttpRequestManager.MSG_UPDATE_VERSION_SUCC:
				VersionInfo info = (VersionInfo) msg.obj;
				if (info == null) {
					return;
				}
				boolean isUpdate = ToolUtils.getInstance().isUpdate(
						MainActivity.this, info.versionNo);
				if (isUpdate) {
					DownloadUtils.getInstance().showUpdateDialog(
							MainActivity.this, info.downloadUrl,
							"V" + info.versionNo);
				}
				break;
			}
		};
	};

	/*
	 * // 静音 private Runnable muteRunnable = new Runnable() {
	 * 
	 * @Override public void run() { //WZJ1.0.9 isMute =
	 * SharedPreUtils.getInstance(MainActivity.this).getIntValue(Constants.MUTE)
	 * == 1 ? true : false; // Log.e("dy", "isMute2:" + String.valueOf(isMute));
	 * while (DataPlay.isPlaying) { //
	 * Log.e("dy","time:"+String.valueOf(System.currentTimeMillis
	 * ()-startMuteTime)); if ((System.currentTimeMillis() - startMuteTime) >
	 * 3*60 * 1000) {
	 * 
	 * DataPlay.isPlaying = false; muteHandler.sendEmptyMessage(0); } } } };
	 * 
	 * private Handler muteHandler = new Handler() { public void
	 * handleMessage(android.os.Message msg) { switch (msg.what) { case 0:
	 * setMute2(false); //setMute(false); break; } }; };
	 */

	private void setMute2(boolean state) { // /public static
		if (state) {
			// isMute = true;
			// muteThread = new Thread(muteRunnable);
			// muteThread.start();
			startMuteTime = 180;// 0 ;//System.currentTimeMillis();
			// WZJ1.0.9
			// SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE,
			// 1);
			muteTv.setText("3分钟静音，点击取消");
			spkBtn.setBackgroundResource(R.drawable.land_btn_spk_off);
			DataPlay.isPlaying = true;
		}
		if (!state) {
			// isMute = false;
			// if (muteThread != null) {
			// muteThread.interrupt();
			// muteThread = null;
			// }
			// WZJ1.0.9
			// SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE,
			// 0);

			startMuteTime = 0;
			muteTv.setText(" ");
			spkBtn.setBackgroundResource(R.drawable.land_btn_spk_on);
			DataPlay.isPlaying = false;
		}
	}

	private void setMuteForver2(boolean state) {
		if (state) {
			// isMute = true;
			blueRl.setEnabled(false); // 蓝色区域 不能点击
			spkBtn.setBackgroundResource(R.drawable.land_btn_spk_off);
			// WZJ1.0.9
			// SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE,
			// 1);
			DataPlay.isPlaying = true;
		}
		if (!state) {
			// isMute = false;
			blueRl.setEnabled(true);
			spkBtn.setBackgroundResource(R.drawable.land_btn_spk_on);
			// WZJ1.0.9
			// SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE,
			// 0);
			DataPlay.isPlaying = false;
			startMuteTime = 0;
		}
		muteTv.setText(" ");
	}

	/*
	 * 
	 * // 设置静音 true ; private void setMute(boolean state) { AudioManager
	 * mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	 * if (state) { isMute = true; muteThread = new Thread(muteRunnable);
	 * muteThread.start(); startMuteTime = System.currentTimeMillis();
	 * //WZJ1.0.9
	 * SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE, 1);
	 * muteTv.setText("3分钟静音，点击取消");
	 * spkBtn.setBackgroundResource(R.drawable.land_btn_spk_off); //WZJ1.0.9
	 * preAudioIndex = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	 * //WZJ1.0.9 mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
	 * 0); } if (!state) { isMute = false; if (muteThread != null) {
	 * muteThread.interrupt(); muteThread = null; } //WZJ1.0.9
	 * SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE, 0);
	 * muteTv.setText(" ");
	 * spkBtn.setBackgroundResource(R.drawable.land_btn_spk_on); //WZJ1.0.9
	 * mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preAudioIndex,
	 * 0); } }
	 * 
	 * 
	 * 
	 * private void setMuteForver(boolean state) { AudioManager mAudioManager =
	 * (AudioManager) getSystemService(Context.AUDIO_SERVICE); if (state) {
	 * isMute = true; blueRl.setEnabled(false);
	 * spkBtn.setBackgroundResource(R.drawable.land_btn_spk_off); //WZJ1.0.9
	 * SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE, 1);
	 * //WZJ1.0.9 preAudioIndex =
	 * mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); //WZJ1.0.9
	 * mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0); } if
	 * (!state) { isMute = false; blueRl.setEnabled(true);
	 * spkBtn.setBackgroundResource(R.drawable.land_btn_spk_on); //WZJ1.0.9
	 * SharedPreUtils.getInstance(this).commitIntValue(Constants.MUTE, 0);
	 * //WZJ1.0.9 mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
	 * preAudioIndex, 0); } muteTv.setText(" "); }
	 */

}

package com.hdsc.edog;

import com.hdsc.edog.entity.VersionInfo;
import com.hdsc.edog.net.HttpRequestManager;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.DownloadUtils;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;
import com.hdsc.edog.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingActivity extends Activity implements OnClickListener {
	
	private ArrayAdapter<CharSequence> adapter;
//	private String coms[] = {"关闭com口", "ttyS1", "ttyS2", "ttyS3", "ttyS4", "ttyS5", "ttyS6", "ttyS7","ttyMT1","ttyMT2","ttyMT3","ttyAMA0","ttyAMA1","ttyAMA2","ttyAMA3"};  //ttyS0
	private String coms[] = Constants.coms;
	private Spinner comsSpinner;
	
	private SharedPreUtils sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		TuzhiApplication.addActivity(this);
		sp = SharedPreUtils.getInstance(this);
		/*
		if(coms==null){
			coms = ToolUtils.getFiles("/dev");
		}*/
		initViews();
		if(coms != null){
			adapter = new ArrayAdapter<CharSequence>(this,
					android.R.layout.simple_spinner_item, coms);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			comsSpinner.setAdapter(adapter);
			
			if(coms[sp.getIntValue(Constants.RADAR_COM_P)].equals(sp.getStringValue(Constants.RADAR_COM))){
				comsSpinner.setSelection(sp.getIntValue(Constants.RADAR_COM_P));
			}else{
				comsSpinner.setSelection(0);
			}
				
		}
		
		
	}

	private void initViews(){
		findViewById(R.id.set_btn_back).setOnClickListener(this);
		findViewById(R.id.set_btn_about).setOnClickListener(this);
		findViewById(R.id.set_btn_intro).setOnClickListener(this);
		findViewById(R.id.set_btn_report).setOnClickListener(this);
		findViewById(R.id.set_btn_smartrcloud).setOnClickListener(this);
		findViewById(R.id.set_btn_feedback).setOnClickListener(this);
		findViewById(R.id.set_btn_softupdate).setOnClickListener(this);
//		findViewById(R.id.set_btn_twodimensioncode).setOnClickListener(this);
		findViewById(R.id.set_start_imitate).setOnClickListener(this);
		comsSpinner = (Spinner) this.findViewById(R.id.spinner_radar_com);
		
		comsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			/*
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				sp.commitStringValue(Constants.RADAR_COM, coms[arg2]);
				sp.commitIntValue(Constants.RADAR_COM_P, arg2);
			}
			*/
			
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2 != sp.getIntValue(Constants.RADAR_COM_P))
					showExitDialog(arg2);
			}
			
			
			
			
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.set_btn_back:
				finish();
				break;
			case R.id.set_btn_report:
				startActivity(new Intent(this, ReportSettingsActivity.class));
				break;
			case R.id.set_btn_smartrcloud:
				startActivity(new Intent(this, SmartCloudSettingActivity.class)); //SmartCloudSettingActivity  SetSmartrcloudActivity
				break;
			case R.id.set_btn_softupdate:
				dialog = new ProgressDialog(this);
				dialog.setMessage(getResources().getString(R.string.update_now));
				dialog.show();
				HttpRequestManager manager = HttpRequestManager.getInstance();
				manager.updateVersion(this, mHandler);
				mHandler.postDelayed(timeOutRunnable, timeOut);
				break;
			case R.id.set_start_imitate:
	//			MainActivity.isStartImitate = true;
	//			Intent intent = new Intent(this, MainActivity.class);
	//			startActivity(intent);
				break;
			case R.id.set_btn_feedback:
				startActivity(new Intent(this, SetFeedbackActivity.class));
				break;
			case R.id.set_btn_about:
				startActivity(new Intent(this, AboutActivity.class));
				break;
				
			case R.id.set_btn_intro:
				startActivity(new Intent(this, IntroduceActivity.class));
				break;
			default:
				break;
		}
	}
	
	private final int timeOut = 15000;
	ProgressDialog dialog;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(dialog != null){
				dialog.dismiss();
			}
			
			switch (msg.what) {
			case HttpRequestManager.MSG_UPDATE_VERSION_SUCC:
				removeCallback();
				VersionInfo info = (VersionInfo) msg.obj;
				if(info == null){
					return;
				}
						
				boolean isUpdate = ToolUtils.getInstance().isUpdate(
						SettingActivity.this, info.versionNo);
				if (isUpdate) {
					DownloadUtils.getInstance().showUpdateDialog(
							SettingActivity.this, info.downloadUrl,
							"V" + info.versionNo);
					// http://fileservice.365car.com.cn:88/fileService/obd/app/PD_OBD_CarButler.apk
				} else {
					Toast.makeText(SettingActivity.this,
							R.string.no_new_version_update, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case HttpRequestManager.MSG_UPDATE_VERSION_FAIL:
				removeCallback();
				Toast.makeText(SettingActivity.this,
						R.string.update_version_fail, Toast.LENGTH_SHORT)
						.show();	
			case HttpRequestManager.TIME_OUT:
				Toast.makeText(SettingActivity.this,
						R.string.update_version_timeout, Toast.LENGTH_SHORT)
						.show();	
				
				break;
			default:
				break;
			}
		};
	};
	
	private void removeCallback(){
		if(mHandler != null && timeOutRunnable != null){
			mHandler.removeCallbacks(timeOutRunnable);
		}
	}
	
	Runnable timeOutRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(dialog != null){
				dialog.dismiss();
			}
			if(mHandler != null)
				mHandler.sendEmptyMessage(HttpRequestManager.TIME_OUT);
		}
	};
	
	
	private void showExitDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_alert);
		builder.setMessage(R.string.dialog_changecom_content);
		builder.setNegativeButton(R.string.dialog_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sp.commitStringValue(Constants.RADAR_COM, coms[position]);
						sp.commitIntValue(Constants.RADAR_COM_P, position);
						Intent intent = new Intent();
						intent.setAction("radar.com.change");
						sendBroadcast(intent);
					}
				});
		builder.setPositiveButton(R.string.dialog_cancel, null);
		builder.create().show();
	}
	
}

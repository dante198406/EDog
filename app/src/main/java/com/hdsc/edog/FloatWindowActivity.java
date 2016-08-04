package com.hdsc.edog;

import com.hdsc.edog.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowActivity extends Activity implements OnClickListener {

	public static TextView fwKilometer, fwSpeed, fwDireciton;
	public static ImageView fwWarnType, fwWarnSpeed;
	public static TextView fwDir, fwDis;
	public static LinearLayout fwLlNormal, fwLlWarn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_flow_window);
		TuzhiApplication.addActivity(this);
		
		fwKilometer = (TextView) findViewById(R.id.speed_float_kilometer);
		fwSpeed = (TextView) findViewById(R.id.speed_float_speed);
		fwSpeed.setOnClickListener(this);
		fwDireciton = (TextView) findViewById(R.id.speed_float_direction);
		fwDireciton.setOnClickListener(this);
		findViewById(R.id.speed_float_id).setOnClickListener(this);
		fwWarnType = (ImageView)findViewById(R.id.fl_iv_warntype);
		fwWarnType.setOnClickListener(this);
		fwWarnSpeed = (ImageView)findViewById(R.id.fl_iv_warnspeed);
		fwWarnSpeed.setOnClickListener(this);
		fwDis = (TextView) findViewById(R.id.tv_fw_distance);
		fwDis.setOnClickListener(this);
		fwLlNormal = (LinearLayout) findViewById(R.id.fw_ll_normal);
		fwLlWarn = (LinearLayout) findViewById(R.id.fw_ll_warn);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.speed_float_id:
			startMainActivity();
		case R.id.fl_iv_warntype:
			startMainActivity();
			break;
		case R.id.speed_float_speed:
			startMainActivity();
			break;
		case R.id.tv_fw_distance:
			startMainActivity();
			break;
		case R.id.speed_float_direction:
			startMainActivity();
			break;
		case R.id.fl_iv_warnspeed:
			startMainActivity();
			break;
		default:
			break;
		}
	}
	
	private void startMainActivity(){
		finish();
		Intent intent = new Intent(FloatWindowActivity.this,
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(MainActivity.SHOW_DIALOG_FLAG, false);// 如果是最小化，不弹框
		startActivity(intent);
	}
}

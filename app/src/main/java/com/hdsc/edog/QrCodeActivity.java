package com.hdsc.edog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class QrCodeActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_qrcode);
		TuzhiApplication.addActivity(this);
		findViewById(R.id.set_about_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.set_about_back:
				finish();
				break;
			default:
				break;
		}
	}
}

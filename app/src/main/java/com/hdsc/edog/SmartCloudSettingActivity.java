package com.hdsc.edog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.hdsc.edog.adapter.SmartCloudAdapter;
import com.hdsc.edog.entity.ReportInfo;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class SmartCloudSettingActivity extends Activity implements OnClickListener {

	ListView listView;
	
	String[] nameString = new String[]{Constants.BOOT_START,Constants.SMARTTCLOUND_AUTOUPDATE,
			Constants.SMARTTCLOUND_AUTOPOST,Constants.SMARTTCLOUND_GAINFLOW};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_smartrcloud);
		TuzhiApplication.addActivity(this);
		initViews();
	}

	private void initViews(){
		DecimalFormat dff  = new DecimalFormat("0.00");//这样为保持2位
		float m = 1024f * 1024f;
		String total = TuzhiService.GPRSTOTAL < m ? (TuzhiService.GPRSTOTAL / 1024) + "KB" : 
			dff.format(TuzhiService.GPRSTOTAL/(1024f * 1024f)) + "MB";
		((TextView)findViewById(R.id.set_cloud_gprstotal)).setText(total);
		findViewById(R.id.set_smartrcloud_back).setOnClickListener(this);
		ArrayList<ReportInfo> list = new ArrayList<ReportInfo>();
		for(int i=0;i<nameString.length;i++){
			ReportInfo info = new ReportInfo();
			info.name = nameString[i];
			info.state = SharedPreUtils.getInstance(this).getIntValue(info.name);
			list.add(info);
		}
		listView = (ListView) findViewById(R.id.set_smartrcloud_listview);
		listView.setAdapter(new SmartCloudAdapter(this, list));
		
		
		this.findViewById(R.id.set_clound_ewm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						SmartCloudSettingActivity.this
								.startActivity(new Intent(
										SmartCloudSettingActivity.this,
										QrCodeActivity.class));
						finish();
					}

				});
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.set_smartrcloud_back:
				finish();
				break;
			default:
				break;
		}
	}
	
	
}

package com.hdsc.edog;

import java.util.ArrayList;

import com.hdsc.edog.adapter.ReportSettingsAdapter;
import com.hdsc.edog.entity.ReportInfo;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class ReportSettingsActivity extends Activity implements OnClickListener {

	ListView listView;
	String[] nameString = new String[] { Constants.W_RED_LIGHT,
			Constants.W_SPEED_LIMIT, Constants.W_TRAFFIC_RULE,
			Constants.W_SAFE, Constants.W_RADAR_MODE, Constants.W_MAX_SPEED,
			Constants.W_RADAR_MUTE_SPEED, Constants.W_SPEED_MODIFY , Constants.RECOVERY};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_report);
		TuzhiApplication.addActivity(this);
		initViews();
	}

	private void initViews() {
		findViewById(R.id.set_report_back).setOnClickListener(this);
		ArrayList<ReportInfo> list = new ArrayList<ReportInfo>();
		for (int i = 0; i < nameString.length; i++) {
			ReportInfo info = new ReportInfo();
			info.name = nameString[i];
			info.state = SharedPreUtils.getInstance(this)
					.getIntValue(info.name);
			list.add(info);
		}
		listView = (ListView) findViewById(R.id.set_report_listview);
		listView.setAdapter(new ReportSettingsAdapter(this, list));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.set_report_back:
			finish();
			break;
		default:
			break;
		}
	}

}

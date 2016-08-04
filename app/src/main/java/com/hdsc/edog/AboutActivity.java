package com.hdsc.edog;

import com.hdsc.edog.adapter.AboutAdapter;
import com.hdsc.edog.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class AboutActivity extends Activity implements OnClickListener {

	ListView listView;
	String[] REVnameString = new String[]{"软件版本号：V1.2.7","开发者：速安达科技（zjWen）","电子眼数据授权：先知眼","开发时间：2014年5月5日","适应系统：Android2.3以上系统","修改时间20160606"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_about);
		TuzhiApplication.addActivity(this);
		initViews();
		
	}

	private void initViews(){
		findViewById(R.id.set_about_back).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.set_about_listview);
		listView.setAdapter(new AboutAdapter(this, REVnameString));
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

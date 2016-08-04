package com.hdsc.edog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.hdsc.edog.R;

public class LiveRoadConditionActivity extends Activity implements OnClickListener {
	private AMap aMap;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_road_cond);
		TuzhiApplication.addActivity(this);
		mapView = (MapView) findViewById(R.id.lrc_map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initViews();
	}

	private void initViews(){
		findViewById(R.id.btn_map_location).setOnClickListener(this);
		findViewById(R.id.lrc_btn_back).setOnClickListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		aMap.setTrafficEnabled(true);// 显示实时交通状况
		//设置地图中心点
//		LatLng ll = new LatLng(TuzhiService.lat, TuzhiService.lon);
//		aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(ll, 18, 0, 30)));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lrc_btn_back:
			finish();
			break;
		case R.id.btn_map_location:
//			LatLng ll = new LatLng(TuzhiService.lat, TuzhiService.lon);
//			aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(ll, 18, 0, 30)));
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 方法必须重写
	 
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();

	}
*/
	/**
	 * 方法必须重写
	 
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
*/
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
*/

	
	
}

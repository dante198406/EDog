package com.hdsc.edog;

import com.hdsc.edog.adapter.GVAdapter;
import com.hdsc.edog.jni.FixedGps;
import com.hdsc.edog.jni.RadarDataManager;
import com.hdsc.edog.jni.eEyeInfo;
import com.hdsc.edog.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EeyesActivity extends Activity implements OnClickListener {
	
	public static int flag = 0;
	String[] step0 = new String[]{"0m","50m","100m","150m","200m","250m","300m"};
	String step1[] = new String[]{"前方","平面道路","前方桥上","前方桥下","前方主道","前方辅道","左前方","右前方","高速道路","普通道路","隧道入口","隧道出口","隧道内","连续","--","删除这点"};
	String step2[] = new String[]{"闯红灯测速照相","固定测速照相","NC","限高","流动照相区","违章监控路段","为区间测速*终点","安全车距测速照相","有高架测速照相","限重","隧道内测速照相","隧道口测速照相","区间测速*起点"};
	String step3_1[] = new String[]{"多雾路段","易落石路段","事故多发路段","急下坡路段","交流道","为系统交流道","机场路段","学校路段",
			"商场路段","遵守交通规则路段","有加气站","有隧道，请开头灯","地下道","禁止左转弯","禁止右转弯","禁止调头","警察局","火车站","有加油站",
			"有收费站","有休息服务区","禁停路段","越线违章拍照路段","单行道请勿逆向行驶","右则为公交车道照相","有汽车美容装饰中心","有汽车修理厂","有汽车站","为风景区","山区路段",
			"冰雪路段","为检查站","K 频雷达测速区","为市政府","闯红灯拍照","闯红灯压黄线拍照","尾号限行路段","有减速带","有高架出口","违规监控照相","高速公路出口",
			"Ka 频雷达","铁路道口","禁行路段","有立交桥","私家车限时通行路段"};
	String step3_2[] = new String[]{"20Km/h","30Km/h","40Km/h","50Km/h","60Km/h","70Km/h","80Km/h","90Km/h",
			                        "100Km/h","110Km/h","120Km/h","无限速"};
	
	int currentPage = 0;//当前的步骤数，从0开始
	int step3 = 2;//第三步的分支：1代表第三步的第一分支，2代表第三步的第二分支
	
	GridView gv;
	Button btnStep3, preBtn, btnCancel;
	Button btnZx, btnFx, btnSx;
	ScrollView llStep4;//最后1一把也是第四步的布局
	TextView tv;
	static TextView tvLongi, tvLate, tvBear;
	
	eEyeInfo eInfo = new eEyeInfo();
	final String splitFlag = "\\";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eeyes_layout);
		TuzhiApplication.addActivity(this);
		initViews();
	}
	
	/**
	 * 下一个步骤UI的刷新
	 * @param step3：判断第三步的分支
	 */
	private void showNext(int step3){
		switch (currentPage) {
		case 0:
			currentPage = 1;
			gv.setAdapter(new GVAdapter(this, step1, mHandler, currentPage));
			break;
		case 1:
			currentPage = 2;
			gv.setAdapter(new GVAdapter(this, step2, mHandler, currentPage));
			break;
		case 2:
			currentPage = 3;
			if(step3 == 1){
				gv.setAdapter(new GVAdapter(this, step3_1, mHandler, currentPage));
			}else{
				gv.setAdapter(new GVAdapter(this, step3_2, mHandler, currentPage));
			}
			break;
		case 3:
			currentPage = 4;
			break;
		default:
			break;

		}
		hideView(currentPage);
	}
	
	/**
	 * 上一个步骤UI的刷新
	 * @param step3：判断第三步的分支
	 */
	private void showPre(int step3){
		switch (currentPage) {
		case 4:
			currentPage = 3;
			if(step3 == 1){
				gv.setAdapter(new GVAdapter(this, step3_1, mHandler, currentPage));
			}else{
				gv.setAdapter(new GVAdapter(this, step3_2, mHandler, currentPage));
			}
			break;
		case 3:
			currentPage = 2;
			gv.setAdapter(new GVAdapter(this, step2, mHandler, currentPage));
			break;
		case 2:
			currentPage = 1;
			gv.setAdapter(new GVAdapter(this, step1, mHandler, currentPage));
			break;
		case 1:
			currentPage = 0;
			gv.setAdapter(new GVAdapter(this, step0, mHandler, currentPage));
			break;
		}
		hideView(currentPage);
	}
	
	/**
	 * 根据当前步骤显示或隐藏那些UI
	 * @param page
	 */
	private void hideView(int page){
		switch (page) {
		case 0:
			preBtn.setVisibility(View.INVISIBLE);
			btnStep3.setVisibility(View.INVISIBLE);
			gv.setVisibility(View.VISIBLE);
			llStep4.setVisibility(View.GONE);
			break;
		case 1:
			preBtn.setVisibility(View.VISIBLE);
			btnStep3.setVisibility(View.INVISIBLE);
			gv.setVisibility(View.VISIBLE);
			llStep4.setVisibility(View.GONE);
			break;
		case 2:
			preBtn.setVisibility(View.VISIBLE);
			btnStep3.setVisibility(View.VISIBLE);
			gv.setVisibility(View.VISIBLE);
			llStep4.setVisibility(View.GONE);
			break;
		case 3:
			preBtn.setVisibility(View.VISIBLE);
			btnStep3.setVisibility(View.INVISIBLE);
			gv.setVisibility(View.VISIBLE);
			llStep4.setVisibility(View.GONE);
			break;
		case 4:
			preBtn.setVisibility(View.VISIBLE);
			btnStep3.setVisibility(View.INVISIBLE);
			gv.setVisibility(View.GONE);
			llStep4.setVisibility(View.VISIBLE);
			break;

		}
	}
	
	StringBuffer sb = new StringBuffer();
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			addStepString(msg.obj.toString());
			switch (msg.what) {
				case 0:
					
					/*
					FixedGps fgps = edogCppEngine.getFixedGps();
					if(fgps != null){
						eInfo.m_dir = fgps.m_Direction;
						eInfo.m_Lat = (float)fgps.m_Lat;
						eInfo.m_Lng = (float)fgps.m_Long;
					}
					
					*/
					
					eInfo.m_errorRange = Integer.parseInt(msg.obj.toString().replace("m", ""));
					break;
				case 1:
					eInfo.m_siteType = msg.arg1;
					break;
				case 2:
					step3 = 2;
//					eInfo.m_ptType = RadarDataManager.getPointTypeByName(msg.obj.toString());
					break;
				case 3:
					if(step3 == 1){//第二步点击“其他安全信息坐标”按钮
						eInfo.m_Speed = 0;
//						eInfo.m_ptType = RadarDataManager.getPointTypeByName(msg.obj.toString());
					}else{
						if(step3_2[step3_2.length-1].equals(msg.obj.toString())){
							eInfo.m_Speed = 0;
						}else{
							try{
								eInfo.m_Speed =  Integer.parseInt(msg.obj.toString().replace("Km/h", ""));
								//	eInfo.m_Speed =  Float.parseFloat(msg.obj.toString().replace("Km/h", ""));
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					break;
			}
			showNext(step3);
		};
	};

	private void addStepString(String content){
		sb.append(splitFlag).append(content);
		if(flag > 0){
			tv.setText(new StringBuffer().append(getString(R.string.eeyes_update))
					.append("   ").append(sb.toString()).toString());
		}else{
//			tv.setText(new StringBuffer().append("新建坐标编号：").append(RadarDataManager.getEyePtIndex())
//					.append("   ").append(sb.toString()).toString());
		}
		
		

		
				
	
		
		updateLocationData(eInfo);	
		
	}
	
	
	
	
	private void initViews(){
		gv = (GridView) findViewById(R.id.eeyes_gridview);
		gv.setAdapter(new GVAdapter(this, step0, mHandler, currentPage));
		btnStep3 = (Button) findViewById(R.id.eeyes_btn_step3_next);
		btnStep3.setOnClickListener(this);
		preBtn = (Button) findViewById(R.id.eeyes_btn_next);
		preBtn.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.eeyes_btn_cancel);
		btnCancel.setOnClickListener(this);
		btnZx = (Button) findViewById(R.id.eeyes_btn_zxdd);
		btnZx.setOnClickListener(this);
		btnFx = (Button) findViewById(R.id.eeyes_btn_fxdd);
		btnFx.setOnClickListener(this);
		btnSx = (Button) findViewById(R.id.eeyes_btn_sxed);
		btnSx.setOnClickListener(this);
		findViewById(R.id.eeyes_btn_zfdd).setOnClickListener(this);
		findViewById(R.id.eeyes_btn_yfdd).setOnClickListener(this);
		findViewById(R.id.eeyes_btn_zyed).setOnClickListener(this);
		findViewById(R.id.eeyes_btn_qhzysd).setOnClickListener(this);
		llStep4 = (ScrollView) findViewById(R.id.eeyes_ll_stepfour);
		tv = (TextView) findViewById(R.id.eeyes_tv);
		if(flag > 0){
			tv.setText(R.string.eeyes_update);
		}else{
//			tv.setText("新建坐标编号："+RadarDataManager.getEyePtIndex()+"   --- 按【添加电子眼】时，汽车经过报警点大哟多少米距离。  " );
		}
	//	tvLate = (TextView) findViewById(R.id.eeyes_tv_late);
	//	tvLongi = (TextView) findViewById(R.id.eeyes_tv_longi);
	//	tvBear = (TextView) findViewById(R.id.eeyes_tv_bear);
		
		
		
	//	eInfo = new eEyeInfo() ;
		
//		FixedGps fgps = RadarDataManager.getFixedGps();
//		if(fgps != null){
//			eInfo.m_dir =  fgps.m_Direction;
//			eInfo.m_Lat =  fgps.m_Lat;
//			eInfo.m_Lng =  fgps.m_Long;  //(float)
//			
//				
//		}
		
		updateLocationData(eInfo);
		
	//		tv.setText("新建坐标编号：" +edogCppEngine.getEyePtIndex()+"纬度" +  eInfo.m_Lat +  "经度" +  eInfo.m_Lng     ); //杩欓噷鏄涓€涓嬫寜鏂板缓鍧愭爣	

		
		
		
		
	}
	
	@Override
	public void onClick(View v) {
		
		
		updateLocationData(eInfo);	
		
		switch (v.getId()) {
		case R.id.eeyes_btn_step3_next:
			step3 = 1;
		//	addStepString(getString(R.string.eeyes_other));
			showNext(step3);
			break;
		case R.id.eeyes_btn_next:
			int index = sb.toString().lastIndexOf(splitFlag);
			sb.delete(index, sb.toString().length());
			if(flag > 0){
				tv.setText(new StringBuffer().append(getString(R.string.eeyes_update)).append("   ")
						.append(sb.toString()).toString());
			}else{
//				tv.setText(new StringBuffer().append("新建坐标编号：").append(RadarDataManager.getEyePtIndex())
//						.append("   ").append(sb.toString()).toString());
			}
			showPre(step3);
			break;
		case R.id.eeyes_btn_cancel:
			finish();
			break;
		case R.id.eeyes_btn_zxdd:
			operEEyeInfo(0);
			break;
		case R.id.eeyes_btn_fxdd:
			operEEyeInfo(1);
			break;
		case R.id.eeyes_btn_sxed:
			operEEyeInfo(2);
			break;
		case R.id.eeyes_btn_zfdd:
			operEEyeInfo(3);
			break;
		case R.id.eeyes_btn_yfdd:
			operEEyeInfo(4);
			break;
		case R.id.eeyes_btn_zyed:
			operEEyeInfo(5);
			break;
		case R.id.eeyes_btn_qhzysd:
			operEEyeInfo(6);
			break;
		default:
			break;
		}
		
		
		
		
		
	}
	
	private void operEEyeInfo(int type){
		eInfo.m_dirType = type;
		if(flag > 0){
//			RadarDataManager.editEEyeInfo(eInfo);
		}else{
//			RadarDataManager.addEEyeInfo(eInfo);
		}
		eEyeInfo info = eInfo;
		finish();
	}
	
	
	

/*
//	private static Location location ;
	public static Handler locationHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
//			location = (Location)msg.obj;
		//	FixedGps fgps = edogCppEngine.getFixedGps();
		//	updateLocationData(fgps);
		//	
		//	FixedGps fgps; 
		//	eInfo.m_dir =  fgps.m_Direction;
		//	eInfo.m_Lat =  fgps.m_Lat;
		//	eInfo.m_Lng =  fgps.m_Long;  //(float)	
			
			
			
			updateLocationData();
		};
	};
	
	
		
		
	private static void updateLocationData(FixedGps fgps){
		if(tvBear == null || tvLongi == null || tvLate == null){
			return;
		}
		if(fgps != null){
			tvBear.setText("角度："+fgps.m_Direction);
			tvLongi.setText("经度："+fgps.m_Long);
			tvLate.setText("纬度："+fgps.m_Lat);
		}else{
			tvBear.setText("角度：X");
			tvLongi.setText("经度：X");
			tvLate.setText("纬度：X");
		}
	}
	*/
	
	private static void updateLocationData(eEyeInfo  eInfo){
		if(tvBear == null || tvLongi == null || tvLate == null){
			return;
		}
			
		
			tvBear.setText("角度："+ eInfo.m_dir);
			tvLongi.setText("经度："+ eInfo.m_Lng);
			tvLate.setText("纬度："+ eInfo.m_Lat);
			
			
		
	
		//		tvBear.setText(String.format(getResources().getString(R.string.eeyes_bear),eInfo.m_dir));
		
		//		tvLate.setText(String.format(getResources().getString(R.string.eeyes_late),eInfo.m_Lat));
		
		//		tvLongi.setText(String.format(getResources().getString(R.string.eeyes_longi),eInfo.m_Lng));
			
			
			
			
			
			
			
			
	
	}
	
	
	
}

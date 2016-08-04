package com.hdsc.edog.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hdsc.edog.MainActivity;
import com.hdsc.edog.R;
import com.hdsc.edog.entity.ReportInfo;
import com.hdsc.edog.jni.EdogDataManager;
import com.hdsc.edog.jni.RadarDataManager;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;

public class ReportSettingsAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	private RadarDataManager radarDataManager;
	private EdogDataManager edogDataManager;

	// 定义整型数组 即图片源
	private ArrayList<ReportInfo> list;
	SharedPreUtils sp;
	ArrayAdapter<CharSequence> adapter;
	ArrayAdapter<CharSequence> adapter2;
	ArrayAdapter<CharSequence> adapter3, adapter4;
	String data[] = new String[] { "智能模式", "市区模式", "高速模式", "关闭" };
	String data2[] = new String[] { "80Km/h", "90Km/h", "100Km/h", "110Km/h",
			"120Km/h", "130Km/h", "140Km/h", "150Km/h", "160Km/h" };
	String data3[] = new String[] { "取消静音", "10Km/h", "20Km/h", "30Km/h",
			"40Km/h", "50Km/h", "60Km/h", "70Km/h", "80Km/h" };
	String data4[] = new String[] { "-9%", "-8%", "-7%", "-6%", "-5%", "-4%",
			"-3%", "-2%", "-1%", "0%", "1%", "2%", "3%", "4%", "5%", "6%",
			"7%", "8%", "9%" };

	public ReportSettingsAdapter(Context c, ArrayList<ReportInfo> list) {
		mContext = c;
		this.list = list;
		sp = SharedPreUtils.getInstance(c);
		adapter = new ArrayAdapter<CharSequence>(mContext,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2 = new ArrayAdapter<CharSequence>(mContext,
				android.R.layout.simple_spinner_item, data2);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter3 = new ArrayAdapter<CharSequence>(mContext,
				android.R.layout.simple_spinner_item, data3);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter4 = new ArrayAdapter<CharSequence>(mContext,
				android.R.layout.simple_spinner_item, data4);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		radarDataManager = new RadarDataManager(mContext);
		edogDataManager = new EdogDataManager(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.set_report_listview_item, null);
			viewholder = new ViewHolder();
			viewholder.tv = (TextView) convertView
					.findViewById(R.id.set_report_lv_item_tv);
			viewholder.ivOff = (ImageView) convertView
					.findViewById(R.id.set_report_lv_item_iv_off);
			viewholder.ivOn = (ImageView) convertView
					.findViewById(R.id.set_report_lv_item_iv_on);
			viewholder.spinner = (Spinner) convertView
					.findViewById(R.id.set_report_lv_item_spinner);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		final ReportInfo info = list.get(position);
		viewholder.tv.setText(info.name);
		if (position == list.size() - 1 || position == list.size() - 2
				|| position == list.size() - 3 || position == list.size() - 4|| position == list.size() - 5) {
			viewholder.spinner.setVisibility(View.VISIBLE);
			viewholder.ivOff.setVisibility(View.GONE);
			viewholder.ivOn.setVisibility(View.GONE);
		} else {
			viewholder.spinner.setVisibility(View.GONE);
			if (info.state == 0) {
				viewholder.ivOff.setVisibility(View.GONE);
				viewholder.ivOn.setVisibility(View.VISIBLE);
			} else {
				viewholder.ivOff.setVisibility(View.VISIBLE);
				viewholder.ivOn.setVisibility(View.GONE);
			}
		}
		viewholder.ivOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				info.state = 0;
				sp.commitIntValue(info.name, info.state);
				 Log.e("dy","name:"+info.name+"/state:"+String.valueOf(info.state));
				boolean result = (info.state == 0 ? true : false);
				if ("闯红灯提醒".equals(info.name)) {
					// RadarDataManager.setHongDengHint(result);
				} else if ("交通违规提示".equals(info.name)) {
					// RadarDataManager.setWeiGuiHint(result);
				} else if ("安全提示".equals(info.name)) {
					// RadarDataManager.setAnQuanHint(result);
				} else if ("雷达模式".equals(info.name)) {
					// RadarDataManager.setRadarMode(info.state);
				}
				notifyDataSetChanged();
			}
		});
		viewholder.ivOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				info.state = 1;
				sp.commitIntValue(info.name, info.state);
				 Log.e("dy","name2:"+info.name+"/state2:"+String.valueOf(info.state));
				boolean result = (info.state == 0 ? true : false);
				if ("闯红灯提醒".equals(info.name)) {
					// RadarDataManager.setHongDengHint(result);
				} else if ("交通违规提示".equals(info.name)) {
					// RadarDataManager.setWeiGuiHint(result);
				} else if ("安全提示".equals(info.name)) {
					// RadarDataManager.setAnQuanHint(result);
				} else if ("雷达模式".equals(info.name)) {
					// RadarDataManager.setRadarMode(info.state);
				}
				notifyDataSetChanged();
			}
		});

		
		
		
		
		
		
		
		if (position == list.size() - 4) {
			viewholder.spinner.setAdapter(adapter2);
			int limitSpeed = edogDataManager.getMaxLimitSpeed();
			if (limitSpeed < 80 || limitSpeed > 160) {
				viewholder.spinner.setSelection(4);   //设置120km
				
			} else {
				int size = limitSpeed / 10 - 8;
				if (size < data2.length && size >= 0) {
					viewholder.spinner.setSelection(size);
				}
			}
		} else if (position == list.size() - 3) {
			viewholder.spinner.setAdapter(adapter3);
			int muteSpeed = radarDataManager.getRadarMuteSpeed();
			if (muteSpeed < 0 || muteSpeed > 80) {
				viewholder.spinner.setSelection(4);   //设置 40km
			} else {
				int size = muteSpeed / 10;
				if (size < data3.length && size >= 0) {
					viewholder.spinner.setSelection(size);
				}
			}
		} else if (position == list.size() - 2) {
			viewholder.spinner.setAdapter(adapter4);
			int speedModify = edogDataManager.getSpeedModify();
			if (speedModify < -9 || speedModify > 9) {
				viewholder.spinner.setSelection(0);
			} else {
				int size = speedModify + 9;
				if (size < data4.length && size >= 0) {
					viewholder.spinner.setSelection(size);
				}
			}
		}else if (position == list.size() - 1) {
			//鎭㈠鍑哄巶璁剧疆
			viewholder.spinner.setVisibility(View.GONE);
			viewholder.tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					showRecoveryDialog();
				}
				
			});
		}else if(position == list.size() - 5) {
			viewholder.spinner.setAdapter(adapter);
			int index = radarDataManager.getRadarMode();
			if (index > 0 && index <=data.length) {
				viewholder.spinner.setSelection(index - 1);
			}
			
		} else if(position == list.size() - 6) {
			if(edogDataManager.isSafeOn()){
				viewholder.ivOn.setVisibility(View.VISIBLE);
				viewholder.ivOff.setVisibility(View.GONE);
			}
		}else if(position == list.size() - 7) {
			if(edogDataManager.isTrafficRuleOn()){
				viewholder.ivOn.setVisibility(View.VISIBLE);
				viewholder.ivOff.setVisibility(View.GONE);
			}
		}else if(position == list.size() - 8) {
			if(edogDataManager.isSpeedLimitOn()){
				viewholder.ivOn.setVisibility(View.VISIBLE);
				viewholder.ivOff.setVisibility(View.GONE);
			}
		}else if(position == list.size() - 9) {
			if(edogDataManager.isRedLightOn()){
				viewholder.ivOn.setVisibility(View.VISIBLE);
				viewholder.ivOff.setVisibility(View.GONE);
			}
		}	
			
			viewholder.spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
				
					/*	
						if (position == list.size() - 3) {
							edogDataManager.setMaxLimitSpeed(Integer
									.parseInt(data2[arg2].replace("Km/h", "")));
						} else if (position == list.size() - 2) {
							radarDataManager.setRadarMuteSpeed(Integer
									.parseInt(data3[arg2].replace("Km/h", "")));
						} else if (position == list.size() - 1) {
							edogDataManager.setSpeedModify(Integer.parseInt(data4[arg2].replace("%","")));
						} else {
							radarDataManager.setRadarMode(arg2 + 1);
						}

					}
					*/
					if (position == list.size() - 4) {
						
				
						edogDataManager.setMaxLimitSpeed(Integer
								.parseInt(data2[arg2].replace("Km/h", "")));
						
						
						
					} else if (position == list.size() - 3) {
						
						if(arg2 == 0){
							radarDataManager.setRadarMuteSpeed(Integer
									.parseInt("0"));
						}
						else{
						
						
						radarDataManager.setRadarMuteSpeed(Integer
								.parseInt(data3[arg2].replace("Km/h", "")));
						
						}
						
						
					} else if (position == list.size() - 2) {
						edogDataManager.setSpeedModify(Integer.parseInt(data4[arg2].replace("%","")));
					}else if (position == list.size() - 1) {
//						showRecoveryDialog();
					} else {
						radarDataManager.setRadarMode(arg2 + 1);
					}

				}		
					
					
					
					
					

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView ivOff, ivOn;
		Spinner spinner;
	}


private void showRecoveryDialog() {
	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	builder.setTitle(R.string.dialog_alert);
	builder.setMessage(R.string.dialog_recovery);
	builder.setNegativeButton(R.string.dialog_ok,
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					recovery();
				}
			});
	builder.setPositiveButton(R.string.dialog_cancel, null);
	builder.create().show();
}

private void recovery(){
	edogDataManager.setRedLightOn(true);
	edogDataManager.setSpeedLimitOn(true);
	edogDataManager.setTrafficRuleOn(true);
	edogDataManager.setSafeOn(true);
	radarDataManager.setRadarMode(1);
	edogDataManager.setMaxLimitSpeed(120);
	edogDataManager.setSpeedModify(0);
	radarDataManager.setRadarMuteSpeed(40);
	this.notifyDataSetChanged();
}
}



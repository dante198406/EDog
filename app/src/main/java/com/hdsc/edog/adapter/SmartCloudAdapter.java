package com.hdsc.edog.adapter;

import java.util.ArrayList;

import com.hdsc.edog.entity.ReportInfo;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SmartCloudAdapter extends BaseAdapter
//public class SmartCloudAdapter extends BaseAdapter
{
	// 定义Context
	private Context		mContext;
	// 定义整型数组 即图片源
	private ArrayList<ReportInfo> list;
	SharedPreUtils sp;
	
	public SmartCloudAdapter(Context c, ArrayList<ReportInfo> list)
	{
		mContext = c;
		this.list = list;
		sp = SharedPreUtils.getInstance(c);
	}

	public int getCount()
	{
		return list.size();
	}

	public Object getItem(int position)
	{
		return position;
	}


	public long getItemId(int position)
	{
		return position;
	}


	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewholder = null;
		if (null == convertView) 
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.set_smartccloud_listview_item, null);
			viewholder = new ViewHolder();
			viewholder.tv = (TextView) convertView.findViewById(R.id.set_cloud_lv_item_tv);
			viewholder.ivOff = (ImageView) convertView.findViewById(R.id.set_cloud_lv_item_iv_off);
			viewholder.ivOn = (ImageView) convertView.findViewById(R.id.set_cloud_lv_item_iv_on);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		final ReportInfo info = list.get(position);
		viewholder.tv.setText(info.name);
		if(info.state == 1){
			viewholder.ivOff.setVisibility(View.GONE);
			viewholder.ivOn.setVisibility(View.VISIBLE);
		}else{
			viewholder.ivOff.setVisibility(View.VISIBLE);
			viewholder.ivOn.setVisibility(View.GONE);
		}
		viewholder.ivOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				info.state = 1;
				sp.commitIntValue(info.name, info.state);
				notifyDataSetChanged();
			}
		});
		viewholder.ivOn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				info.state = 0;
				sp.commitIntValue(info.name, info.state);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView ivOff, ivOn;
	}
}


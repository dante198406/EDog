package com.hdsc.edog.adapter;

import com.hdsc.edog.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AboutAdapter extends BaseAdapter
{
	// 定义Context
	private Context		mContext;
	// 定义整型数组 即图片源
	private String[] list;

	public AboutAdapter(Context c, String[] list)
	{
		mContext = c;
		this.list = list;
	}

	public int getCount()
	{
		return list.length;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.set_about_listview_item, null);
			viewholder = new ViewHolder();
			viewholder.tv = (TextView) convertView.findViewById(R.id.set_about_lv_item_tv);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		viewholder.tv.setText(list[position]);
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}
}


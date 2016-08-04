package com.hdsc.edog.adapter;


import com.hdsc.edog.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class GVAdapter extends BaseAdapter
{
	// 定义Context
	private Context		mContext;
	// 定义整型数组 即图片源
	private String[] arryStr;
	private Handler mHandler;
	private int currentPage;

	public GVAdapter(Context c, String[] arryStr, Handler mHandler, int currentPage)
	{
		mContext = c;
		this.arryStr = arryStr;
		this.mHandler = mHandler;
		this.currentPage = currentPage;
	}

	// 获取图片的个数
	public int getCount()
	{
		return arryStr.length;
	}

	// 获取图片在库中的位置
	public Object getItem(int position)
	{
		return position;
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.eeyes_gridview_item, null);
			holder.button = (Button) convertView.findViewById(R.id.eeyes_gv_item_btn);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}

		holder.button.setText(arryStr[position]);
		
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = currentPage;
				msg.obj = arryStr[position];
				msg.arg1 = position;
				mHandler.sendMessage(msg);
			}
		});
		return convertView;
	}

	class ViewHolder{
		public Button button;
		}
}


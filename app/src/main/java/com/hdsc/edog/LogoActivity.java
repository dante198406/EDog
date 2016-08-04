package com.hdsc.edog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.hdsc.edog.entity.VersionInfo;
import com.hdsc.edog.net.HttpRequestManager;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;

public class LogoActivity extends Activity  {//Activity implements Runnable
	 //是否是第一次使用  
    private boolean isFirstUse;  
    private SharedPreUtils sp;
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				/** 
	             *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面 
	             */  
				
		        if (isFirstUse) {  
	                 startActivity(new Intent(LogoActivity.this, GuideActivity.class));  
	            } else { 
	                startActivity(new Intent(LogoActivity.this, MainActivity.class));  
	            }  
	            LogoActivity.this.finish();  
	              
	        	sp.commitBooleanValue(Constants.IS_FIRST_USE, false);
				
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.logo);
		
		//读取SharedPreferences中需要的数据  
		
		sp = SharedPreUtils.getInstance(this);
		// 判断程序是否是第一次使用
		isFirstUse = sp.getBooleanValue(Constants.IS_FIRST_USE, true);
		
		
		
		HttpRequestManager manager = HttpRequestManager.getInstance();
		if (manager.checkNetwork(this)) {
			manager.updateVersion(LogoActivity.this, mHandler);
		} else {
			try {
				ToolUtils.copyFile(LogoActivity.this, false);
		//		ToolUtils.copylib(LogoActivity.this, false);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(1); // 给UI主线程发送消息
				}
			}, 1000); // 启动等待3秒钟2000
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HttpRequestManager.MSG_UPDATE_VERSION_SUCC:
				VersionInfo info = (VersionInfo) msg.obj;
				if (info == null) {
					return;
				}
				boolean isUpdate= SharedPreUtils.getInstance(LogoActivity.this).getIntValue(Constants.IS_UPDATED)==1?true:false;
				try {
					ToolUtils.copyFile(LogoActivity.this, isUpdate);
		//			ToolUtils.copylib(LogoActivity.this, isUpdate);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(1); // 给UI主线程发送消息
					}
				}, 2000); // 启动等待3秒钟
				break;
			case HttpRequestManager.MSG_UPDATE_VERSION_FAIL:

				try {
					ToolUtils.copyFile(LogoActivity.this, false);
		//			ToolUtils.copylib(LogoActivity.this, false);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(1); // 给UI主线程发送消息
					}
				}, 2000); // 启动等待3秒钟
				break;
			}
		};
	};
/*
	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		
		//Log.e("dy", "logo getImageFromAssetsFile ");    //开机 2
		
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}

	*/	
	
}

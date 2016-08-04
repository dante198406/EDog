package com.hdsc.edog;

import com.hdsc.edog.utils.ToolUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{
	
	private final String ACTION = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("dy","BootReceiver..."+String.valueOf(ToolUtils.isBootStart(context)));
	//	if(intent.getAction() == ACTION && ToolUtils.isBootStart(context)){
			
			
			if (intent.getAction().equals(ACTION) && ToolUtils.isBootStart(context)) {
				
				waitSec(14000);//   改15秒  睡眠10秒，等待其它服务启动
				
			
			// ???? }
				TuzhiService.isSystemBoot = true;
				
			
				
					
			Intent serviceIntent = new Intent(context, LogoActivity.class);
			serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(serviceIntent);
			Log.e("dy","BootReceiver...");
			
			}
			
	}
	
	private void waitSec(long time){
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

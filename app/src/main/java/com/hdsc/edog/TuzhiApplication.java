package com.hdsc.edog;

import java.util.ArrayList;
import java.util.List;

import com.hdsc.edog.jni.DataPlay;
import com.hdsc.edog.jni.DataShow;
import com.hdsc.edog.jni.StorageDevice;
import com.hdsc.edog.net.HttpRequestManager;

import com.hdsc.edog.utils.AppDefaultConfig;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;
import com.hdsc.edog.utils.ToolUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;


public class TuzhiApplication extends Application {
	
//	public static final String POWER_ON_RADAR_ACTION = "landsem.intent.action.RADAR_POWER_ON";
//	public static final String POWER_OFF_RADAR_ACTION = "landsem.intent.action.RADAR_POWER_OFF";

	

	@Override
	public void onCreate() {
		StorageDevice.context= this;
//		CrashHandler crash = CrashHandler.getInstance();
//		crash.init(this);
		super.onCreate();
		
	/*	
		Log.e("dd", "send power on ");
		Intent intent = new Intent();
		intent.setAction(POWER_ON_RADAR_ACTION);
		sendBroadcast(intent);
	*/	
	}
	
	@Override
	public void onTerminate() {
		// 程序在内存清理的时候执行
		super.onTerminate();
	/*	
		Log.e("dd", "send power off ");
		Intent intent = new Intent();
		intent.setAction(POWER_OFF_RADAR_ACTION);
		sendBroadcast(intent);
		*/
	}
	
	
	
	 //运用list来保存们每一个activity是关键   
    static List<Activity> mList = new ArrayList<Activity>();   
       
    public static void addActivity(Activity activity) {    
        mList.add(activity);    
    }    
    //关闭每一个list内的activity   
    public static void exit() {    
        try {    
            for (Activity activity:mList) {    
                if (activity != null){    
                    activity.finish();
                }
            }    
            mList.clear();
        } catch (Exception e) {    
            e.printStackTrace();    
        } finally {    
//            System.exit(0);    
        }    
    }    
    //杀进程   
    public void onLowMemory() {    
        super.onLowMemory();        
        System.gc();    
    }  
    
    public static boolean isCurrent(Context context) {
		String currentPackageName = context.getPackageName();
		List<RunningTaskInfo> runningTaskInfos = ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE))
				.getRunningTasks(1);

		if (runningTaskInfos != null) {
			ComponentName cn = runningTaskInfos.get(0).topActivity;
			if (currentPackageName.trim().equalsIgnoreCase(
					cn.getPackageName().trim())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

    public static void operateFloatView(Context context, String action){
		Intent intent = new Intent();
		intent.setAction(action); 
		context.sendBroadcast(intent);// 发送广播
	}
}

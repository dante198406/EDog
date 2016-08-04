package com.hdsc.edog.jni;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

public class StorageDevice 
{
	public String mPath;
	public String mState;
	public boolean mMoveable;
	public static Context context;
	public static StorageDevice[] getDevices()
	{		
		StorageManager mStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
		
		Method getVolumeList = null;
		try {
			getVolumeList = StorageManager.class.getDeclaredMethod("getVolumeList");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		Object[] storageVolume = null;
		Method isRemovable = null;
		Method getPath = null;
		Method getState = null;
		//for(int i=0;i<storageVolume.length;i++)
		try {
			storageVolume = (Object[]) getVolumeList.invoke(mStorageManager);
			if(storageVolume != null)
			{
				StorageDevice devices[] = new StorageDevice[storageVolume.length];
				try 
				{
					isRemovable = storageVolume[0].getClass().getDeclaredMethod("isRemovable");
					getPath = storageVolume[0].getClass().getDeclaredMethod("getPath");
					getState = storageVolume[0].getClass().getDeclaredMethod("getState");
				}
				catch (NoSuchMethodException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				
				for(int i=0;i<devices.length;i++)
				{
					devices[i] = new StorageDevice();
					devices[i].mMoveable = (Boolean) isRemovable.invoke(storageVolume[i]);
					devices[i].mPath = (String) getPath.invoke(storageVolume[i]);
					devices[i].mState = (String) getState.invoke(storageVolume[i]);					
				}	
				return devices;
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private static String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
		.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}
		else
		{
			return null;
		}
		return sdDir.toString();
	} 
	
	
	//
	public static String getExternaSDPath()
	{
		StorageDevice Candidate = null;
		StorageDevice[] devices = getDevices();
		if(devices!=null)
		{
			for(int i=0;i<devices.length;i++)
			{
				if(devices[i].mMoveable)
				{
					if(devices[i].mState.contains("mounted"))
					{
						Log.i("liyichang ", "find sd");
						Candidate = devices[i];
						break;
					}
					else
					{
						break;
					}
				}
			}
		}
		
		Candidate = null;//add by liyichang 有些机器不允许写处部SD卡，只能写内部卡
		if(Candidate != null)
		{
			return  Candidate.mPath;
		}
		else
		{
			Log.i("liyichang ", "find def sd");
			return getSDPath();
		}
		
		
	}

}

package com.hdsc.edog;
//package tuzhi.edog.jni;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.hdsc.edog.jni.DataShow;
import com.hdsc.edog.R;

public class TestActivity extends Activity implements OnClickListener
{
	private final String TAG = "TestActivity";
	private int edoghandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "oncreate");
		setContentView(R.layout.activity_test);
		findViewById(R.id.button_testinit).setOnClickListener(this);
		findViewById(R.id.button_testuinit).setOnClickListener(this);
		findViewById(R.id.button_sd).setOnClickListener(this);
		
		
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = 0;//metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        Log.v(TAG, "width:"+width+"; height:"+height+"; density:"+density+"; densityDpi:"+densityDpi);
        


	}
	
	public static String getExternalStorageDirectory() {  
	    Map<String, String> map = System.getenv();  
	    String[] values = new String[map.values().size()];  
	    map.values().toArray(values);  
	    String path = values[values.length - 1];    //外置SD卡的路径  
	    if (path.startsWith("/mnt/") && !Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)){  
	        return path;  
	    }else{  
	        return null;  
	    }  
	    
	    
	    
	    /*ServiceManager.getService("mount"));
        final StorageVolume[] volumes = mountService.getVolumeList();
        for (StorageVolume volume : volumes) {
            if (rawPath.startsWith(volume.getPath())) {
                return mountService.getVolumeState(volume.getPath());
            }
        }*/
	}  
	void test()
	{
		//IActivitManageram = ActivityManagerNative.getDefault();
		//Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class); forceStopPackage.setAccessible(true);
		//forceStopPackage.invoke(am, yourpkgname);  
	}
	protected boolean checkSDCardMount(String mountPoint) {
        if(mountPoint == null){
            return false;
        }
        String state = null;
        state = "";//mStorageManager.getVolumeState(mountPoint);
        return Environment.MEDIA_MOUNTED.equals(state);
    }
	
	public String get()
	{
		
		StorageManager mStorageManager = (StorageManager)getSystemService(Context.STORAGE_SERVICE);
		
		Method method = null;
		try {
			method = StorageManager.class.getDeclaredMethod("getVolumePaths");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		String[] storagePathList = null;
		try {
			storagePathList = (String[]) method.invoke(mStorageManager);
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
		String mSDCardPath="";
		String mSDCard2Path="";
		boolean mSDCardMounted;
		boolean mSDCard2Mounted;
		
        if (storagePathList != null) { 
            Log.d(TAG, "StorgaeList size: " + storagePathList.length); 
            if (storagePathList.length >= 2) {
                mSDCardPath = storagePathList[0];
                mSDCard2Path = storagePathList[1];
            } else if (storagePathList.length == 1){
                mSDCardPath = storagePathList[0];
            }
        } 
        //Environment.getStorageState();
        Log.d(TAG, "SDCard path: " + mSDCardPath); //取出来的/mnt/sdcard
        Log.d(TAG, "SDCard2 path: " + mSDCard2Path);//取出来的应该是/mnt/sdcard
        return "";
        /*mSDCardMounted = checkSDCardMount(mSDCardPath);
        mSDCard2Mounted = checkSDCardMount(mSDCard2Path); 
        Log.d(TAG, "SDCard state in onCreate: " + mSDCardMounted); 
        Log.d(TAG, "SDCard2 state in onCreate: " + mSDCard2Mounted); */
	}
	
	public String get2()
	{
		String path = null;
		   try
		   {
				path = "";//android.os.SystemProperties.get("external_sd_path");	            
		   }
		   catch (IllegalArgumentException e) 
			{
			}
			if(path.equals("/storage/sdcard0"))
			{
	            //LogUtils.d(TAG, "current card id=  有sd卡" + path);
			}else
			{
	            //LogUtils.d(TAG, "current card id=  没有sd卡" + path);
			}
			return "";
	}
	
	public String get3()
	{
		
		//StorageVolume v = null;
		StorageManager mStorageManager = (StorageManager)getSystemService(Context.STORAGE_SERVICE);
		
		Method method = null;
		try {
			method = StorageManager.class.getDeclaredMethod("getVolumeList");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Object[] storageVolume = null;
		//for(int i=0;i<storageVolume.length;i++)
		try {
			storageVolume = (Object[]) method.invoke(mStorageManager);
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
		String mSDCardPath="";
		String mSDCard2Path="";
		String mSDCard3Path="";
		boolean ismoveable;
		boolean ismoveable2;
		boolean ismoveable3;
		
		Method method2 = null;
		Method method3 = null;
		Method method4 = null;
		try 
		{
			method2 = storageVolume[0].getClass().getDeclaredMethod("isRemovable");
			method3 = storageVolume[0].getClass().getDeclaredMethod("getPath");
			method4 = storageVolume[0].getClass().getDeclaredMethod("getState");
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
        if (storageVolume != null) { 
            Log.d(TAG, "StorgaeList size: " + storageVolume.length); 
            if (storageVolume.length >= 2) {
                try {
					ismoveable = (Boolean) method2.invoke(storageVolume[0]) ;
					ismoveable2 = (Boolean) method2.invoke(storageVolume[1]) ;
					ismoveable3 = (Boolean) method2.invoke(storageVolume[2]) ;
					mSDCardPath = (String) method3.invoke(storageVolume[0]);
					mSDCard2Path = (String) method3.invoke(storageVolume[1]);
					mSDCard3Path = (String) method3.invoke(storageVolume[2]);
					int i=0;
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               
                //boolean ismoveable2 = (boolean) method2.invoke(storageVolume[2]) ;
            }
            else if (storageVolume.length == 1)
            {
            	try {
					boolean ismoveable4 = (Boolean) method2.invoke(storageVolume[0]) ;
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
            }
        } 
        //Environment.getStorageState();
        Log.d(TAG, "SDCard path: " + mSDCardPath); //取出来的/mnt/sdcard
        Log.d(TAG, "SDCard2 path: " + mSDCard2Path);//取出来的应该是/mnt/sdcard
        Log.d(TAG, "SDCard3 path: " + mSDCard3Path);//取出来的应该是/mnt/sdcard
        return "";
	}
	
	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		switch (arg0.getId()) 
		{
			case R.id.button_testinit:
//				Log.v(TAG, "in button_testinit");
//				int objid = RadarDataManager.initEdog();//初始化
//				Log.v(TAG, "out button_testinit");
//				
//				
//				//
//				RadarDataManager.setTestWarning(true);//设置报警
//				Log.v(TAG, "out setTestWarning");
//				boolean bret = RadarDataManager.getTestWarning();
//				Log.i("getTestWarning", "ret:"+bret);
//				
//				Log.v(TAG, "in handlerGpsInfox");
//				GpsInfo gpsinfo = new GpsInfo();
//				gpsinfo.altitude = 50.12f;
//				gpsinfo.bearing = 50.12f;
//				gpsinfo.lat = 50.12f;
//				gpsinfo.lng = 50.12f;
//				gpsinfo.speed = 50.12f;
//				RadarDataManager.handlerGpsInfox(null);//输入gps信息
//				RadarDataManager.handlerGpsInfox(gpsinfo);//输入gps信息
//
//				
//				Log.v(TAG, "in handlerGpsInfox2");
//				GpsInfo gpsinfo2 = new GpsInfo();
//				gpsinfo2.altitude = 50.12f;
//				gpsinfo2.bearing = 50.12f;
//				gpsinfo2.lat = 50.12f;
//				gpsinfo2.lng = 50.12f;
//				gpsinfo2.speed = 50.12f;
//				RadarDataManager.handlerGpsInfox(null);//输入gps信息
//				RadarDataManager.handlerGpsInfox(gpsinfo2);//输入gps信息
//				
//			
//				//edogCppEngine.handlerGpsInfox(gpsinfo);//输入gps信息
//				Log.v(TAG, "out handlerGpsInfox--1");
//				//edogCppEngine.handlerGpsInfox(gpsinfo);//输入gps信息
//				//edogCppEngine.handlerGpsInfo(1,gpsinfo.m_Altitude,
//				///		gpsinfo.m_Bearing,gpsinfo.m_Lat,
//				//		gpsinfo.m_Lng,gpsinfo.m_Speed);
//				Log.v(TAG, "out handlerGpsInfox");
//				
//			/*	eEyeInfo ptInfo = new eEyeInfo();
//				ptInfo.m_dir = 55.999f;
//				ptInfo.m_dirType = 3;
//				ptInfo.m_Lat = 55.999f;
//				ptInfo.m_Lng = 55.999f;
//				ptInfo.m_ptType="CD";
//				ptInfo.m_siteType=7;
//				ptInfo.m_Speed=55.999f;
//				edogCppEngine.addEEyeInfo(ptInfo);//添加电子眼
//				Log.v(TAG, "out addEEyeInfo1");
//				edogCppEngine.addEEyeInfo(null);
//				Log.v(TAG, "out addEEyeInfo1 null");
//				
//				eEyeInfo ptInfo2 = new eEyeInfo();
//				ptInfo2.m_dir = 55.999f;
//				ptInfo2.m_dirType = 3;
//				ptInfo2.m_Lat = 55.999f;
//				ptInfo2.m_Lng = 55.999f;
//				ptInfo2.m_ptType="CD";
//				ptInfo2.m_siteType=7;
//				ptInfo2.m_Speed=55.999f;
//				edogCppEngine.addEEyeInfo(ptInfo2);//添加电子眼
//				Log.v(TAG, "out addEEyeInfo12");
//	
//				*/
//
//				RadarDataManager.setTestWarning(false);
//				bret = RadarDataManager.getTestWarning();
//				Log.i("getTestWarning", "ret:"+bret);
//				
//				byte radData[] ={(byte) 0xaa,0x30} ;//雷达协议 音量加,有两个码的，也有6个码的
//				RadarDataManager.OnRadData(radData, 2);
//				Log.i(TAG,"out OnRadData");
//				RadarDataManager.OnRadData(radData, 2);
//				Log.i(TAG,"out OnRadData 2");
//				RadarDataManager.SetSpeedMax(120);
//				Log.v(TAG,"out GetSpeedMax"+":"+RadarDataManager.GetSpeedMax());
//				RadarDataManager.SetSpeedMax(130);
//				Log.v(TAG,"out GetSpeedMax"+":"+RadarDataManager.GetSpeedMax());
//				Log.v("liyichang", "is ok");
//				
//				RadarDataManager.EnableEditPt();
//		        
//				RadarDataManager.EnableAddPt();
//				break;
//				//*****/
//			case R.id.button_testuinit:
//				break;
//			case R.id.button_sd:
//			{
//			    /*Dev_MountInfo dev = Dev_MountInfo.getInstance();  
//			    DevInfo info = dev.getInternalInfo();//Internal SD Card Informations  
//			    info = dev.getExternalInfo();//External SD Card Informations  
//			      
//			    //   Methods:  
//			    info.getLabel(); // SD 卡的名称  
//			    info.getMount_point();//SD 卡挂载点  
//			    info.getPath(); //SD 卡路径  
//			    info.getSysfs_path(); // ....没弄清楚什么意思  
//			    EditText lab = (EditText) findViewById(R.id.button_sd);
//			    lab.setText("Lab:"+info.getLabel()+";pt:"+info.getMount_point()+";path:"+info.getPath()+";spath:"+info.getSysfs_path());
//			    Log.v("liyichang", info.getLabel());
//			    Log.v("liyichang", info.getMount_point());
//			    Log.v("liyichang", info.getPath());
//			    Log.v("liyichang", info.getSysfs_path());*/
//				
//				//String sdpath = getExternalStorageDirectory();
//				//Log.v("liyichang", sdpath);
//				
//				//Environment.get
//				//File sdDir = Environment.getExternalStorageDirectory();
//				//Log.i("liyichang", sdDir.getPath());
//				
//				StorageDevice.context = this.getApplicationContext();
//				StorageDevice[] devices = StorageDevice.getDevices();
//				for(int i=0;i<devices.length;i++)
//				{
//					Log.i("liyichang", devices[i].mPath);
//					Log.i("liyichang", devices[i].mState);
//					Log.i("liyichang", "end:"+devices[i].mMoveable);
//					Log.i("liyichang", "end2"+devices[i].mMoveable);
//				}
//				
//				Log.i("liyichang sd:", StorageDevice.getExternaSDPath());
//				EditText lab = (EditText) findViewById(R.id.editText1);
//				lab.setText(StorageDevice.getExternaSDPath());
//			    
//			}
//				break;
//				
//			default:break;
		}
		
	}
}

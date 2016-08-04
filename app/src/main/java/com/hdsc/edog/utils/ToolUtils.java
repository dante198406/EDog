package com.hdsc.edog.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hdsc.edog.MainActivity;
import com.hdsc.edog.TuzhiService;
//import com.hdsc.edog.entity.TConstant;
import com.hdsc.edog.jni.EdogDataManager;
import com.hdsc.edog.jni.RadarDataManager;
import com.hdsc.edog.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class ToolUtils {

	private static ToolUtils instance;

	private ToolUtils() {
	}

	public static ToolUtils getInstance() {
		if (instance == null)
			instance = new ToolUtils();
		return instance;
	}

	/**
	 * GPS没有打开弹框提示
	 * 
	 * @param context
	 */
	public Dialog showGpsDialog(LocationManager locationManager,
			final Context context) {
		if (!getGpsStatus(locationManager)) {
			AlertDialog.Builder builer = new AlertDialog.Builder(context);
			builer.setTitle(R.string.dialog_gps_title);
			builer.setMessage(R.string.dialog_gps_message);
			builer.setPositiveButton(R.string.dialog_btn_open,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							openGPS(context);
							dialog.cancel();
						}
					});
			builer.setNegativeButton(R.string.dialog_btn_skip,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			Dialog d = builer.create();
			d.show();
			return d;
		}
		return null;
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public boolean getGpsStatus(LocationManager locationManager) {
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		return gps;
	}

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public void openGPS(Context context) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivity(intent);
	}

	/**
	 * 后台运行消息栏通知
	 * 
	 * @param context
	 */
	public void showRunBgNotify(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);

		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification();
		notif.icon = R.drawable.ic_launcher;
		//notif.flags = Notification.FLAG_AUTO_CANCEL;
		notif.tickerText = context.getResources().getString(
				R.string.notify_alert);
		notif.contentView = new RemoteViews(context.getPackageName(),
				R.layout.notif_exit_view);
		notif.when=System.currentTimeMillis();
		notif.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_SHOW_LIGHTS;
		//notif.contentIntent = pIntent;
		notif.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(R.string.notify_alert), pIntent);
		manager.notify(1, notif);
	}

	public void exitNotify(Context context) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (manager != null) {
			manager.cancel(1);
		}
	}

	/**
	 * 弹出网络设置窗口
	 * 
	 * @param context
	 */

	Dialog dialog = null;

	public void showSetNetDialog(final Context context) {
		dialog = new AlertDialog.Builder(context)
				.setTitle(R.string.net_setting)
				.setMessage(R.string.net_prompt)
				.setPositiveButton(R.string.net_setting, new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						forwardToWifiAPPage(context);
						dialog.cancel();
					}

				})
				.setNegativeButton(R.string.dialog_cancel,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								dialog.cancel();
							}

						}).create();

		dialog.show();
	}
	

	public void dismissSetNetDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	/**
	 * 跳转到网络设置的界面
	 * 
	 * @param context
	 */
	public void forwardToWifiAPPage(Context context) {
		if (android.os.Build.VERSION.SDK_INT < 10) {
			// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
			context.startActivity(new Intent(
					android.provider.Settings.ACTION_SETTINGS));
		} else {
			context.startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 检测是否更新 版本号格式:X.X.X
	 * 
	 * @param curVersion
	 *            :当前版本号
	 * @param netVersion
	 *            :服务器版本号
	 * @return
	 */
	public boolean isUpdate(Context context, String netVersion) {
		String curVersion = getVersionName(context);
		if (TextUtils.isEmpty(curVersion) || TextUtils.isEmpty(netVersion)) {
			return false;
		}
		int curVer = Integer.parseInt(curVersion.replace(".", "")); // 100 101
		int netVer = Integer.parseInt(netVersion.replace(".", ""));

		Log.e("dy", "curVer = " + String.valueOf(curVer));
		Log.e("dy", "netVer = " + String.valueOf(netVer));

		if (curVer < netVer) {
			return true;
		}
		return false;
	}

	// 字节数组转化为16进制字符串
	public static String bytesToHexString(byte[] src , int srclength) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < srclength ; i++) {   //src.length
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	// 获取仪表盘速度值
	public static String[] getPanelSpeed(float speed) {
		String strSpeed = String.valueOf((int) speed);

		return new String[] { strSpeed.substring(0, 1),
				strSpeed.substring(1, 2), strSpeed.substring(2, 3) };
	}

	
	/*  JT_V1.2.3 
	public static void copyLogo(Context context)
			throws Throwable {
		String filePath = context.getFilesDir().getAbsolutePath() + "/"
				+ "logo.png";
		EdogDataManager.logoPath = filePath;
		File file = new File(filePath);
		InputStream inStream = context.getAssets().open("logo.png");
		OutputStream outStream = new FileOutputStream(filePath);
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, length);
		}
		outStream.flush();
		inStream.close();
		outStream.close();
		Log.e("dy", "logo size=" + String.valueOf(file.length()));

		return;
	}

	*/
	
	public static void copyFile(Context context, boolean isUpdate)
			throws Throwable {
		String filePath = context.getFilesDir().getAbsolutePath() + "/"
				+ "map03apk.bin";
		EdogDataManager.eDogFilePath = filePath;
		File file = new File(filePath);
		if (!file.exists() || isUpdate) {
			InputStream inStream = context.getAssets().open("map03apk.bin");
			OutputStream outStream = new FileOutputStream(filePath);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			outStream.flush();
			inStream.close();
			outStream.close();
		//	Log.e("dy", "copyFile size=" + String.valueOf(file.length()));

			return;
		}
		//Log.e("dy", "file exist!");
	}

/*	
	
	public static void copylib(Context context, boolean isUpdate)
			throws Throwable {

		String filePath = context.getFilesDir() + "/" + "edog.so";
		EdogDataManager.eDoglibPath = filePath;
		File file = new File(filePath);
		if (!file.exists() || isUpdate) {
			InputStream inStream = context.getAssets().open("edog.so");
			OutputStream outStream = new FileOutputStream(filePath);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			outStream.flush();
			inStream.close();
			outStream.close();
			Log.e("dy", "file size=" + String.valueOf(file.length()));
			return;
		}
		// Log.e("dy","file exist!");

	}

*/
	
	public static void sleep(int time) {
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	/**
	public static boolean isBootStart(Context context) {
		return SharedPreUtils.getInstance(context).getIntValue(
				TConstant.BOOT_START) == 0 ? true : false;

		// public static boolean isBootStart(Context context){
		// return
		// SharedPreUtils.getInstance(context).getIntValue(TConstant.BOOT_START)==0?true:false;
	}
	
	
	
	 * 判断app是否是开机就进入后台运行
	 * @param context
	 * @return 
	 */
	public static boolean isBootStart(Context context) {
		
		return SharedPreUtils.getInstance(context).getIntValue(
				Constants.BOOT_START) == 1 ? true : false;
	}
	
	
	public static String[] getFiles(String path) {  
		File[] allFiles = new File(path).listFiles(); 
		List<String> fileNameList = new ArrayList<String>();
		for (int i = 0; i < allFiles.length; i++) {
			File file = allFiles[i];  
			if (file.getName().contains("tty")) {  
				fileNameList.add(file.getName());
	        } 
		}
		if(fileNameList.size()==0)
			return null;
		
		String filesName[] = new String[fileNameList.size()];
	    for (int i = 0; i < fileNameList.size(); i++) {  
	        filesName[i] = fileNameList.get(i); 
	    }  
	    return filesName;
	}

}

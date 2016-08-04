package com.hdsc.edog.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hdsc.edog.LogoActivity;
import com.hdsc.edog.TuzhiService;
import com.hdsc.edog.R;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadUtils {

	public static final int DOWNLOAD_ERR = -1;
	public static final int CODE_DOWNLOADING = 0X01;
	public static final String filePath = Environment.getExternalStorageDirectory()+"/tuzhi.edog.androidapp/apk";
	private  Notification notif;
	private NotificationManager manager;
	private static DownloadUtils instance;

	private DownloadUtils() {
	}

	public static DownloadUtils getInstance() {
		if (instance == null)
			instance = new DownloadUtils();
		return instance;
	}

	/**
	 * 显示更新下载的dialog
	 * @param context
	 * @param url 下载的URL
	 * @param serverVersion 服务器返回的最新版本号
	 */
	public void showUpdateDialog(final Context context, final String url,
			final String serverVersion) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(R.string.dialog_update_title);
		dialog.setMessage(String.format(
				context.getResources().getString(R.string.new_version_update),
				serverVersion));
		dialog.setPositiveButton(R.string.now_update,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloadApk(context,getApplicationName(context), url);
						SharedPreUtils.getInstance(context).commitIntValue(Constants.IS_UPDATED, 1);
						dialog.dismiss();
					}
				});
		dialog.setNegativeButton(R.string.now_no_update,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dialog.create().show();
	}
	
	/**
	 * 获取APP的应用名称
	 * @param context
	 * @return
	 */
	private String getApplicationName(Context context) { 
		PackageManager packageManager = null; 
		ApplicationInfo applicationInfo = null; 
		try 
		{ 
			packageManager = context.getApplicationContext().getPackageManager(); 
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0); 
		 } catch (PackageManager.NameNotFoundException e) { 
			applicationInfo = null; 
		} 
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo); 
		return applicationName; 
	} 
	
	/**
	 * 下载apk，通知栏显示进度
	 * @param context
	 * @param name
	 * @param url
	 */
	private void downloadApk(final Context context, final String name, String url){
		showNotify(context, name+".apk");
		handleDownFile(url, name+".apk", new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case CODE_DOWNLOADING:
					
					int progress = msg.arg1;
					notif.contentView.setTextViewText(R.id.content_view_text1, progress+"% " + name); 
					notif.contentView.setProgressBar(R.id.content_view_progress, 100, progress, false); 
					manager.notify(0, notif); 
					if(progress == 100){
						setupAPK(context,name+".apk");
					}
					
					break;
				case DOWNLOAD_ERR:
					Toast.makeText(context, context.getResources().getString(R.string.utils_download_fail_try_later), Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				 
			}
		});

	}
	
	/**
	 * 通知栏显示
	 * @param context
	 * @param apkName
	 */
	private void showNotify(Context context, String apkName){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath+"/"+apkName.replace("/", ""))),
				"application/vnd.android.package-archive");
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0); 
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
        notif = new Notification(); 
        notif.icon = R.drawable.ic_launcher; 
        notif.tickerText = context.getResources().getString(R.string.download_notify); 
        notif.contentView = new RemoteViews(context.getPackageName(), R.layout.notif_context_view); 
        notif.contentIntent = pIntent; 
        manager.notify(0, notif); 
	}
	
	/**
	 * 自动安装apk
	 * @param context
	 * @param apkName
	 */
	private void setupAPK(Context context, String apkName) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath+"/"+apkName.replace("/", ""))),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 下载线程
	 * @param url
	 * @param apkName
	 * @param handler
	 */
	private void handleDownFile(final String url, final String apkName, final Handler handler) {
		new Thread() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse repResponse;
				try {
					repResponse = client.execute(get);
					HttpEntity entity = repResponse.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					File file = new File(filePath, apkName.replace("/", ""));
					if(!file.exists()){
						File rootFile = new File(filePath);
						if(!rootFile.exists()){
							rootFile.mkdirs();
						}
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					FileOutputStream fos = null;
					if (fos == null) {
						fos = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						int temp = 0;
						while ((ch = is.read(buf)) != -1) {
							fos.write(buf, 0, ch);
							count += ch;
							temp += ch;
							TuzhiService.GPRSTOTAL += ch;
							if(length != 0){
								int progress = (int)(count * 100 / length);
								int progressTemp = (int)(temp * 100 / length);
								if(progressTemp > 5 || progress == 100){
									Message msg = new Message();
									msg.what = CODE_DOWNLOADING;
									msg.arg1 = progress;
									handler.sendMessage(msg);
									temp = 0;
								}
							}
						}
						fos.flush();
						if (fos != null) {
							fos.close();
						}
					}
				} catch (MalformedURLException e) {
					handler.sendEmptyMessage(DOWNLOAD_ERR);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(DOWNLOAD_ERR);
					e.printStackTrace();
				}

			}

		}.start();
	}
}

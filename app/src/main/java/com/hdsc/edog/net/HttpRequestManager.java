package com.hdsc.edog.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.hdsc.edog.TuzhiService;
import com.hdsc.edog.entity.HttpResult;
import com.hdsc.edog.entity.LogTag;
import com.hdsc.edog.entity.VersionInfo;
import com.hdsc.edog.utils.ToolUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.sax.EndTextElementListener;
import android.sax.RootElement;

public class HttpRequestManager {
	public final static int TIME_OUT = 0x000;
	public final static int MSG_UPDATE_VERSION_SUCC = 0x001;
	public final static int MSG_UPDATE_VERSION_FAIL = 0x002;
	
	//访问的IP地址
	private String mstrIP = "http://www.dzgsj.com/note/apk/apkver.xml";
	
	private final static int HTTP_TIMEOUT = 10000;//http连接超时时间
	
	private ExecutorService executorService = null;
	private HttpRequestManager() {
		executorService = Executors.newFixedThreadPool(5);
	}

	private static HttpRequestManager instance;

	public static HttpRequestManager getInstance() {
		if (instance == null)
			instance = new HttpRequestManager();
		return instance;
	}
	
	public void updateVersion(Context context, final Handler handler){
		updateVersionParams(context, new HttpReqCallback() {
			
			@Override
			public void httpResult(HttpResult hr) {
				if(hr.mnRet == Protocol_base.RET_NETWORK_SUCC){
					VersionInfo info = paserUpdateVersion(hr.mstrRet);
					Message msg = new Message();
					msg.what = MSG_UPDATE_VERSION_SUCC; 
					msg.obj = info;
					handler.sendMessage(msg);
				}else{
					handler.sendEmptyMessage(MSG_UPDATE_VERSION_FAIL);
				}
			}
		});
	}
	
	
	private void updateVersionParams(Context context, final HttpReqCallback hrc){
		if(checkNetwork(context)){
			if(executorService == null){
				LogTag.e("HttpRequestManager:", "executorService == null");
				return;
			}
			Future<?> future = executorService.submit(new Runnable() {
				
				@Override
				public void run() {
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					String timestampStr = String.valueOf(System.currentTimeMillis());
//					params.add(new BasicNameValuePair("timestamp",timestampStr));
//					RequestHttpPost(mstrIP, params, hrc);
					RequestHttpGet(mstrIP, hrc);
					
//					HttpResult httpRet = new HttpResult();
//					httpRet.mnRet = Protocol_base.RET_NETWORK_SUCC;
//					httpRet.mstrRet = "<root><versionNo>1.0.2</versionNo><downloadUrl>http://testfileservice.365car.com.cn:88/fileService/osp/image/40cbeee8459acef9f6532534563359fc.png</downloadUrl></root>";
//					hrc.httpResult(httpRet);

				}
			});
			try {
				if (future.get() == null) {// 如果Future's get返回null，任务完成
					LogTag.d("HttpRequestManager", "executorService finish!!!!!");
				}
			}catch (Exception e) {
				// 否则我们可以看看任务失败的原因是什么
				LogTag.e("HttpRequestManager",e.getCause().getMessage());
			}
		}else{
			ToolUtils.getInstance().showSetNetDialog(context);
		}
	}
	
	private VersionInfo paserUpdateVersion(String strData)
	{
		if (strData == null){
			return null;
		}
		final VersionInfo obj = new VersionInfo();
		InputStream inputStream = new ByteArrayInputStream(strData.getBytes());
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		XMLReader reader = null;
		try
		{
			parser = factory.newSAXParser();
			reader = parser.getXMLReader();
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch (SAXException e)
		{
			e.printStackTrace();
		}

		RootElement rootElement = new RootElement("root");
		android.sax.Element versionNo = rootElement.getChild("versionNo");
		versionNo.setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String vip)
			{
				obj.versionNo = vip;
			}

		});
		android.sax.Element urlElement = rootElement.getChild("downloadUrl");
		urlElement.setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String vip)
			{
				obj.downloadUrl = vip.trim();
			}

		});

		if (reader != null)
		{
			reader.setContentHandler(rootElement.getContentHandler());

			try
			{
				reader.parse(new InputSource(inputStream));
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (SAXException e)
			{
				e.printStackTrace();
			}
		}
		return obj;
		
	}
	
	public void shutDownThreadPool(){
		if(executorService != null){
			executorService.shutdownNow();
			executorService = null;
			instance = null;
		}
	}
	
	public boolean checkNetwork(Context context)
	{
		ConnectivityManager netManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = netManager.getActiveNetworkInfo();
		if (info != null) return info.isAvailable();
		else return false;
	}
	
	private void RequestHttpGet(String uriAPI, HttpReqCallback hrc)
	{
		long currentTime = System.currentTimeMillis();
		URL url = null;
		HttpURLConnection httpConn = null;
		HttpResult httpRet = new HttpResult();
		try
		{
			url = new URL(uriAPI);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setConnectTimeout(HTTP_TIMEOUT);
			httpConn.setReadTimeout(HTTP_TIMEOUT);
			httpConn.setRequestMethod("GET");
//			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//			httpConn.setRequestProperty("Connection", "Keep-Alive");
//			httpConn.setRequestProperty("Accept", "*/*");
//			httpConn.setRequestProperty("Accept-Language", "zh-cn");
//			httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//			httpConn.setRequestProperty("Accept-Charset", "utf-8");

			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode)
			{

				
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null)
				{
					sb.append(readLine).append("\n");
					TuzhiService.GPRSTOTAL += readLine.getBytes().length;
				}
				responseReader.close();
				httpRet.mstrRet = sb.toString();
				httpRet.mnRet = Protocol_base.RET_NETWORK_SUCC;
				
				//记录协议访问时间
				LogTag.netTimeLog(uriAPI, System.currentTimeMillis() - currentTime);
			}
			else if ((responseCode == HttpStatus.SC_GATEWAY_TIMEOUT) || (responseCode == HttpStatus.SC_REQUEST_TIMEOUT))
			{
				httpRet.mnRet = Protocol_base.RET_NETWORK_TIMEOUT;
			}
			else
			{
				httpRet.mnRet = Protocol_base.RET_NETWORK_FAIL;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			httpRet.mnRet = Protocol_base.RET_NETWORK_FAIL;
		}finally{
			if(httpConn != null){
				httpConn.disconnect();
			}
			url = null;
		}
		
		hrc.httpResult(httpRet);
	}
	
	private void  RequestHttpPost(String uriAPI, Object obj, HttpReqCallback hrc)
	{
		long currentTime = System.currentTimeMillis();
		URL url = null;
		HttpURLConnection httpConn = null;
		HttpResult httpRet = new HttpResult();
		try
		{
			@SuppressWarnings("unchecked")
			List<NameValuePair> params = (List<NameValuePair>) obj;

			
			url = new URL(uriAPI);
			httpConn = (HttpURLConnection) url.openConnection();
			
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setConnectTimeout(HTTP_TIMEOUT);
			httpConn.setReadTimeout(HTTP_TIMEOUT);

			httpConn.setRequestMethod("POST");

			String requestString = URLEncodedUtils.format(params, "UTF-8");

			
			byte[] requestStringBytes = requestString.getBytes("UTF-8");
			httpConn.setRequestProperty("Content-Length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Accept-Language", "zh-cn");
			httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");

			
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			TuzhiService.GPRSTOTAL += requestStringBytes.length;
			
			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode)
			{

				
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null)
				{
					sb.append(readLine).append("\n");
					TuzhiService.GPRSTOTAL += readLine.getBytes().length;
				}
				responseReader.close();
				httpRet.mstrRet = sb.toString();
				httpRet.mnRet = Protocol_base.RET_NETWORK_SUCC;
				
				//记录协议访问时间
				LogTag.netTimeLog(uriAPI, System.currentTimeMillis() - currentTime);
			}
			else if ((responseCode == HttpStatus.SC_GATEWAY_TIMEOUT) || (responseCode == HttpStatus.SC_REQUEST_TIMEOUT))
			{
				httpRet.mnRet = Protocol_base.RET_NETWORK_TIMEOUT;
			}
			else
			{
				httpRet.mnRet = Protocol_base.RET_NETWORK_FAIL;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			httpRet.mnRet = Protocol_base.RET_NETWORK_FAIL;
		}finally{
			if(httpConn != null){
				httpConn.disconnect();
			}
			url = null;
		}
		
		hrc.httpResult(httpRet);
	}
}

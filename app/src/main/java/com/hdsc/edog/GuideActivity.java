package com.hdsc.edog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hdsc.edog.adapter.ViewPagerAdapter;
import com.hdsc.edog.utils.AppDefaultConfig;
import com.hdsc.edog.utils.Constants;
import com.hdsc.edog.utils.SharedPreUtils;


/** 
 * @author duyuan 
 *  功能描述：引导界面activity类 
 */  
public class GuideActivity extends Activity implements OnPageChangeListener{  
    // 定义ViewPager对象  
    private ViewPager viewPager;  
  
    // 定义ViewPager适配器  
    private ViewPagerAdapter vpAdapter;  
  
    // 定义一个ArrayList来存放View  
    private ArrayList<View> views;  
  
    // 定义各个界面View对象  
    private View view1, view2;  
      
    //定义开始按钮对象  
    private TextView startTv;  
          
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		
        setContentView(R.layout.guide);  
          
        initView();  
          
        initData();  
        
    	SharedPreUtils sp = SharedPreUtils.getInstance(this);
		// 判断当前app是否为公版，如果不是则设置默认的雷达串口号
		if (!AppDefaultConfig.isCommonVersion) {
			sp.commitIntValue(Constants.RADAR_COM_P, AppDefaultConfig.ttyNo);
			sp.commitStringValue(Constants.RADAR_COM, Constants.coms[AppDefaultConfig.ttyNo]);

			if (AppDefaultConfig.autoStarted) {
				sp.commitIntValue(Constants.BOOT_START, 1);
			} else {
				sp.commitIntValue(Constants.BOOT_START, 0);
			}
			Log.e("dy", "is not CommonVersion");
		}
        
    }  
  
    /** 
     * 初始化组件 
     */  
    private void initView() {  
        //实例化各个界面的布局对象   
        LayoutInflater mLi = LayoutInflater.from(this);  
        view1 = mLi.inflate(R.layout.guide_view01, null);  
        view2 = mLi.inflate(R.layout.guide_view02, null);  
      
        // 实例化ViewPager  
        viewPager = (ViewPager) findViewById(R.id.viewpager);  
    	viewPager.setOffscreenPageLimit(1);
        // 实例化ArrayList对象  
        views = new ArrayList<View>();  
  
        // 实例化ViewPager适配器  
        vpAdapter = new ViewPagerAdapter(views);  
          
        //实例化开始按钮  
        startTv = (TextView) view2.findViewById(R.id.tv_start);  
    }  
  
    /** 
     * 初始化数据 
     */  
    private void initData() {  
        // 设置监听  
        viewPager.setOnPageChangeListener(this);  
        //将要分页显示的View装入数组中        
        views.add(view1);  
        views.add(view2);
        // 设置适配器数据  
        viewPager.setAdapter(vpAdapter);  
  
        // 给开始按钮设置监听  
        startTv.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                 startbutton();  
            }  
        });  
    }  
  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
              
    }  
  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
          
    }  
  
    @Override  
    public void onPageSelected(int arg0) {  
          
    }  
      
    /** 
     * 相应按钮点击事件 
     */  
    private void startbutton() {    
        Intent intent = new Intent();  
        intent.setClass(GuideActivity.this,MainActivity.class);  
        startActivity(intent);  
        this.finish();  
      }  
} 
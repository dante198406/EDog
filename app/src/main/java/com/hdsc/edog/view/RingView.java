package com.hdsc.edog.view;


import com.hdsc.edog.MainActivity;
import com.hdsc.edog.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;



public class RingView extends View {

	private Paint mPaint;
	Bitmap bigPointerBmp;
	private Context context;
	public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public RingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	void init(Context context) {
		this.context = context;
	//V1.2.6 
		bigPointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ring_arrows);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
		
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		
		// 0 --- 150 200 --- 350 210 --- 0 220 --- 10 230 --- 20 240 --- 30
		
		canvas.save();
		
		float ringWidth = dip2px(context, 8); // 设置圆环宽度
		float standard = (36.5f+12.5f/2)/254f;
		float paddingX = MainActivity.xdpi * standard;
		float paddingY = MainActivity.ydpi * standard;
		if(MainActivity.width == 1800 && MainActivity.height == 1080){
			paddingX = MainActivity.xdpi * standard + 32;
			paddingY = MainActivity.ydpi * standard + 28;
		}
		float cx = getWidth()/4;
		float cy = getHeight()/2;
		float radiusX = cy -  paddingX; //圆环的半径  
		float radiusY = cy - paddingY;
		RectF rect2 = new RectF(cx - radiusX, cy - radiusY, cx + radiusX, cy + radiusY);
		
		mPaint.setStrokeWidth(ringWidth);
		// 绘制不透明部分

		
	/* V1.2.6	
		int limitSpeedTemp = limitSpeed + 150;
		if (limitSpeedTemp >= 360) {
			limitSpeedTemp = limitSpeedTemp - 360;
		}
		if (currentSpeed < limitSpeed) {

			// 当前速度时段
			mPaint.setARGB(255, 0, 255, 51);
			canvas.drawArc(rect2, 150, currentSpeed, false, mPaint);

			// 当前速度距离超时时段
//			mPaint.setARGB(255, 52, 0, 255);
//			canvas.drawArc(rect2, limitSpeedTemp, currentSpeed - limitSpeed,
//					false, mPaint);
			
			
			// 限速部分
			mPaint.setARGB(255, 252, 209, 12);
			canvas.drawArc(rect2, limitSpeedTemp, 240 - limitSpeed, false, mPaint);
		} else {
			//限速区域闪烁效果
//			if(i%2==0){
				// 限速部分
				mPaint.setARGB(255, 252, 209, 12);
				canvas.drawArc(rect2, currentSpeed+150, 240 - currentSpeed, false, mPaint);
//			}else{
//				// 限速部分
//				mPaint.setARGB(0, 0, 0, 0);
//				canvas.drawArc(rect2, limitSpeedTemp, 240 - limitSpeed, false, mPaint);
//			}
			
			// 当前速度时段
			if(i%2==0){
				mPaint.setARGB(255, 255, 0, 0);
				canvas.drawArc(rect2, 150, currentSpeed, false, mPaint);
			}else{
				// 限速部分
				mPaint.setARGB(0, 0, 0, 0);
				canvas.drawArc(rect2, 150, currentSpeed, false, mPaint);
			}
			
			
		}
		
		*/
		
 //画速度 指针
		
		
	//	Log.e("rd", "RESNO= AAA-currentSpeed"+String.valueOf(currentSpeed)); 
		
		mPaint.setARGB(255, 255, 0, 0);
		canvas.rotate(currentSpeed-120, cx, cy);
		float s = 15.5f/254f;
		float fx = MainActivity.xdpi * s;
		float fy = MainActivity.ydpi * s;
		if(MainActivity.width == 1800 && MainActivity.height == 1080){
			fx = MainActivity.xdpi * s + 32;
			fy = MainActivity.ydpi * s + 28;
		}
		float rx = cy -  fx;
		float ry = cy - fy;
		
		
		//V1.2.6	RectF rect3 = new RectF(cx - rx, cy - ry, cx + rx, cy + ry);
	RectF rect3 = new RectF(cx - rx, cy - ry+ 15, cx + rx, cy + ry);
		
		canvas.drawBitmap(bigPointerBmp, null, rect3, mPaint);
		
		canvas.restore();
		super.onDraw(canvas);

	}

//	private int limitSpeed = 80;
	private int currentSpeed = 0;
	
	//int i = 0;
	public void restoreRingView( /*int limitSpeed, */int currentSpeed){
	//	this.limitSpeed = limitSpeed;
		this.currentSpeed = currentSpeed;
		
	//	Log.e("rd", "RESNO=this.currentSpeed == "+String.valueOf(this.currentSpeed)); 
		
	//	i++;
		postInvalidate();
	}
	
	public static float px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (pxValue / scale + 0.5f);  
    }  

	 public static float dip2px(Context context, float dpValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (dpValue * scale + 0.5f);  
	    }  
}

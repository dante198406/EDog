package com.hdsc.edog.entity;

public class EdogData {
	private int mFindCamera; // 是否搜索到摄像点 1:目前已找到摄像点 0:未找�?,报警�
	private int mFirstFindCamera; // 第一次找到摄像点 1:第一次找�?2:100距离; 3:退出报�?4：退出报警时的距�
									// > 70M�?5：单点报�
	private int mSpeedLimite; // 摄像点的限速�? 0-120 130 140
	private int mUserCameraIndex; // 自建摄像点的索引�?0xFFFF表示为系统内置数�?;临时 +1000,000 。增�
									// +2000,000 ；内�?3000,000
	private int mDisToCamera; // 与摄像点的距�?以米为单位， --显示�
	private int mVoiceCode; // 要播放的语音ID,具体的语音ID代表意义见附�
	private int mTaCode; // 0~0xf = 0xff表示 不是原始数据 //要播放的方位-语音TA,具体的语音TA代表意义见附�?
	
	private int mBlockSpeed; // 区间平均速度
	private int mPercent ;  //区间完成%比 ;
	private int  mBlockSpace  ;
	private int mVersion;
	
	public EdogData(int bFindCamera, int bFirstFindCamera,
			int bCameraSpeedLimite, int wUserCameraIndex, int wDisToCamera,
			int wVoiceCode, int wTACode, int blockSpeed, int percent,int blockSpace, int version) { //
		this.mFindCamera = bFindCamera;
		this.mFirstFindCamera = bFirstFindCamera;
		this.mSpeedLimite = bCameraSpeedLimite;
		this.mUserCameraIndex = wUserCameraIndex;
		this.mDisToCamera = wDisToCamera;
		this.mVoiceCode = wVoiceCode;
		this.mTaCode = wTACode;
		
			
		this.mBlockSpeed = blockSpeed;
		this.mPercent = percent;
		this.mBlockSpace = blockSpace;
		
		
		this.mVersion = version;
	}

	public int getmFindCamera() {
		return mFindCamera;
	}

	public void setmFindCamera(int mFindCamera) {
		this.mFindCamera = mFindCamera;
	}

	public int getmFirstFindCamera() {
		return mFirstFindCamera;
	}

	public void setmFirstFindCamera(int mFirstFindCamera) {
		this.mFirstFindCamera = mFirstFindCamera;
	}

	public int getmSpeedLimite() {
		return mSpeedLimite;
	}

	public void setmSpeedLimite(int mSpeedLimite) {
		this.mSpeedLimite = mSpeedLimite;
	}

	public int getmUserCameraIndex() {
		return mUserCameraIndex;
	}

	public void setmUserCameraIndex(int mUserCameraIndex) {
		this.mUserCameraIndex = mUserCameraIndex;
	}

	public int getmDisToCamera() {
		return mDisToCamera;
	}

	public void setmDisToCamera(int mDisToCamera) {
		this.mDisToCamera = mDisToCamera;
	}

	public int getmVoiceCode() {
		return mVoiceCode;
	}

	public void setmVoiceCode(int mVoiceCode) {
		this.mVoiceCode = mVoiceCode;
	}

	public int getmTaCode() {
		return mTaCode;
	}

	public void setmTaCode(int mTaCode) {
		this.mTaCode = mTaCode;
	}

	
	
	
	
	 // 区间平均速度
		public int getmBlockSpeed() {
		return mBlockSpeed;
	}

	public void setmBlockSpeed(int mBlockSpeed) {
		this.mBlockSpeed = mBlockSpeed;
	}
	
		//区间完成%比 ;
		public int getmPercent() {
		return mPercent;
	}

	public void setmPercent(int mPercent) {
		this.mPercent = mPercent;
	}

		//区间距离 ;
		public int getmBlockSpace() {
		return mBlockSpace;
	}

	public void setmBlockSpace(int mBlockSpace) {
		this.mBlockSpace = mBlockSpace;
	}
	
	
	public int getmVersion() {
		return mVersion;
	}

	public void setmVersion(int mVersion) {
		this.mVersion = mVersion;
	}
	
	

}

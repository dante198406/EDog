package com.hdsc.edog.jni;
//电子眼信息
public class eEyeInfo
{
	public int m_errorRange; //距离误差
	public int m_siteType;		//第一个页面类型索引
	public int m_dir;			//角度0---360
	public int m_Lng;			//经度,
	public int m_Lat;			//纬度
	public int m_Speed;		//限速
	public String   m_ptType;	//16进制字符
	public int   m_dirType;		//0:单向,1:反向,2:双向,3：4向     最后一步中的
	public eEyeInfo()
	{}

}

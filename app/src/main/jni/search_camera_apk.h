/*
 *
 *
 *
 *
 * 2015-06-05 : 增加区间测速的 平均速度 ；区间时间 ；
 *
 *
 *
 *
 *
 *
 * search_camera_apk.h
 *
 *  Created on: 2014年7月15日
 *      Author: Administrator
 */




#ifndef SEARCH_CAMERA_APK_H_
#define SEARCH_CAMERA_APK_H_


#include "stdafx.h"


//#define SEEK_CUR    1
//#define SEEK_END    2
//#define SEEK_SET    0


//#define LOGI(...) // ((void)__android_log_print(ANDROID_LOG_INFO, "liyichang", __VA_ARGS__))
#define VOID                void

//#define  Gpsncok  10
#define    	AddBufLen    85


/*
//typedef void *				FILE;
typedef void *				PVOID;
//typedef unsigned char		BOOL;	//	  int
//typedef unsigned char *		PBOOL;  //	 int
typedef signed char			INT8;
typedef signed char *		PINT8;
typedef unsigned char		UINT8;
typedef unsigned char *		PUINT8;
typedef signed short		INT16;
typedef signed short *		PINT16;
typedef unsigned short		UINT16;
typedef unsigned short *	PUINT16;
typedef signed int			INT32;
typedef signed int *		PINT32;
typedef unsigned int		UINT32;
typedef unsigned int *		PUINT32;
typedef signed long long	INT64;
typedef signed long long *	PINT64;
typedef unsigned long long	UINT64;
typedef unsigned long long  *PUINT64;
typedef float				FLOAT;
typedef float *				PFLOAT;
typedef double				DOUBLE;
typedef double *			PDOUBLE;

*/



/*

typedef struct  _TIMETICK		   //时间，表示GPS的时间
{
    UINT8 TYear;                //十六进制 2013年只要后面的13
	UINT8 TMonth;               //十六进制
	UINT8 TDay;                 //十六进制
	UINT8 THour;                //十六进制 24小时制
	UINT8 TMinute;              //十六进制
	UINT8 TSecond;              //十六进制
}TMCURRTIME;

*/

typedef struct  _MAPINFOVER
{
//	char Flag[8];  //8
//	unsigned short PasswordFlag;
//	char Name[80];
   unsigned int    Date  ;  // 电子狗数据的日期  20140508     //	char Date[8];  //	char Date[40]; 20120205 121252
	unsigned int    Ver;     // 电子狗数据的帮本   1234      ffff
//	unsigned int    Count;	 //    	 :24

//	char  ID[16];

}MAPINFOVER ;


/*
typedef struct  _GPSINFO{

	//  NMEA_Time  gpstime  ;
int 	uc_Long;	 // unsigned	int  5位  123。123456  --》  123 12345
unsigned char LongitudePos;

int 	  uc_Lat ;	 // unsigned int 5位	  //  22。123456  --》  22 12345
unsigned char LatitudePos;

 unsigned 	 char GpsStatus   ; // 1 0  "A" 有效 "V" 无效	unsigned  char uc_Status      ; 	//定位状态

	  unsigned short  uc_SPEED;	   	// unsigned short  unsigned int
     unsigned short uc_Direction;	   //	unsigned int

	   short   uc_Elevation;			 //海拔		 unsigned int

	unsigned char gpsFixed_no;

	//	NMEA_Date	gpsdate;
	  char   gpsFixed ;  //  signed
	  int m_nGpssecond;			//GPS绉掓暟

     }GPSINFO;
*/
     typedef struct  _GPSINFO{
     	 int 	uc_Long;	 // unsigned	int  5位  123。123456  --》  123 12345
     	unsigned char LongitudePos;
     	 int 	  uc_Lat ;	 // unsigned int 5位	  //  22。123456  --》  22 12345
     	unsigned char LatitudePos;
     	unsigned char GpsStatus   ; // 1 0  "A" 有效 "V" 无效	unsigned  char uc_Status      ; 	//定位状态
     	unsigned short  uc_SPEED;	   	// unsigned short  unsigned int
         unsigned short uc_Direction;	   //	unsigned int
     	short   uc_Elevation;			 //海拔		 unsigned int
     	unsigned char gpsFixed_no;
     	char   gpsFixed ;  //  signed
     	  int  GPS_DATE;

     	  int  GPS_TimeS;		//GPS秒数
     }GPSINFO;










  //   typedef struct{
  //   	ULONG	m_uMileage ;    //行驶里程
  //       UINT8  	Average_spd ;  //平均速度
   //      UINT8  	Block_Average_spd ;  //区间测速路段 平均速度
  //    }CarInfo;


//  extern     GPSINFO 	Rxgps  ;

//   extern  CarInfo   carinfo  ;


/*=========================================================
摄像点数据结构定义
摄像点搜索用，调用Camera_Search接口后数据会自动更新
可用来处理系统显示

bFirstFindCamera =5
==========================================================*/
      /*
typedef struct _SEARCH_STATUS
{
	BOOL	bFindCamera;			//是否搜索到摄像点 1:目前已找到摄像点 0:未找到 ,报警中
    UINT8	bFirstFindCamera;       //第一次找到摄像点 1:第一次找到;2:100距离; 3:退出报警 4：退出报警时的距离  > 70M； 5：单点报警
	UINT8	bCameraSpeedLimite;		//摄像点的限速值, 0-120 130  140
	UINT32	wUserCameraIndex;		//自建摄像点的索引， 0xFFFF表示为系统内置数据	;临时 +1000,000  。增量 +2000,000 ；内部+3000,000
	UINT16	wDisToCamera;			//与摄像点的距离,以米为单位， --显示用
	UINT8	wVoiceCode;				//要播放的语音ID,具体的语音ID代表意义见附件

//sad
    UINT8	wTACode ;		 // 0~0xf  = 0xff表示 不是原始数据  //要播放的方位-语音TA,具体的语音TA代表意义见附件;


    //UINT8  Time_Block_spd  ;    //区间测速  期间   要走完的时间 。   255  最长  25分钟

}SEARCH_STATUS;

*/

typedef struct _SEARCH_STATUS {
	char bFindCamera; //鏄惁鎼滅储鍒版憚鍍忕偣 1:鐩墠宸叉壘鍒版憚鍍忕偣 0:鏈壘鍒?,鎶ヨ涓
	char bFirstFindCamera; //绗竴娆℃壘鍒版憚鍍忕偣 1:绗竴娆℃壘鍒?2:100璺濈; 3:閫€鍑烘姤璀?4锛氶€€鍑烘姤璀︽椂鐨勮窛绂? > 70M锛?5锛氬崟鐐规姤璀
	unsigned char bCameraSpeedLimite; //鎽勫儚鐐圭殑闄愰€熷€? 0-120 130  140
	unsigned int wUserCameraIndex; //鑷缓鎽勫儚鐐圭殑绱㈠紩锛?0xFFFF琛ㄧず涓虹郴缁熷唴缃暟鎹?;涓存椂 +1000,000  銆傚閲?+2000,000 锛涘唴閮?3000,000
	short wDisToCamera; //涓庢憚鍍忕偣鐨勮窛绂?浠ョ背涓哄崟浣嶏紝 --鏄剧ず鐢
	char wVoiceCode; //瑕佹挱鏀剧殑璇煶ID,鍏蜂綋鐨勮闊矷D浠ｈ〃鎰忎箟瑙侀檮浠
//sad
	char wTACode; // 0~0xf  = 0xff琛ㄧず 涓嶆槸鍘熷鏁版嵁  //瑕佹挱鏀剧殑鏂逛綅-璇煶TA,鍏蜂綋鐨勮闊砊A浠ｈ〃鎰忎箟瑙侀檮浠?

	unsigned char   wBlockSpeed ; // 区间平均速度
	unsigned char	wPercentOver ;  //区间完成%比 ;
	unsigned int    wBlockSpace  ;//区间 的 距离 。


} SEARCH_STATUS;







//***************
//
//角度 偏差 判定
//AngleA AngleB
//compangle  ：不大于这个+、-角度 ；
// 小于 返回 1，大于 返回 0
//***************



//extern SEARCH_STATUS   pStatus ;

extern "C" {


//extern
UINT8  AngleCalcul(UINT16 AngleA,UINT16 AngleB,UINT8 AngleSet)	;


 //  void LongLatToDist(UINT32  dBeginLat, UINT32  dBeginLong,  UINT32  dEndLat,    UINT32  dEndLong ,  unsigned short * dDist ) ;
// void LongLatToDist(INT32   B1, INT32  L1,  INT32   B2,  INT32   L2 , unsigned short  * dDist )  ;


  //***************
  //
  //b1 L1  起点 维度经度
  //b2 L2  终端点 维度经度
  //；
  //  返回角度 0
  //***************



 // extern
  UINT16 LongLatToDist(INT32   B1, INT32  L1,  INT32   B2,  INT32   L2 , unsigned short  * dDist,UINT8 C_ANGLE ) ;


  //***************
  //  文件初始化
  //
  //返回 0  电子狗文件 ok  ；
  //返回 1  电子狗文件 出错  ；
  //
  //***************
 // extern
  void  map_init(FILE *file, SEARCH_STATUS *pStatus,  MAPINFOVER *pMapinfover);

// void ReadUserDataBase( FILE *map_fd, int AddRecodeNo , /*out*/SEARCH_STATUS *pStatus,GPSINFO *Rxgps ) ;

//extern	 void AlarmEvent(/*out*/SEARCH_STATUS *pStatus ) ;
//extern
void AlarmEvent(/*out*/SEARCH_STATUS *pStatus ,GPSINFO *Rxgps );
 // void Coordinate_Search(void)

  /*=========================================================
  当GPS信号发生变化时，调用此接口搜索摄像点（建议一秒调用一次）
  返回数据在SEARCH_STATUS // extern
  =========================================================*/
//  void 	Camera_Search(FILE  *map_fd ,/*in*/GPSINFO *pGPSInfo,/*in*/TMCURRTIME *pCurrTime, /*out*/SEARCH_STATUS *pStatus);

 //放主循环 ，
 // void process_map(void);
 // extern
  void process_map(FILE *map_fd, /*out*/SEARCH_STATUS *pStatus  ,GPSINFO *Rxgps);

 // extern  int  Radar_chk( char  *RxData, GPSINFO *Rxgps);

 //  extern
   int  Radar_chk( char  *RxData) ;  //, int GpsDate

   //RxData  :" 0xCC,0x55 , 0xF7 ,0x**, ...... "  ==>  CC55F7**XX**XXCC55F7**XX**XX....
   //GpsDate :  20150525



 // extern  BOOL  GetWarningMode();

  }


#endif /* SEARCH_CAMERA_APK_H_ */

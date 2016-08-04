/*
 *
 *
 *
 *
 *
 *
 *
 *
 * 增加区间测速的 时间   ，推算 距离  ， | -------------------------|  ，
 *
 * */

#include "search_camera_apk.h"
//#include "EdogData.h"

#define LOGI(...)  ((void)__android_log_print(ANDROID_LOG_INFO, "liyichang", __VA_ARGS__))

extern "C" {

typedef struct {
	unsigned char enable; 		//T_BOOL ����
//	T_U32   Number;			//������ݱ��
	INT32 Lat;				//Ŀ��γ��
	INT32 Long;			//Ŀ�꾭��
	unsigned short Distance;			//����
	unsigned short Angle;			//�Ƕ�
	UINT32 VoiceNum;		//��Ƶ�ļ����
	unsigned char SpeedLimit;
//	T_U32   Index;
//	T_U8	Mute;
	unsigned char Type;
	unsigned char UserSpeedLimit;   //��䳵��

//JT_V1361
//INT32	Lat_s;				//���γ��
//INT32	Long_s;			//��㾭��

	UINT16 BlockSpace ;   //区间测速的 总距离 M
	//unsigned short BlockAngle;


} WARNING;

typedef struct {
	char Flag[8];  //8
//	unsigned short PasswordFlag;
//	char Name[80];
	unsigned int Date; // ���ӹ���ݵ�����  20140508     //	char Date[8];  //	char Date[40]; 20120205 121252
	unsigned int Ver;     // ���ӹ���ݵİﱾ   1234      ffff
	unsigned int Count;	 //    	 :24

	char ID[16];

	INT32 Min_Longitude;
} MapInfo;

typedef struct {
	UINT32 Index;
	UINT32 Index1;
	UINT32 Index2;
	UINT32 Index3;
	UINT32 Index4;
	UINT32 Index5;
} CHKID;

#define 	 DDNUBER    100000
#define 	 LAT_FT    800  //    100000
#define 	 LONG_FT   1600 //    100000
//	 extern 	 UINT8		  e_warning_safe ;

UINT32 TIMES_LOOP;
//#define TIMES_LOOP  500


WARNING e_warning;
CHKID S_INDEX;
CHKID O_INDEX;

//   extern UINT8 TBUF[TXBUFSIZE] ;
//  Map_Data  mdata     ;
MapInfo FileInfo;

UINT8 mdata_Voice;
UINT8 mdata_SpeedLimit;
UINT16 mdata_Angle;
UINT8 mdata_DataType;

//unsigned short PasswordEnable;
static char PassKeyValue_501[8] = { 0};

int UserSpeedLimit_Time = 0;
//UINT16 BlockSpace = 0;   //区间测速的 总距离 M
int Last_TimeS = 0;
double Tspace = 0;       //区间测速路段 走的距离
UINT16 BlockTime = 0;    //区间测速路段 要用时

UINT8 RdUserFlag = 0;

UINT8 middle_L;  // = 0;
UINT32 middle;

INT32 WD_start;
INT32 JD_start;
INT32 WD_over;
INT32 JD_over;
INT32 OLD_Long_S;

UINT32 Rcoder_all;
UINT8 OLD_Recode_SUB_Long;
UINT16 LastDist;  //  = 0xFFFF;
//UINT16	   dist	;
//UINT8	   dist2over	 ;
INT32 WD_PASS, JD_PASS;

UINT8 TIME_BLOCK;

int GPS_Date = 0;
int cSAD_RD_ERR = 0;

unsigned char Last_RD_CONT = 0;

//UINT8  AngleCalcul(UINT16 AngleA,UINT16 AngleB,UINT8 compangle)

UINT8 AngleCalcul(UINT16 AngleA, UINT16 AngleB, UINT8 AngleSet) {

	//JT_1006

	if ((abs(AngleA - AngleB) <= AngleSet)
			|| (abs(AngleA - AngleB) >= (360 - AngleSet))) {
		return TRUE;
	}   //JT_V1346
	else {
		return FALSE;
	}    //JT_V1346

}

//=======================

//JT_V1361A

// ��  �ܣ���֪�����յ㾭γ�ȣ����򣨷�λ�������̣����룩
// ����ֵ�������յ㾭γ��
// ����ֵ���յ㾭γ��(������ʽ)

//���������ľ���(��Ϊ��λ)  jyk  2008-0925

//  void LongLatToDist(unsigned long  dBeginLat, unsigned long  dBeginLong,  unsigned long  dEndLat,    unsigned long  dEndLong , unsigned short  * dDist )

UINT16 LongLatToDist(INT32 B1, INT32 L1, INT32 B2, INT32 L2,
		unsigned short * dDist, UINT8 C_ANGLE) 		 //1-->2
		{


	*dDist = 0 ;

	if (!C_ANGLE)
		return 0;


	return 90;

}



void  map_init (FILE *file,SEARCH_STATUS *pStatus, MAPINFOVER *pMapinfover )
	{

	char CHK_data_read[1];

	UINT8 CHK_data_all, CHK_data_over;
	UINT32 k;  //,m ;


//	CHK_data_over = 0;

	fseek(file, 0, SEEK_SET);

		fread(&FileInfo, sizeof(MapInfo), 1, file);



		pMapinfover->Ver = 01 ;//FileInfo.Ver;


	pMapinfover->Date = 0 ;// FileInfo.Date;

	middle = 0;
	middle_L = 0;
	e_warning.Distance = 0;
	e_warning.UserSpeedLimit = 0xff;
	e_warning.SpeedLimit = 0xff;
	//NUMBER_SHOW_HOLD = 0;

	e_warning.BlockSpace =0 ;

	WD_PASS = 0xffffffff;
	JD_PASS = 0xffffffff;

	pStatus->bFirstFindCamera = 0 ;
	pStatus->bFindCamera= 0 ;
	pStatus->bCameraSpeedLimite= 0xff ;
	pStatus->wDisToCamera= 0 ;
	pStatus->wTACode= 0 ;
	pStatus->wVoiceCode= 0 ;


	pStatus->wBlockSpeed= 0 ;
	pStatus->wPercentOver= 0 ;
	pStatus->wBlockSpace= 0 ;


//	 LOGI("S_INDEX.Index: %d " ,  S_INDEX.Index   );
//	 LOGI("S_INDEX.Index1: %d " ,  S_INDEX.Index1   );
	//	 LOGI("O_INDEX.Index: %d " ,  O_INDEX.Index   );
//	 LOGI("O_INDEX.Index1: %d " ,  O_INDEX.Index1   );

	S_INDEX.Index5 = 0;
		S_INDEX.Index4 =0;
		S_INDEX.Index3 = 0;
		S_INDEX.Index2 = 0;
		S_INDEX.Index1 = 0;
		S_INDEX.Index = 0; //

		O_INDEX.Index5 = 0;
		O_INDEX.Index4 =0;
		O_INDEX.Index3 = 0;
		O_INDEX.Index2 = 0;
		O_INDEX.Index1 = 0;
		O_INDEX.Index = 0; //



	TIMES_LOOP = 50000 ;//FileInfo.Count / 2;    // 3100  ;  //

	}

/*

//��ȡ�Խ��ļ�Sad-User.dat
// void CEdogApplication::ReadUserDataBase(GPSINFO & Rxgps )    // UINT16 & waytem,CGpsInfo& info)
void ReadUserDataBase(FILE *map_fd, int AddRecodeNo, SEARCH_STATUS *pStatus, GPSINFO *Rxgps) // UINT16 & waytem,CGpsInfo& info)

		{
	char tBufAdd[AddBufLen];  //
	string BufAdd, p;

	UINT16 angleN;	//	eAngle,eAngle=angleN = 0;

	UINT16 AddSpace, SpaceAll;

	string TYPETAT123, SpeedT123, CateT123;
	UINT16 AngleT123;
	INT32 X1T123, Y1T123, X2T123, Y2T123, DATAIDT123;    //JT_V VH100

	int iop = AddRecodeNo; //  ������ 0;// 1; �ޱ���ͷ

	//	fseek(map_fd,0, SEEK_SET);

	pStatus->bFirstFindCamera = 0;

	while (iop)  //< 1000)     //JT_V VH102	while(TRUE)
	{
		fseek(map_fd, AddBufLen * (iop - 1) * 1L, 0); // ���Խ�������� 	"\\Sad-User.dat"; //SEEK_CUR=1
		int len = fread(tBufAdd, 1, AddBufLen, map_fd);
		if (len != AddBufLen) //== 0)
		{
			iop--;
			continue;  //	break;
		}
		//JT_V VH101B		DES_Act(buf3, buf3, sizeof(buf3), key2, sizeof(key2), DECRYPT);
		//Tem=BufAdd;
		BufAdd = tBufAdd;
		int i = 0;
		//		string TYPETAT123,SpeedT123,CateT123;
		//	UINT16 AngleT123;
		//	INT32	X1T123,Y1T123,X2T123,Y2T123,DATAIDT123;    //JT_V VH100

		//	while(i<11)  //
		while (i < 9) {
			//break;
			int sp = BufAdd.find(",");
			if (sp > -1) {
				p = BufAdd.substr(0, sp);
				BufAdd = BufAdd.substr(sp + 1);
				//  JT_V VH218
				//	if ( sp  == 0 )
				//	break ;
			} else {
				break;
			}
			if (i == 0) {
				DATAIDT123 = atoi(p.c_str());
				//	if( DATAIDT123 < 900000 )    //JT_V VH102
				//	{
				//		break;
				//	}
			} else if (i == 1) {
				TYPETAT123 = p;
			} else if (i == 2) {
				AngleT123 = atoi(p.c_str());
			} else if (i == 3) {
				X1T123 = atoi(p.c_str());
			} else if (i == 4) {
				Y1T123 = atoi(p.c_str()) - AngleT123;//JT_V VH101B		Y1T123 = _ttoi(p);
			} else if (i == 5) {
				X2T123 = atoi(p.c_str());
			} else if (i == 6) {
				Y2T123 = atoi(p.c_str()) + AngleT123;//JT_V VH101B		Y2T123 = _ttoi(p);
			} else if (i == 7) {
				SpeedT123 = p;
			} else if (i == 8) {
				CateT123 = p; //JT_V VH003_PIO   BufAdd;  //鏈�悗娌℃湁绌烘牸浜嗭紝鎵�互鍏ㄩ儴绛夛�?
				break;
			}

			i++;
		}  ///  ��Ӧ while(i<9)

		//  JT_V VH218A



		if (AngleCalcul(AngleT123, Rxgps->uc_Direction, 40)) {
			//���Ծ���
			//CEdogUtil::LongLatToXY(info.m_Lng, info.m_Lat, X1T123, Y1T123 ,  &angleN , &space );

			angleN = LongLatToDist(Rxgps->uc_Lat, Rxgps->uc_Long, X1T123,
					Y1T123, &AddSpace, 1);

			SpaceAll = 350;
			if (atoi(SpeedT123.c_str()) > 50)  // ;
				SpaceAll = 650;

			if (AngleCalcul(angleN, AngleT123, 35)) {
				if ((AddSpace < SpaceAll) && (AddSpace > 50)) //JT_V1023  if(distance < mdata.Distance && distance > 40)
						{

					e_warning.enable = 1;
					e_warning.Angle = AngleT123;
					e_warning.Lat = X1T123;
					e_warning.Long = Y1T123;
					//  e_warning.

					break;
				}
			}  //   ��Ӧ if(AngleCalcul(angleN , AngleT123,compangle))
		}
		iop--;  //++;
	}  //   ��Ӧ  while(TRUE)

	pStatus->bFindCamera = e_warning.enable;   //   TRUE   ;//   enable     ;

	if (pStatus->bFindCamera) {
		pStatus->bCameraSpeedLimite = atoi(SpeedT123.c_str());

		if (pStatus->bCameraSpeedLimite < 20
				|| pStatus->bCameraSpeedLimite > 120)
			pStatus->bCameraSpeedLimite = 0xff;

		pStatus->wDisToCamera = AddSpace;

		//	 pStatus->wUserCameraIndex  =  DATAIDT123 + 200 ;

		pStatus->wTACode = atoi(TYPETAT123.c_str());
		pStatus->wVoiceCode = atoi(CateT123.c_str());

		if (pStatus->wUserCameraIndex != DATAIDT123) {
			//	ShowSpeedTime = 0 ;        //  JT_V VH211D
			//	IfSound=1;
			pStatus->bFirstFindCamera = 1;

		}
		//	else {
		//	pStatus->bFirstFindCamera	= 0;
		//	}

		pStatus->wUserCameraIndex = DATAIDT123;
	}

}
*/



//1.�����ҵ���ǰ��+/-500�������ʼ�㵽�����λ��
//2.����ݿ�ʼ��Ƚ�γ�ȣ�����500����������һ��
//3.��γ�Ⱦ�С��500���ٱȽϽǶ�(+/-38~40��)
//4.����������ʵ�����,С��Ԥ�����Ҵ���40m����,������һ����


UINT8 compare(UINT32 recodeno, GPSINFO *Rxgps) /*(MAP_hFILE map_fd, unsigned int LocationNumber)*/
{

	BOOL LastIndexflg;

	UINT16 distance;    //,  DIST_OPER;	  // angle,

	UINT16 distance_S, distance_ALL;    // ,angle1;
	return 0;
}

//=======================
//    INT8  MAP_PAGE_READ_BUF[256] ;
// 	UINT32  MAP_READADD ;
//	UINT32   MAP_READPAGE   ;
//UINT32  LAST_MAP_READPAGE  ;//=0xffffffff ;
//

void GetMapData(char * CurrData, UINT32 Number) {
	// JT_V1x86A
	char temData;   //  INT8  temData ;
	int k;


}

//1��3��5��7��9��11��13��15	��9

//void Coordinate_Search(void)     //   MAP_hFILE  map_fd   // JT_V1x88  JT_V1087A int Coordinate_Search(MAP_pCWSTR MapName)

/*=========================================================
 ��GPS�źŷ���仯ʱ�����ô˽ӿ���������㣨����һ�����һ�Σ�
 ���������SEARCH_STATUS


 =========================================================
 */
//void 	Camera_Search(FILE  *map_fd ,/*in*/GPSINFO *pGPSInfo,/*in*/TMCURRTIME *pCurrTime, /*out*/SEARCH_STATUS *pStatus)
///{
// }
//=============

//ɾ��
// JT_V1x89

//UINT8 CompareData(MAP_pCWSTR MapName, int* location)	//MAP_pCWSTR
//{
//	return -1;
//}

//JT_V1350G   MAP_VOID AlarmEvent( MAP_TIMER timer_id, UINT32 delay )

//JT_V1350G

//void AlarmEvent(void)
void AlarmEvent(/*out*/SEARCH_STATUS *pStatus, GPSINFO *Rxgps)

{

	UINT16 Almdist;
	static UINT8 dist2over;
	UINT16 Tempspd;
	UINT16 TmpDistance;
	UINT8 LASTTSecond;


		if (LastDist > TmpDistance)
			LastDist = TmpDistance;

		e_warning.Distance = LastDist;

		pStatus->wDisToCamera = e_warning.Distance;

		pStatus->bFindCamera = e_warning.enable;
		pStatus->bFirstFindCamera = 0;

}

// void process_map(void)
void process_map_A(FILE *map_fd, /*out*/SEARCH_STATUS *pStatus,
		GPSINFO *Rxgps) {

	UINT8 Recode_SUB_Long;
	INT8 TEMP;

	int k;

	UINT8 Flag_ALM = 0;

	char MAP_BUF[8];

	if (Rxgps->gpsFixed >= 3) //?if( (size == 0xffffffff) && RunCfg.Validity	&& (e_warning.enable == 0))

			{

		if (GPS_Date == 0)
			GPS_Date = Rxgps->GPS_DATE;



		for (k = 0; k < TIMES_LOOP; k++) ////2000  20140303   1000 --> 3000  JT_V1x86A  for(k = 0;k < 1000 ;k ++)  // JT_V1X77C     // //JT_V1350C        涓�釜鍛拷?锟�000锟�  //JT_V1350A k < 1000;  涓�潯鏁帮拷?锟�mS 鏃堕�?

				{}	 //��Ӧ  for(k = 0;k < 500 ;k ++)

	}

	if (Flag_ALM == 1)
		pStatus->bFirstFindCamera = 1;
	else
		pStatus->bFirstFindCamera = 0;

//SET_pStatus:

	pStatus->bFindCamera = e_warning.enable;     //   TRUE  ;  //   enable     ;
	pStatus->bCameraSpeedLimite = e_warning.SpeedLimit;

	if (pStatus->bCameraSpeedLimite < 20 || pStatus->bCameraSpeedLimite > 120)
		pStatus->bCameraSpeedLimite = 0xff;

	pStatus->wDisToCamera = e_warning.Distance;

	pStatus->wUserCameraIndex = S_INDEX.Index + 200;

	pStatus->wTACode = e_warning.Type;
	pStatus->wVoiceCode = e_warning.VoiceNum;
	pStatus->wBlockSpace = e_warning.BlockSpace ;
}

//==============================
void process_map(FILE *map_fd, /*out*/SEARCH_STATUS *pStatus, GPSINFO *Rxgps) {


}

/*
 void Drive_Info( CarInfo  *carinfo, GPSINFO *Rxgps)

 {
 ULONG Speedall ,Speedcont ;

 carinfo->m_uMileage  +=  (int)(Rxgps->uc_SPEED/(3.60f*2.0f));//因为是500ms记一次

 if(carinfo->Block_Average_spd == 0)

 {
 Speedall = 0 ;
 Speedcont = 0 ;

 }

 else
 {

 Speedall +=   Rxgps->uc_SPEED ;
 Speedcont ++ ;
 carinfo->Block_Average_spd = Speedall/Speedcont ;

 }
 }
 */

// unsigned char Last_RD_CONT = 0;
//int  Radar_chk( char  *RxData, GPSINFO *Rxgps)
int Radar_chk(char *RxData)   //GpsDate :  20150525, int GpsDate

		{

	unsigned char RD_CONT = 0;
	unsigned char RD_CONTA = 0;
	unsigned char RD_ChkSum = 0;


	return 0;
}

}    //extern "C" {

/*
 * Copyright 2009-2011 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>
#include "stdafx.h"
#include <sstream>
#include<pthread.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dlfcn.h>
#include <stdio.h>


#include "EdogData.h"
#include "search_camera_apk.h"

#include "android/log.h"
//#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , "EdogData.cpp", __VA_ARGS__)

extern "C" {
//void *edogHandle = NULL;    //void *handle = NULL;
//map_init init = NULL;
//process_map process = NULL;
GPSINFO gpsinfo;
SEARCH_STATUS search_status;
MAPINFOVER mapinfover;
FILE *file = NULL ;  //,*Logofile

static int dog_version = -1;
void DoginitData(JNIEnv *env, jobject obj, jstring filePath , jobject   gpsInfo) {//,jstring libPath  jstring LogoPath,



/*
	if (edogHandle == NULL) {

		const char *soFile = env->GetStringUTFChars(libPath, 0);
		if ((edogHandle = dlopen(soFile, RTLD_NOW)) == NULL) {
			LOGE("dlopen - %sn", dlerror());
			exit(-1);
		}

		if (init == NULL) {
			init = (map_init) dlsym(edogHandle, "map_init");
			LOGE("TEST init dlsym - %sn", dlerror());
		}

		const char *eDogFile = env->GetStringUTFChars(filePath, 0);
		if ((file = fopen(eDogFile, "rb")) == NULL) {
			LOGE("file == NULL");
		}


		const char *logoFile = env->GetStringUTFChars(LogoPath, 0);
		LOGE("logoFile== %s",logoFile);
		if ((Logofile = fopen(logoFile, "rb")) == NULL) {
			LOGE("fileLogo == NULL");
		}

		int r = init(file, Logofile, &mapinfover);
		version = mapinfover.Ver;

	//	LOGE("TEST CHK_LOG RTURN = %X", r );



		if (process == NULL) {
			process = (process_map) dlsym(edogHandle, "process_map");
			LOGE("process dlsym - %sn", dlerror());
		}
	}

	*/




//LOGE("dog_version-aa = %d", dog_version );
//	if (dog_version == -1) {

if (file == NULL) {
      	const char *eDogFile = env->GetStringUTFChars(filePath, 0);
		if ((file = fopen(eDogFile, "rb")) == NULL) {
			//;
		//	LOGE("file == NULL");
		}
		else{

			map_init(file, &search_status,&mapinfover);

		dog_version =(int) mapinfover.Ver;

	//	LOGE("TEST CHK_LOG RTURN = %X", r );
		}

	}













	jclass gps_cls = env->GetObjectClass(gpsInfo);  //或得GpsInfo类引用
	if (gps_cls == NULL) {
		//LOGE("gps_cls == null");
	}

	jfieldID lngFieldID = env->GetFieldID(gps_cls, "lng", "I");
	jint lng = env->GetIntField(gpsInfo, lngFieldID); //纬度

	jfieldID latFieldID = env->GetFieldID(gps_cls, "lat", "I");
	jint lat = env->GetIntField(gpsInfo, latFieldID); //经度

	jfieldID altitudeFieldID = env->GetFieldID(gps_cls, "altitude", "I");
	jint altitude = env->GetIntField(gpsInfo, altitudeFieldID); //海拔

	jfieldID speedFieldID = env->GetFieldID(gps_cls, "speed", "I");

	jint speed = env->GetIntField(gpsInfo, speedFieldID); //速度

	jfieldID bearingFieldID = env->GetFieldID(gps_cls, "bearing", "I");
	jint bearing = env->GetIntField(gpsInfo, bearingFieldID); //方向

	jfieldID satelliteCountFieldID = env->GetFieldID(gps_cls, "satelliteCount",	"I");
	jint satelliteCount = env->GetIntField(gpsInfo, satelliteCountFieldID); //卫星个数

	jfieldID gpsDateFieldID = env->GetFieldID(gps_cls, "gpsDate", "I");
	jint gpsDate = env->GetIntField(gpsInfo, gpsDateFieldID); //当前日期


	jfieldID gpsTimeSFieldID = env->GetFieldID(gps_cls, "gpsTimeS", "I");
	jint gpsTimeS = env->GetIntField(gpsInfo, gpsTimeSFieldID); //当前时间

	jfieldID gpsFixTimeFieldID = env->GetFieldID(gps_cls, "gpsFixTime", "I");
	jint gpsFixTime = env->GetIntField(gpsInfo, gpsFixTimeFieldID); // gpsFixTime




	gpsinfo.LatitudePos = 0;
	gpsinfo.LongitudePos = 0;
	gpsinfo.uc_Long = lng;
	gpsinfo.uc_Lat = lat;
	//gpsinfo.GpsStatus = 1;
	gpsinfo.uc_SPEED = speed;
	gpsinfo.uc_Direction =bearing;
	gpsinfo.uc_Elevation = altitude;
	gpsinfo.GPS_DATE = gpsDate;

    gpsinfo.GPS_TimeS = gpsTimeS;

     gpsinfo.GpsStatus = 0;
/*

	if(satelliteCount >= 3 ){
		gpsinfo.GpsStatus = 1;
		gpsinfo.gpsFixed = 5;
	}
	else{

		if(gpsinfo.gpsFixed >0 ){
		gpsinfo.gpsFixed -- ;
	//	if(gpsinfo.gpsFixed ==0 ) ;
}
*/

	gpsinfo.gpsFixed = gpsFixTime  ;  //gpsFixed  1-->8
	if( gpsinfo.gpsFixed >= 6 ){

		gpsinfo.GpsStatus = 1;

		}




//	LOGE("GPS_TimeS =%d", gpsTimeS);

/*
	LOGE("uc_Long %d", gpsinfo.uc_Long);
LOGE("uc_Lat %d", gpsinfo.uc_Lat);
	LOGE("GpsStatus %d", gpsinfo.GpsStatus);
	LOGE("uc_SPEED %d", gpsinfo.uc_SPEED);
	LOGE("gpsFixed %d", gpsinfo.gpsFixed);
	LOGE("GPS_DATE %d", gpsinfo.GPS_DATE);

	LOGE("bFirstFindCamera %d", search_status.bFirstFindCamera);
		LOGE("bFindCamera %d", search_status.bFindCamera);
		LOGE("bCameraSpeedLimite %d", search_status.bCameraSpeedLimite);
		LOGE("wDisToCamera %d", search_status.wDisToCamera);
		LOGE("wTACode %d", search_status.wTACode);
		LOGE("wVoiceCode %d", search_status.wVoiceCode);
*/

//	process(file, &search_status, &gpsinfo);
	process_map(file, &search_status, &gpsinfo);
	//  extern void process_map(FILE *map_fd, /*out*/SEARCH_STATUS *pStatus  ,GPSINFO *Rxgps);
/*
  LOGE("bFirstFindCamera %d", search_status.bFirstFindCamera);
	LOGE("bFindCamera %d", search_status.bFindCamera);
	LOGE("bCameraSpeedLimite %d", search_status.bCameraSpeedLimite);
	LOGE("wDisToCamera %d", search_status.wDisToCamera);
	LOGE("wTACode %d", search_status.wTACode);
	LOGE("wVoiceCode %d", search_status.wVoiceCode);


	LOGE("wBlockSpeed %d", search_status.wBlockSpeed);
	LOGE("wPercentOver %d", search_status.wPercentOver);
	LOGE("wBlockSpace %d", search_status.wBlockSpace);
*/

}



JNIEXPORT jobject JNICALL Java_com_hdsc_edog_jni_EdogDataManager_getEdogData(
		JNIEnv *env, jobject obj, jstring filePath,
		jobject  gpsInfo) {// jstring libPath,jstring logoPath,

	DoginitData(env, obj, filePath,gpsInfo);//logoPath, libPath,initData(env, obj, filePath,fileLogoPath, libPath, gpsInfo);

	//关于包描述符，这儿可以是 tuzhi/edog/jni/EdogData 或者是 Ltuzhi/edog/jni/EdogData;
	//这两种类型 都可以获得class引用
	jclass cls = env->FindClass("com/hdsc/edog/entity/EdogData"); //或得EdogData类引用

	//LOGE("AAAAA %d", 1);

	//获得得该类型的构造函数  函数名为 <init> 返回类型必须为 void 即 V

//	jmethodID constrocMID = env->GetMethodID(cls, "<init>", "(IIIIIIII)V");

	jmethodID constrocMID = env->GetMethodID(cls, "<init>", "(IIIIIIIIIII)V");

	//构造一个对象，调用该类的构造函数，并且传递参数
	//LOGE("AAAAA %d", 2);

	jobject edogdata_ojb = env->NewObject(cls, constrocMID,
			search_status.bFindCamera, search_status.bFirstFindCamera,
			search_status.bCameraSpeedLimite, 0, search_status.wDisToCamera,
			search_status.wVoiceCode, search_status.wTACode ,
			search_status.wBlockSpeed ,search_status.wPercentOver , search_status.wBlockSpace,dog_version);
//	LOGE("AAAAA %d", 3);
	return edogdata_ojb;
}

}

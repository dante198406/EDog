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
#include <sstream>
#include<pthread.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dlfcn.h>



#include "RadarData.h"
//#include "EdogData.h"
#include "search_camera_apk.h"

#include "android/log.h"
static const char *TAG = "RadarData.cpp";
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

extern "C" {

//extern void *radarHandle = NULL;
//check_radar check = NULL;
//extern GPSINFO gpsinfo;


int hexStr2CharArr(const char* str, char * dest) {

	int i = 0, len = 20;

	//len = strlen(dest);    //
	len = strlen(str);

	if(len >= 30 ) len = 30 ;

	for (i = 0; i < len; i++) {

		if (i % 2 == 0) {
			if (*(str + i) >= 'A' && *(str + i) <= 'F')
				*(dest + i / 2) =10+(*(str + i) - 'A') << 4;
			else
				*(dest + i / 2) = (*(str + i) - '0') << 4;
		} else {
			if (*(str + i) >= 'A' && *(str + i) <= 'F')
				*(dest + i / 2) |= (*(str + i) - 'A') + 10;
			else
				*(dest + i / 2) |= (*(str + i) - '0');
		}
	}
	return 0;
}







//JNIEXPORT jint JNICALL Java_com_sad_edog_jni_RadarDataManager_checkRadar(
//		JNIEnv *env, jobject obj, jstring radarData, jobject gpsInfo)


//JNIEXPORT jint JNICALL Java_com_sad_edog_jni_RadarDataManager_checkRadar(
//		JNIEnv *env, jobject obj, jchar  radar_data, jobject gpsInfo)

JNIEXPORT jint JNICALL Java_com_hdsc_edog_jni_RadarDataManager_checkRadar(
		JNIEnv *env, jobject obj,  jstring radarData/*,jobject gpsInfo*/) { //jstring libPath,

	/*

	jclass gps_cls = env->GetObjectClass(gpsInfo); //鎴栧緱GpsInfo绫诲紩鐢
	if (gps_cls == NULL) {
		LOGE("gps_cls == null");
	}

	jfieldID lngFieldID = env->GetFieldID(gps_cls, "lng", "I");
	jint lng = env->GetIntField(gpsInfo, lngFieldID); //绾害
	jfieldID latFieldID = env->GetFieldID(gps_cls, "lat", "I");
	jint lat = env->GetIntField(gpsInfo, latFieldID); //缁忓害
	jfieldID altitudeFieldID = env->GetFieldID(gps_cls, "altitude", "I");
	jint altitude = env->GetIntField(gpsInfo, altitudeFieldID); //娴锋嫈
	jfieldID speedFieldID = env->GetFieldID(gps_cls, "speed", "I");

	jint speed = env->GetIntField(gpsInfo, speedFieldID); //閫熷害
	jfieldID bearingFieldID = env->GetFieldID(gps_cls, "bearing", "I");
	jint bearing = env->GetIntField(gpsInfo, bearingFieldID); //鏂瑰悜
	jfieldID satelliteCountFieldID = env->GetFieldID(gps_cls, "satelliteCount", "I");
	jint satelliteCount = env->GetIntField(gpsInfo, satelliteCountFieldID); //鍗槦涓暟
	jfieldID gpsDateFieldID = env->GetFieldID(gps_cls, "gpsDate", "I");
	jint gpsDate = env->GetIntField(gpsInfo, gpsDateFieldID); //褰撳墠鏃ユ湡

	gpsinfo.LatitudePos = 0;
	gpsinfo.LongitudePos = 0;
	gpsinfo.uc_Long = lng;
	gpsinfo.uc_Lat = lat;
	gpsinfo.GpsStatus = 1;
	gpsinfo.uc_SPEED = speed;
	gpsinfo.uc_Direction = bearing;
	gpsinfo.uc_Elevation = altitude;
	gpsinfo.GPS_DATE = gpsDate;
	gpsinfo.gpsFixed = 5;


*/
	//if (check == NULL) {
	//	check = (check_radar) dlsym(handle, "Radar_chk");
	//	LOGE("checkRadar dlsym - %sn", dlerror());
//	}

	/*
	if (radarHandle == NULL) {
		const char *soFile = env->GetStringUTFChars(libPath, 0);
		if ((radarHandle = dlopen(soFile, RTLD_NOW)) == NULL) {
			printf("dlopen - %sn", dlerror());
			LOGE("dlopen - %sn", dlerror());
			exit(-1);
		}
		LOGE("dlopen success");
		if (check == NULL) {
			check = (check_radar) dlsym(radarHandle, "Radar_chk");
			LOGE("checkRadar dlsym - %sn", dlerror());
		}
	}
*/


	const char * data = env->GetStringUTFChars(radarData, 0);   //*
	//LOGE("radar_data: %s", data);
	char radar_data[30]={0};
	//char * radar_data = 0 ;
	hexStr2CharArr(data, radar_data);


	//LOGE("radar_data 0: %x", radar_data[0]);
	//LOGE("radar_data 1: %x", radar_data[1]);
	//LOGE("radar_data 2: %x", radar_data[2]);
	//LOGE("radar_data3: %x", radar_data[3]);


//	int chk_result = check(radar_data);  //, gpsinfo.GPS_DATE

int chk_result = Radar_chk(radar_data);


 //const char radar_data = env->GetStringUTFChars(radarData, 0);


 // char  radar_data[14]={0xCC, 0x55, 0xF7, 0x8F, 0x70 ,0x7D ,0x9A ,0xCC ,0x55, 0xF7, 0x8E ,0x71 ,0x7D, 0x9A};

 // int chk_result = check(radar_data, &gpsinfo);

	//LOGE("chk_result %d", chk_result);
	return chk_result;
}




char data[] = "1212121212121212";
static speed_t getBaudrate(jint baudrate) {
	switch (baudrate) {
	case 0:
		return B0;
	case 50:
		return B50;
	case 75:
		return B75;
	case 110:
		return B110;
	case 134:
		return B134;
	case 150:
		return B150;
	case 200:
		return B200;
	case 300:
		return B300;
	case 600:
		return B600;
	case 1200:
		return B1200;
	case 1800:
		return B1800;
	case 2400:
		return B2400;
	case 4800:
		return B4800;
	case 9600:
		return B9600;
	case 19200:
		return B19200;
	case 38400:
		return B38400;
	case 57600:
		return B57600;
	case 115200:
		return B115200;
	case 230400:
		return B230400;
	case 460800:
		return B460800;
	case 500000:
		return B500000;
	case 576000:
		return B576000;
	case 921600:
		return B921600;
	case 1000000:
		return B1000000;
	case 1152000:
		return B1152000;
	case 1500000:
		return B1500000;
	case 2000000:
		return B2000000;
	case 2500000:
		return B2500000;
	case 3000000:
		return B3000000;
	case 3500000:
		return B3500000;
	case 4000000:
		return B4000000;
	default:
		return -1;
	}
}


/*
 * Class:     android_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */JNIEXPORT jobject JNICALL Java_com_hdsc_edog_jni_RadarDataManager_openRadar(
		JNIEnv *env, jclass thiz, jstring path, jint baudrate, jint flags) {
	int fd;
	speed_t speed;
	jobject mFileDescriptor;

	/* Check arguments */
	//{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			/* TODO: throw an exception */
			//LOGE("Invalid baudrate");
			return NULL;
		}
	//}

	/* Opening device */
	//{
		jboolean iscopy;
		const char *path_utf = env->GetStringUTFChars(path, &iscopy);

	//	LOGE("Opening serial port %s with flags 0x%x",path_utf, O_RDWR | flags);

		fd = open(path_utf, O_RDWR | flags);

	//	LOGE("open() fd = %d", fd);

		env->ReleaseStringUTFChars(path, path_utf);
		if (fd == -1) {
			/* Throw an exception */
			// LOGE("Cannot open port");
			/* TODO: throw an exception */
			return NULL;
		}
	//}

	/* Configure device */
	//{
		struct termios cfg;
	//	LOGE("Configuring serial port");
		if (tcgetattr(fd, &cfg)) {
			//LOGE("tcgetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}

		cfmakeraw(&cfg);
		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);

		if (tcsetattr(fd, TCSANOW, &cfg)) {
			//LOGE("tcsetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}
	//}

	/* Create a corresponding file descriptor */
//	{
		jclass cFileDescriptor = env->FindClass("java/io/FileDescriptor");
		jmethodID iFileDescriptor = env->GetMethodID(cFileDescriptor, "<init>",
				"()V");
		jfieldID descriptorID = env->GetFieldID(cFileDescriptor, "descriptor",
				"I");
		mFileDescriptor = env->NewObject(cFileDescriptor, iFileDescriptor);
		env->SetIntField(mFileDescriptor, descriptorID, (jint) fd);
//	}

	return mFileDescriptor;
}

}

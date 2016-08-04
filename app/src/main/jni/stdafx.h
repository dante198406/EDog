#ifndef _JNI_PLATFORM_
#define _JNI_PLATFORM_	1

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <vector>
//using ::std::vector;
#include <string>
//using ::std::string;
//using ::std::basic_string;
#include<iostream>
#include<sstream>
//using ::std::sstream;
#include "JniDef.h"
#include <android/log.h>
#include <jni.h>
//using namespace std;
#define LOGI(...)    ((void)__android_log_print(ANDROID_LOG_INFO, "liyichang", __VA_ARGS__))
#define LOGE(...)    ((void)__android_log_print(ANDROID_LOG_ERROR, "liyichang",__VA_ARGS__))
////add by liyichang
//#define _vsnprintf		vsnprintf
//#define _vsnwprintf		vswprintf
#define printf	LOGI


//#define wprintf(wfmt,...)	do{ \
//	char _fmt[1024] = {0};	\
//	mbstowcs((const char*)wfmt,_fmt,1024);\
//	(void)__android_log_print(ANDROID_LOG_INFO, "zhouhs", _fmt,__VA_ARGS__);\
//}while(0)
////end 

#endif

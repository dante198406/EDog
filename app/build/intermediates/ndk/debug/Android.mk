LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := tuzhi_edog
LOCAL_SRC_FILES := \
	C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\main\jni\Android.mk \
	C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\main\jni\Application.mk \
	C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\main\jni\EdogData.cpp \
	C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\main\jni\RadarData.cpp \
	C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\main\jni\search_camera_apk.cpp \

LOCAL_C_INCLUDES += C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\main\jni
LOCAL_C_INCLUDES += C:\Users\zhangzhaolei.EROBBING\AppData\Local\Android\StudioProjects\EDog\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)

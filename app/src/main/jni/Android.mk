LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)

#LOCAL_CFLAGS 	:= -fshort-wchar
LOCAL_CFLAGS += -UNDEBUG -D_DEBUG
LOCAL_MODULE    := tuzhi_edog
LOCAL_SRC_FILES := RadarData.cpp\
				EdogData.cpp\
				search_camera_apk.cpp\
				
LOCAL_LDLIBS    := -llog -landroid
LOCAL_SHARED_LIBRARIES := \
        libutils \
        libbinder \
        libhardware \
		
include $(BUILD_SHARED_LIBRARY)




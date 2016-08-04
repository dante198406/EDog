#APP_STL := gnustl_shared
#APP_CPPFLAGS += -fexceptions -frtti
# Build for all arch ndk supported
APP_ABI := armeabi

# enable rtti
# system stlport_static stlport_static_hard stlport_shared stlport_shared_hard gnustl_static gnustl_shared gabi++_static gabi++_shared libc++_static 
APP_STL := gnustl_static
#APP_STL := stlport_shared
#APP_STL := gnustl_shared
APP_OPTIM := debug
APP_CPPFLAGS += -frtti -Wno-psabi -fexceptions

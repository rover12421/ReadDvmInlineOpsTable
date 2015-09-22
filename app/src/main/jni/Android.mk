LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE     := readlibdvm
LOCAL_SRC_FILES  := read_dvmGetInlineOpsTable.cpp


#include $(BUILD_EXECUTABLE)
include $(BUILD_SHARED_LIBRARY)

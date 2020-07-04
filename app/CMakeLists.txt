# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

set(LIB_DIR ${CMAKE_CURRENT_LIST_DIR}/src/main/jniLibs/${ANDROID_ABI}/)

#add include dir
include_directories(src/main/jni/include)
include_directories(src/main/jni)

#add source files
aux_source_directory(src/main/jni SOURCE_FILES)
aux_source_directory(src/main/jni/src/core SOURCE_FILES_CORE)
aux_source_directory(src/main/jni/src/train SOURCE_FILES_TRAIN)
aux_source_directory(src/main/jni/src/util SOURCE_FILES_UTIL)
aux_source_directory(src/main/jni/thirdparty/LBP SOURCE_FILES_LBP)
aux_source_directory(src/main/jni/thirdparty/mser SOURCE_FILES_MSER)
aux_source_directory(src/main/jni/thirdparty/svm SOURCE_FILES_SVM)
aux_source_directory(src/main/jni/thirdparty/textDetect SOURCE_FILES_TEXTDETECT)
aux_source_directory(src/main/jni/thirdparty/xmlParser SOURCE_FILES_XML)
list(REMOVE_ITEM SOURCE_FILES main.cpp)
list(APPEND SOURCE_FILES ${SOURCE_FILES_CORE})
list(APPEND SOURCE_FILES ${SOURCE_FILES_SOURCE_FILES_SVM})
list(APPEND SOURCE_FILES ${SOURCE_FILES_CORE})
list(APPEND SOURCE_FILES ${SOURCE_FILES_TRAIN})
list(APPEND SOURCE_FILES ${SOURCE_FILES_UTIL})
list(APPEND SOURCE_FILES ${SOURCE_FILES_LBP})
list(APPEND SOURCE_FILES ${SOURCE_FILES_MSER})
list(APPEND SOURCE_FILES ${SOURCE_FILES_TEXTDETECT})
list(APPEND SOURCE_FILES ${SOURCE_FILES_XML})
list(APPEND SOURCE_FILES "src/main/jni/jni.cpp")

add_library(lib_opencv_java3 SHARED IMPORTED)
add_library(lib_freetype STATIC IMPORTED)
set_target_properties(lib_opencv_java3 PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/libopencv_java3.so)
set_target_properties(lib_freetype PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/libfreetype2-static.a)
add_library(mrcar SHARED ${SOURCE_FILES})

find_library(log-lib log)
target_link_libraries(mrcar lib_opencv_java3 ${log-lib} lib_freetype)
#ifndef MR_UTIL_H_
#define MR_UTIL_H_

#include <android/log.h>
#include "time.h"
#define LOG_TAG "MRCar"
#ifdef LOG_TAG
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__))
#else
#define LOGI(...)
#define LOGD(...)
#define LOGE(...)
#endif
template <typename T>
std::string to_string(T value)
{
    std::ostringstream os ;
    os << value ;
    return os.str() ;
}

#endif

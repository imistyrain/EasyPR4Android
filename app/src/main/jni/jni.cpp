#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/log.h>
#include "util.h"
#include "mrcar.h"

using namespace std;
using namespace cv;

#ifdef __cplusplus
extern "C" {
#endif

static const char* JNI_CLASS_NAME = "yanyu/com/mrcar/MRCar";

jint registerNativesMethods(JNIEnv *);

__attribute__((visibility("default")))
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    jint result = registerNativesMethods(env);
    if (result != JNI_OK) {
        return result;
    }
    return JNI_VERSION_1_6;
}
//得到签名方法
//javac MRCarUtil.java
//javap -s MRCarUtil
static JNINativeMethod sNativeMethods[] = {
        {"init","(Ljava/lang/String;)Z",(void*) init},
        {"plateRecognition", "(JJ)Ljava/lang/String;", (void*) plateRecognition},
        {"plateLive", "(J)Ljava/lang/String;", (void*) plateLive},
        {"plateNV21", "([BII)Ljava/lang/String;",(void*)plateNV21},
        {"release","()I",(void*)release},
};

jint registerNativesMethods(JNIEnv *env) {
    jclass clazz = env->FindClass(JNI_CLASS_NAME);
    if (clazz == NULL)
    {
        LOGE("Can't find class %s\n", JNI_CLASS_NAME);
        return -1;
    }
    jint result = env->RegisterNatives(
            clazz,
            sNativeMethods,
            std::extent<decltype(sNativeMethods)>::value);
    if (result!= JNI_OK)
    {
        LOGD("Failed registering methods for %s\n", JNI_CLASS_NAME);
        return -1;
    }
    return JNI_OK;
}

#ifdef __cplusplus
}
#endif
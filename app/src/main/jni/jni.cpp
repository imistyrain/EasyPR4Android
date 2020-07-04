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
//javac MRCar.java
//javap -s MRCar
static JNINativeMethod sNativeMethods[] = {
        {"init","(Ljava/lang/String;)Z",(void*) init},
        {"plateRecognition", "(JJ)[Lyanyu/com/mrcar/MRPlate;", (void*) plateRecognition},
        {"plateLive", "(J)[Lyanyu/com/mrcar/MRPlate;", (void*) plateLive},
        {"plateNV21", "([BII)[Lyanyu/com/mrcar/MRPlate;",(void*)plateNV21},
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
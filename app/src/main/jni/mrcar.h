#ifndef MRCAR_H
#define MRCAR_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jboolean JNICALL
init(JNIEnv *env, jclass type,jstring dir);

JNIEXPORT jobjectArray JNICALL
plateRecognition(JNIEnv *env, jclass type, jlong matImg, jlong matResult);

JNIEXPORT jobjectArray JNICALL
plateLive(JNIEnv *env, jclass type, jlong matImg);

JNIEXPORT jobjectArray JNICALL
plateNV21(JNIEnv *env, jclass type, jbyteArray img, jint height, jint width);

JNIEXPORT jint JNICALL
release(JNIEnv *env, jclass type);

#ifdef __cplusplus
}
#endif

#endif
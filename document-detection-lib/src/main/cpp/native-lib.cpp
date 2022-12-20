//
// Created by Sergio on 12/17/2022
//
#include <jni.h>
#include "document-detection.h"

extern "C"
JNIEXPORT jint JNICALL
Java_com_seramirezdev_lib_CameraTest_getNumber(JNIEnv *jniEn, jobject obj) {
    return 200;
}
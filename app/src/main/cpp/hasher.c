#include <stdlib.h>
#include <stdio.h>

#include "jni.h"
#include "cryptonight/hash-ops.h"

JNIEXPORT bool
JNICALL Java_com_turastory_shanghycon_MainActivity_slowHash(JNIEnv *env, jclass clazz,
															jbyteArray input, jbyteArray output, jint variant) {
	jbyte* inputBuffer = (*env)->GetByteArrayElements(env, input, NULL);
//	jbyte* outputBuffer = (*env)->GetByteArrayElements(env, output, NULL);

	jsize inputSize = (*env)->GetArrayLength(env, input);
//	jsize outputSize = (*env)->GetArrayLength(env, output);

//	if (outputSize < 32) {
//		jclass exception = (*env)->FindClass(env, "java/lang/Exception");
//
//		(*env)->ThrowNew(env, exception, "length of output array is less than 32 bytes");
//	}

//	memset(outputBuffer, 0, outputSize);
	bool value = cn_slow_hash(inputBuffer, inputSize - 8, NULL, (int)variant, NULL);

	(*env)->ReleaseByteArrayElements(env, input, (jbyte *)inputBuffer, JNI_ABORT);
//	(*env)->ReleaseByteArrayElements(env, output, (jbyte *)outputBuffer, JNI_COMMIT);

	return value;
}
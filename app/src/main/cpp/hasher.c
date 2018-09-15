#include <stdlib.h>
#include <stdio.h>

#include "jni.h"
#include "cryptonight/hash-ops.h"

JNIEXPORT bool
JNICALL Java_com_turastory_shanghycon_MainActivity_slowHash(JNIEnv *env, jclass clazz,
															jbyteArray input, jbyteArray target, jint variant) {
	jbyte* inputBuffer = (*env)->GetByteArrayElements(env, input, NULL);
	jbyte* targetBuffer = (*env)->GetByteArrayElements(env, target, NULL);

	jsize inputSize = (*env)->GetArrayLength(env, input);
	jsize outputSize = (*env)->GetArrayLength(env, target);

//	if (outputSize < 32) {
//		jclass exception = (*env)->FindClass(env, "java/lang/Exception");
//
//		(*env)->ThrowNew(env, exception, "length of output array is less than 32 bytes");
//	}

//	memset(outputBuffer, 0, outputSize);
	uint8_t* charInput = inputBuffer;
	unsigned long long int values[10];
	for (int i = 0; i < 9; i++) {
        values[i] = 0;
        for (int j = 0; j < 8; j++) {
            int nextIndex = i * 8 + j;
            unsigned long long int v = charInput[nextIndex];
            values[i] += (v * ((unsigned long long int)1 << (j * 8)));
        }
        printf("%d: %llx\n", i, values[i]);
	}

	values[9] = 0;
	for (int j = 0; j < 8; j++) {
		unsigned long long int v = targetBuffer[j];
		values[9] += (v * ((unsigned long long int)1 << (j * 8)));
	}

	bool value = cn_slow_hash(values[0], values[1], values[2], values[3], values[4], values[5],
							  values[6], values[7], values[8], values[9], (int)variant, NULL);

	(*env)->ReleaseByteArrayElements(env, input, (jbyte *)inputBuffer, JNI_ABORT);
//	(*env)->ReleaseByteArrayElements(env, output, (jbyte *)outputBuffer, JNI_COMMIT);

	return value;
}
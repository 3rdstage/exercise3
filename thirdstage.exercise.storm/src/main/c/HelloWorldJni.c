#include <jni.h>
#include <stdio.h>
#include "thirdstage_exercise_jni_case1_HelloWorldUsingJni.h"


JNIEXPORT void JNICALL
Java_thirdstage_exercise_jni_case1_HelloWorldUsingJni_print(JNIEnv *, jobject){
	printf("Hello World!\n");
	return;
}

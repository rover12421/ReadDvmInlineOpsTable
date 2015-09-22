#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <jni.h>
#include <string>

struct InlineOperation {
  const int*   	  func;               /* MUST be first entry */
  const char*     classDescriptor;
  const char*     methodName;
  const char*     methodSignature;
};

typedef int (*fn_dvmGetInlineOpsTableLength)();

//int main( int argc, const char* argv[] )
//{
//  const char* libdvmPath = "libdvm.so";
//  if (argc == 2) {
//    libdvmPath = argv[1];
//  }
//
//}

const char* getDvmInfo(const char* libdvmPath) {
    printf("libdvmPath : %s\n", libdvmPath);
    void *handle = dlopen(libdvmPath, RTLD_LAZY);
    if (!handle) {
        fprintf(stderr, "%s\n", dlerror());
        return "error!!!";
    }

    dlerror();    /* Clear any existing error */

    fn_dvmGetInlineOpsTableLength dvmGetInlineOpsTableLength = (fn_dvmGetInlineOpsTableLength)dlsym(handle, "_Z26dvmGetInlineOpsTableLengthv");
    //printf("dvmGetInlineOpsTableLength : %p\n", dvmGetInlineOpsTableLength);

    InlineOperation *gDvmInlineOpsTable = (InlineOperation*)dlsym(handle, "gDvmInlineOpsTable");
    //printf("gDvmInlineOpsTable : %p\n", gDvmInlineOpsTable);

    int len = 0;
    if (dvmGetInlineOpsTableLength == NULL) {
        len = 50;
        printf("dvmGetInlineOpsTableLength is null : len set to %d\n", 50);
    } else {
        len = dvmGetInlineOpsTableLength();
    }

    printf("gDvmInlineOpsTable len : %d\n", len);
    std::string str;
    for (int i=0; i<len; i++)
    {
        InlineOperation inlineOperation = gDvmInlineOpsTable[i];
        printf("%d : %s %s %s\n", i, inlineOperation.classDescriptor, inlineOperation.methodName, inlineOperation.methodSignature);
        str.append(inlineOperation.classDescriptor);
        str.append(",");
        str.append(inlineOperation.methodName);
        str.append(",");
        str.append(inlineOperation.methodSignature);
        str.append("\n");
    }

    char* writable = new char[str.size() + 1];
    std::copy(str.begin(), str.end(), writable);
    writable[str.size()] = '\0';

    return writable;
}

jstring ReadDvmInlineOpsTable(JNIEnv* env, jobject clazz) {
    const char* info = getDvmInfo("libdvm.so");
    jstring str = env->NewStringUTF(info);
    delete[] info;
    return str;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {

    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }

    JNINativeMethod gMethods[] = { { "ReadDvmInlineOpsTable", "()Ljava/lang/String;", (void*) ReadDvmInlineOpsTable } };
    jclass clazz = env->FindClass("com/rover12421/android/tool/odex/readdvminlineopstable/ReadDvmInfo");
    if (clazz == NULL) {
        return JNI_ERR;
    }
    if (env->RegisterNatives(clazz, gMethods,
                                sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_4;
}
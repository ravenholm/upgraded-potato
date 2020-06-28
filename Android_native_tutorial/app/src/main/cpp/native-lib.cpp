#include "opencv2/core.hpp"
#include <opencv2/imgproc.hpp>
#include <jni.h>
using namespace cv;
using namespace std;

extern "C"
// We are passing in a pointer to our image from the camera listener and that's in the form of a jlong
// Here we can just typecast to a Mat pointer from the jlong
JNIEXPORT void JNICALL Java_mynativetutorialpackage_NativeClass_testFunction(JNIEnv *env, jclass type, jlong addrRgba){
    //here we are converting the inbound jlong to a Mat
    Mat &img = *(Mat *) addrRgba;

    cvtColor(img, img, COLOR_RGB2GRAY);

}


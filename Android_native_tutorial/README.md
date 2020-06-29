# upgraded-potato
A baseline android application leveraging the power of native C/C++ development via CMake and OpenCV and eventually the DNN/Deep-learning models..

# Development Environment Setup
1. Download Android Development Studio
* Go to the link [Android 4.0 for Linux (About 865MB)](https://developer.android.com/studio)
* Move and unzip in your home folder
```
mv android-studio-ide-193.6514223-linux.tar.gz ~/
tar xvzf android-studio-ide-193.6514223-linux.tar.gz
cd android-studio-ide-193.6514223-linux/android-studio/bin/
sudo chmod 777 -R studio.sh
./studio.sh
```

* Unzip your gzip to the Home folder

2. Download and configure the OpenCV Android SDK

* Go to the link [OpenCV – 4.3.0](https://opencv.org/releases/)
* Click on Android and the download (About 227 MB) will start
* Move and unzip in your home folder 
```
mv opencv-4.3.0-android-sdk.zip ~/
unzip opencv-4.3.0-android-sdk.zip
```
3. Install/upgrade your Gradle build tools [Releases](https://gradle.org/releases/)

* Switch to the Android view
* Go to your **build.gradle** for your **module**  
```java
  // opencv 4.1.0
  implementation 'com.quickbirdstudios:opencv:4.1.0'
  // opencv 4.1.0 with contribution packages
  implementation 'com.quickbirdstudios:opencv:4.1.0-contrib'
```
**In the same build.gradle(:app), change the minimumSDK to 21 to comply with com.quickbirdstudios:opencv:4.1.0**
4. Pull OpenCV with NDK configuration
This section is taken from [this link](https://github.com/quickbirdstudios/opencv-android)

* Go to your **build.gradle** for your Project: <whatever your project name> 
```java
    dependencies {
        //Change the following to 
        // classpath "com.android.tools.build:gradle:4.0.0"
        classpath "com.android.tools.build:gradle:6.4.1"
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
```
* Now, sync your gradle

# Actual development

Open your MainActivity file and implement the following:
```java
    public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 
```
1. Now add/implement all the required methods (there are three methods)
2. Declare the following in your MainActivity class
```java
    private static final String TAG = "myTag";
    JavaCameraView javaCameraView;

    private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
    
                switch (status){
                    case LoaderCallbackInterface.SUCCESS:
                        javaCameraView.enableView();
                        Log.d(TAG, "callback: opencv loaded successfully");
                        break;
                    default:
                        Log.d(TAG, "could not load opencv");
    
    
                }
    
    
            }
        };
```
3. Override the OnCreate as follows:
```java
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);//this will still be red
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
```
4. Override the OnResume as follows:

```java
        @Override
        protected void onResume(){
            Log.d(TAG, "in On Resume");
            super.onResume();
    
            if (!OpenCVLoader.initDebug()){
                boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallBack);
    
                if (success){
                    Log.d(TAG, "Asynchronous initialisation succeeded");
                }
                else{
                    Log.d(TAG, "Asynchronous initialisation failed");
                }
            }
            else{
    
                Log.d(TAG, "OpenCV library found inside package");
                mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        }
```

5. Go to onCameraFrame now and change it as follows:

```java
        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            return inputFrame.rgba();
        }
```
# Now go to Android Manifest to set all the permissions and other parameters
```java
<uses-permission android:name="android.permission.CAMERA"/>
```

# Go to your activity_main.xml and add the following:
```java
    <org.opencv.android.JavaCameraView
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:id="@+id/java_camera_view"
        />
```
6. Finalisation before testing on an emulator

* Do a Gradle sync and if everything goes on perfectly then we are good. 
* Now do a build and then we are pretty-much ready to install our app

# Finally, prepare to talk to the natives (i.e. the C/C++ sourcecodes)
1. Switch to the Project view and go to the **app** directory
2. Create a cmake file **CMakeLists.txt** and add the following
```java
cmake_minimum_required(VERSION 3.10.2)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
include_directories(${CMAKE_SOURCE_DIR}/src/main/jniIncludes)

add_library(native-lib
            SHARED
            src/main/cpp/native-lib.cpp)
add_library(lib_opencv SHARED IMPORTED)
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java4.so)

find_library(log-lib
            log)

target_link_libraries(native-lib ${log-lib} lib_opencv)
```
3. Switch to the Android view
* Go to **/app/src/main** and create a folder called jniIncludes
* Copy **opencv** libraries from the OpenCVSDK 
** Go to where you have your OpenCVSDK 
** Go to **opencv-4.3.0-android-sdk/OpenCV-android-sdk/sdk/native/jni/include**
** Copy the opencv folder and paste it in your **jniIncludes** folder
* The above steps will help you with the autocomplete and include 

4. Now, return to your **Android view**
* Create a package and name it **mynativetutorialpackage** in your app/java
* Create a new Java class and name it "NativeClass"

5. Now, let's develop the C++ side of this work
* Switch back to your **Project** view
* In your src/main, create a directory and name is **cpp**
* In this directory, create a new C++ source file called **native-lib**
* Now link your CMakeList.txt file with Gradle
** Right click on your CMakeList.txt and select **Link C++ project with Gradle** 
** Browse all the way to where the CMakeLists.txt is present, select it and press OK
* Perform a gradle sync at this stage to check everything is fine and working. As per **peter**
* At this stage, after the sync, the CMakeLists.txt file should turn green indicating everything is setup properly

* Now, we can develop our **native-lib** C++ work with autocomplete and everything working
```objectivec
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
```
6. Linking our Java to the C library
* Go to your **MyActivity.java**
* Go to your ** BaseLoaderCallback** method and 
* Add the following to load your native library (native-lib)
```java
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        // Loading your native C library
                        System.loadLibrary("native-lib");
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
```
* Create a Mat object (Mat mRgba) in the above the **onCameraViewStarted** method as follows
```java
    //Mat is our image that has our pixel matrix information
    Mat mRgba;
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);//8bits/4channels
    }
```
*  Now, go to onCameraFrame and ALTER it to use the power of native C/C++
```objectivec
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.gray();
        //Get the address of mRgba as a pointer and pass it to the native C++ function
        NativeClass.testFunction(mRgba.getNativeObjAddr());
        return mRgba;
    }
```
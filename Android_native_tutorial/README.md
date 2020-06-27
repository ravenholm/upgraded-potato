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

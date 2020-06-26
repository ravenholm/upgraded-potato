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
3. Install Gradle's most recent release [Check from here](https://gradle.org/releases/)

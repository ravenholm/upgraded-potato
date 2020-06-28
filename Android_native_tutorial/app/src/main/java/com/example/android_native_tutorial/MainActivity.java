package com.example.android_native_tutorial;

// https://stackoverflow.com/questions/55835542/working-with-opencv-in-the-new-android-studio-3-4
// https://stackoverflow.com/questions/20666366/it-seems-that-your-device-does-not-support-cameraor-it-is-locked
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.dnn.Net;

import mynativetutorialpackage.NativeClass;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // First check android version
        //Check if permission is already granted
        //thisActivity is your activity. (e.g.: MainActivity.this)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Give first an explanation, if needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.java_camera_view);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        System.loadLibrary("native-lib");
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
    }

    //Mat is our image that has our pixel matrix information
    Mat mRgba;
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);//8bits/4channels
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        NativeClass.testFunction(mRgba.getNativeObjAddr());
        return mRgba;
    }

    @Override
    public void onResume(){
        super.onResume();
        try
        {
            if (!OpenCVLoader.initDebug()){
                Toast toast = Toast.makeText(getApplicationContext(), "There is a problem indeed", Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
            }

        }
        catch(Exception e)
        {
            String ttt = e.getMessage();
            int t = 9;
            //messageBox("doStuff", e.getMessage());
        }


    }

    @Override
    public void onPause(){
        super.onPause();
        if (cameraBridgeViewBase != null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (cameraBridgeViewBase != null){
            cameraBridgeViewBase.disableView();
        }
    }
}

package yanyu.com.mrcar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class CVCameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = "MRCar";
    private static final String sdcarddir = "/sdcard/mrcar";
    private int mCameraId = 0;
    private int numberOfCameras = 1;
    private boolean mlibLoaded=false;

    private Thread thread;
    private boolean killed = false;
    private boolean isMatready=false;
    private Mat mImg2Recog;
    private TextView tv;
    private Mat mRgba;
    private CameraBridgeViewBase mOpenCvCameraView;
    private BeepManager beepManager;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    new Thread() {
                        @Override
                        public void run() {
                            initFile();
                            System.loadLibrary("mrcar");
                            if(!mlibLoaded){
                                MRCar.init(sdcarddir);
                                mlibLoaded = true;
                            }
                        }
                    }.start();
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    private boolean switchCamera(){
        if(numberOfCameras >= 2){
            if(mOpenCvCameraView != null){
                mOpenCvCameraView.disableView();
            }
            mCameraId = mCameraId^1;
            mOpenCvCameraView.setCameraIndex(mCameraId);
            mOpenCvCameraView.enableView();
        } else{
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_cvcamera);
        mOpenCvCameraView = findViewById(R.id.mr_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCameraIndex(0);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setMaxFrameSize(640,480);
        mOpenCvCameraView.enableFpsMeter();
        mOpenCvCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchCamera();
            }
        });
        numberOfCameras = Camera.getNumberOfCameras();
        tv=findViewById(R.id.rstLic2);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(true);
    }
    private boolean isRequiredPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequiredPermissionsGranted() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS_CODE);
        }
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        final Mat[] tmp = new Mat[1];
        thread = new Thread() {
            @Override
            public void run() {

                while(!killed){
                    if(!isMatready){
                        continue;
                    }
                    synchronized (mImg2Recog) {
                        tmp[0] =mImg2Recog.clone();
                        isMatready = false;
                    }
                    if(mlibLoaded){
                        final String str=MRCar.plateLive(tmp[0].nativeObj);
                        Log.i(TAG,str);
                        if(str.length()>=6){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(str);
                                    beepManager.playBeepSoundAndVibrate();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv.setText("");
                                        }
                                    },3000);
                                }
                            });
                        }
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mOpenCvCameraView!=null)
            mOpenCvCameraView.disableView();
        beepManager.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mlibLoaded){
            MRCar.release();
            mlibLoaded=false;
        }
    }

    private void initFile(){
        MRAssetUtil.CopyAssets(this,"mrcar",sdcarddir);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Camera myCamera = ((JavaCameraView) mOpenCvCameraView).getCamera();
        Camera.Parameters mparameters = myCamera.getParameters();
        mparameters.setAutoExposureLock(false);
        mparameters.setAutoWhiteBalanceLock(false);
        mparameters.setPreviewFpsRange(0,5);
//            mparameters.setPreviewFormat(ImageFormat.JPEG);
        myCamera.setParameters(mparameters);
        mRgba = new Mat();
        mImg2Recog=mRgba.clone();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mImg2Recog.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        synchronized (mImg2Recog){
            mImg2Recog=mRgba.clone();
            isMatready=true;
        }
//        if(mlibLoaded){
//            MRCar.plateLive(mRgba.nativeObj);
//        }
        return mRgba;
    }
}
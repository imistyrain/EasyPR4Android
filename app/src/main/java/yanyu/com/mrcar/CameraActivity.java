package yanyu.com.mrcar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;


public class CameraActivity  extends Activity {
    private static final String TAG = "MRCar";
    private static final String sdcarddir = "/sdcard/mrcar";
    private TextView tv;
    private ImageView rv;
    @Nullable
    private Camera mCamera;
    private boolean mlibLoaded=false;
    private SurfaceHolder surfaceHolder;

    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    final int PREVIEW_WIDTH=640;
    final int PREVIEW_HEIGHT=480;
    private static final int PREVIEW_FORMAT = ImageFormat.NV21;
    private boolean killed = false;
    private byte nv21[];
    private boolean isNV21ready = false;
    private boolean bshowresult=false;
    private BeepManager beepManager;
    private ViewfinderView finderview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        tv=findViewById(R.id.rstLic);
        rv=findViewById(R.id.resultview);
        finderview=findViewById(R.id.finderview);
        SurfaceView cameraPreview = findViewById(R.id.surface_view);
        cameraPreview.getHolder().addCallback(new PreviewSurfaceCallback());
        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(true);
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.setVisibility(View.GONE);
                finderview.setVisibility(View.VISIBLE);
                tv.setText("");
                bshowresult=false;
            }
        });
    }
    private void setPreviewSurface(@Nullable SurfaceHolder previewSurface) {
        Camera camera = mCamera;
        if (camera != null && previewSurface != null) {
            try {
                camera.setPreviewDisplay(previewSurface);
                Log.d(TAG, "setPreviewSurface() called");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class PreviewSurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            surfaceHolder=holder;
            setPreviewSize(width,height);
            setPreviewSurface(holder);
            startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPreview();
        }
    }
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
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
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
        mCamera=Camera.open(0);
        killed = false;
        final byte[] tmp = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                while (!killed) {
                    if(!isNV21ready)
                        continue;
                    if(bshowresult)
                        continue;
                    synchronized (nv21) {
                        System.arraycopy(nv21, 0, tmp, 0, nv21.length);
                        isNV21ready = false;
                    }
                    if(mlibLoaded)
                    {
                        final String str=MRCar.plateNV21(tmp,PREVIEW_HEIGHT,PREVIEW_WIDTH);
                        Log.i(TAG,str);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(str.length()>=6){
                                    bshowresult=true;
                                    tv.setText(str);
                                    beepManager.playBeepSoundAndVibrate();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            tv.setText("");
//                                        }
//                                    },3000);
                                    Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
                                    YuvImage image = new YuvImage(tmp, ImageFormat.NV21, previewSize.width, previewSize.height, null);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    image.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, stream);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                                    Matrix m=new Matrix();
                                    m.setRotate(90, bitmap.getWidth()/2, bitmap.getHeight()/2);
                                    Bitmap bmp2=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                                    bitmap.recycle();
                                    rv.setImageBitmap(bmp2);
                                    rv.setVisibility(View.VISIBLE);
                                    finderview.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            }
        };
        thread.start();

    }
    private void closeCamera() {
        Camera camera = mCamera;
        mCamera = null;
        if (camera != null) {
            camera.release();
        }
    }
    protected void onPause() {
        super.onPause();
        closeCamera();
        beepManager.close();
    }
    private void setPreviewSize(int shortSide, int longSide) {
        Camera camera = mCamera;
        if (camera != null && shortSide != 0 && longSide != 0) {
            Camera.Parameters parameters = camera.getParameters();
            int frameWidth = PREVIEW_WIDTH;
            int frameHeight = PREVIEW_HEIGHT;
            int previewFormat = parameters.getPreviewFormat();
            parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            parameters.setPreviewFormat(PREVIEW_FORMAT);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);
            PixelFormat pixelFormat = new PixelFormat();
            PixelFormat.getPixelFormatInfo(previewFormat, pixelFormat);
            int bufferSize = (frameWidth * frameHeight * pixelFormat.bitsPerPixel) / 8;
            camera.addCallbackBuffer(new byte[bufferSize]);
            camera.addCallbackBuffer(new byte[bufferSize]);
            camera.addCallbackBuffer(new byte[bufferSize]);
            Log.d(TAG, "Add three callback buffers with size: " + bufferSize);
        }
    }
    private class PreviewCallback implements Camera.PreviewCallback {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            synchronized (nv21) {
                System.arraycopy(data, 0, nv21, 0, data.length);
                isNV21ready = true;
            }
            camera.addCallbackBuffer(data);
        }
    }
    private void startPreview() {
        Camera camera = mCamera;
        mCamera.setDisplayOrientation(90);
        SurfaceHolder previewSurface = surfaceHolder;
        if (camera != null && previewSurface != null) {
            camera.setPreviewCallbackWithBuffer(new PreviewCallback());
            camera.startPreview();
            Log.d(TAG, "startPreview() called");
        }
    }
    private void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            Log.d(TAG, "stopPreview() called");
        }
    }
    private void initFile(){
        MRAssetUtil.CopyAssets(this,"mrcar",sdcarddir);
    }
}

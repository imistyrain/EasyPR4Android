package yanyu.com.mrcar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class PhotoActivity extends Activity {
    private static final String TAG = "MRCar";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    private static final String sdcarddir="/sdcard/mrcar";
    private static final String initimgPath="test.jpg";

    private Bitmap Originbitmap;
    private Bitmap bmp;
    private ImageView im;
    private ImageButton buttonCamera;
    private ImageButton buttonFolder;
    private EditText et;

    private boolean b2Recognition=true;
    private Uri fileUri;
    private static String filePath=null;
    private class plateTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            doRecognition();
            return null;
        }
    }
    private void doRecognition(){
        Mat m = new Mat();
        bmp=Originbitmap.copy(Bitmap.Config.RGB_565,true);
        Utils.bitmapToMat(bmp, m);
        try {
            final String license=MRCar.plateRecognition(m.getNativeObjAddr(), m.getNativeObjAddr());
            Utils.matToBitmap(m, bmp);
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  et.setText(license);
                                  im.setImageBitmap(bmp);
                              }
                          }
            );
        }
        catch (Exception e)
        {
            Log.d(TAG,"exception occured!");
        }
    }
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initFile();
                            System.loadLibrary("mrcar");
                            MRCar.init(sdcarddir);
                            doRecognition();
                        }
                    }).start();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        im=(ImageView)findViewById(R.id.imageView);
        et=(EditText)findViewById(R.id.editText);

        buttonCamera=(ImageButton)findViewById(R.id.buttonCamera);
        buttonFolder=(ImageButton)findViewById(R.id.buttonFolder);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = MRMediaFileUtil.getOutputMediaFileUri(1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        buttonFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b2Recognition) {
                    if (Originbitmap != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                doRecognition();
                            }
                        }).start();
                    }
                }
                else {
                    im.setImageBitmap(Originbitmap);
                    et.setText(""); }
                b2Recognition=!b2Recognition;
            }
        });
        initBitmap();
    }
    private void initBitmap(){
        if(filePath!=null){
            Originbitmap=BitmapFactory.decodeFile(filePath);
        } else{
            Originbitmap=BitmapFactory.decodeFile(sdcarddir+"/"+ initimgPath);
        }
        im.setImageBitmap(Originbitmap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MRCar.release();
    }

    public static Bitmap loadBitmap(ImageView im, String filepath){
        int width = im.getWidth();
        int height = im.getHeight();
        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, factoryOptions);
        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;
        int scaleFactor = Math.min(imageWidth / width, imageHeight / height);
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;
        Bitmap bmp = BitmapFactory.decodeFile(filepath, factoryOptions);
        im.setImageBitmap(bmp);
        return bmp;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    if (data.hasExtra("data")) {
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        im.setImageBitmap(thumbnail); }
                } else {
                    filePath=fileUri.getPath(); }
            }
        }
        else if(requestCode ==SELECT_IMAGE_ACTIVITY_REQUEST_CODE&& resultCode == RESULT_OK && null != data) {
            fileUri = data.getData();
            filePath=MRMediaFileUtil.getPath(this,fileUri);
        }
        initBitmap();
    }

    private void initFile(){
        MRAssetUtil.CopyAssets(this,"mrcar",sdcarddir);
    }
}
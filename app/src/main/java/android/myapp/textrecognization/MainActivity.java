package android.myapp.textrecognization;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private TextView mTextView;
    private Button mButton;
    private Button mRecognize;
    private String mDetectedText = "";

    private String[] mSubjects;

    private List<String> mTextList;

     TextRecognizer mTextRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCameraView = findViewById(R.id.surface_camera_preview);
        mTextView = findViewById(R.id.tv_result);
        //Make TextView scrollable
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        //Start camera to detect the text
        startCameraSource();

        mButton = findViewById(R.id.button_detect_text);

       mRecognize = findViewById(R.id.button_recognize_text);
       mRecognize.setVisibility(View.INVISIBLE);


        mSubjects = getSubjectList();

        mTextList = new ArrayList<String>(Arrays.asList(mSubjects));


    }
    private void startCameraSource() {

        //Create the TextRecognizer
       // final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        mTextRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!mTextRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), mTextRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSION_REQUEST_CAMERA);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
//            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
//                @Override
//                public void release() {
//                }
//
//                /**
//                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
//                 * which will then be set to the textView.
//                 * */
//                @Override
//                public void receiveDetections(Detector.Detections<TextBlock> detections) {
//                    final SparseArray<TextBlock> items = detections.getDetectedItems();
//                    if (items.size() != 0 ){
//
//                        StringBuilder stringBuilder = new StringBuilder();
//                        for(int i=0;i<items.size();i++){
//                            TextBlock item = items.valueAt(i);
//
//                            String s = item.getValue();
//
//                           // Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT).show();
//
//                            if(mList.contains(s))
//                            {
//                              //  Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT).show();
//                                stringBuilder.append(s);
//                                stringBuilder.append("\n");
//
//                            }
//                        }
//                        mDetectedText = stringBuilder.toString();
//
////                        mTextView.post(new Runnable() {
////                            @Override
////                            public void run() {
////                                StringBuilder stringBuilder = new StringBuilder();
////                                for(int i=0;i<items.size();i++){
////                                    TextBlock item = items.valueAt(i);
////
////                                    String s = item.getValue();
////
////                                    if(mList.contains(s))
////                                    {
////                                        Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT).show();
////                                        stringBuilder.append(s);
////                                        stringBuilder.append("\n");
////
////                                    }
////
////
////                                }
////
////                                mDetectedText = stringBuilder.toString();
////                            }
////                        });
//                    }
//                }
//            });
        }
    }
    public String[] getSubjectList(){
        Resources res = getResources();
        String[] subjects = res.getStringArray(R.array.subject_names);

        return subjects;
    }

    public void detect(){


        mTextRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            /**
             * Detect all the text from camera using TextBlock and the values into a stringBuilder
             * which will then be set to the textView.
             * */
            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0 ){

                    mTextView.post(new Runnable() {
                                       @Override

                               public void run() {
                                   StringBuilder stringBuilder = new StringBuilder();
                                   mTextList = new ArrayList<String>();
                                   for (int i = 0; i < items.size(); i++) {
                                       TextBlock item = items.valueAt(i);

                                       String s = item.getValue();
                                       mTextList.add(s);

                                   // Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT).show();


                                   //  {
                                   //
                                   //stringBuilder.append(s);
                                   //stringBuilder.append("\n");

                                               // }
                                           }
                                           mRecognize.setVisibility(View.VISIBLE);
                                       }
                                   });
                    //mDetectedText = stringBuilder.toString();


                }

            }
        });
    }
    public void detectText(View view){
        detect();

        //mTextView.setText(mDetectedText);

        //Intent intent = new Intent(MainActivity.this,DisplayCourseDetailsActivity.class);
//                intent.putExtra("course_names",mDetectedText);
//                startActivity(intent);



    }
    public void recognizeText(View view){
        if(mTextList != null) {
            for (String s : mSubjects) {
                if (mTextList.contains(s))
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
            mRecognize.setVisibility(View.INVISIBLE);
        }

    }

}

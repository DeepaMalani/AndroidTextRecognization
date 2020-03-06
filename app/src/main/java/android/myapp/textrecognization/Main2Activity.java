package android.myapp.textrecognization;

import android.Manifest;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {

    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private TextView mTextView;
    private Button mButton;

    private String mDetectedText = "";

    private String[] mSubjects;

    private List<String> mTextList;

    private TextRecognizer mTextRecognizer;

    private Set<String> mDetectedMatchedSubjects = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mCameraView = findViewById(R.id.surface_camera_preview);
        mTextView = findViewById(R.id.tv_result);
        //Make TextView scrollable
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        //Start camera to detect the text
        startCameraSource();

        mButton = findViewById(R.id.button_detect_text);


        mSubjects = getSubjectList();

        mTextList = new ArrayList<String>(Arrays.asList(mSubjects));
    }

    public String[] getSubjectList() {
        Resources res = getResources();
        String[] subjects = res.getStringArray(R.array.subject_names);

        return subjects;
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

                            ActivityCompat.requestPermissions(Main2Activity.this,
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
        }
    }

    private List<Text> filterInvalidDetections(Detector.Detections<TextBlock> items) {
        List<Text> result = new ArrayList<>();
        SparseArray<TextBlock> detectedItems = items.getDetectedItems();

        for (int i = 0; i < detectedItems.size(); ++i) {
            TextBlock textBlock = detectedItems.valueAt(i);

            // Get sub-components and extract only lines
            List<? extends Text> components = textBlock.getComponents();

            for (Text component : components) {
                String value = component.getValue();
                if (component instanceof Line
                        && value != null
                        && !value.isEmpty()
                        && mTextList.contains(value)) {
                    result.add(component);
                } else {
                    Log.d(TAG, "filterInvalidDetections: sub-component is not a Line, should we go deeper?");
                }
            }
        }
        return result;
    }

    public void detectText(View view) {


        mTextRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

                mDetectedMatchedSubjects = null;
            }

            /**
             * Detect all the text from camera using TextBlock and the values into a stringBuilder
             * which will then be set to the textView.
             */
            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

                List<Text> list = filterInvalidDetections(detections);

                for(Text text : list){

                    String s = text.getValue();
                    mDetectedMatchedSubjects.add(s);
                    Log.d("Found Text",s);
                }
//                mTextView.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Toast.makeText(Main2Activity.this,String.valueOf(mDetectedMatchedSubjects),Toast.LENGTH_SHORT).show();
//                    }
//                });



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(Main2Activity.this,String.valueOf(mDetectedMatchedSubjects),Toast.LENGTH_SHORT).show();

                        // mTextView.setText(String.valueOf(mDetectedMatchedSubjects));

                        Intent intent = new Intent(Main2Activity.this,DisplayCourseDetailsActivity.class);
                        intent.putExtra("course_names",String.valueOf(mDetectedMatchedSubjects));
                        startActivity(intent);
                    }
                });
                //Log.d("Total detected subjects",String.valueOf(mDetectedMatchedSubjects.size()));
            }
        });

//        for(String s : mDetectedMatchedSubjects){
//            Toast.makeText(Main2Activity.this,s,Toast.LENGTH_SHORT).show();
//        }
    }

    public void display(){

        mTextView.setText(String.valueOf(mDetectedMatchedSubjects));

    }
}

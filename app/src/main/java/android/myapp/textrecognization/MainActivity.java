package android.myapp.textrecognization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.myapp.textrecognization.adapter.Subject;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CAMERA = 100;


    private ArrayList<String> mDetectedTextList;


    private String[] mSubjects;

    private List<String> mPrerequisiteSubjects;

     private boolean flag = false;


    private HashMap<Integer,String> mapSubs;
    private HashMap<Integer,String> mapGrades;

    private ArrayList<Subject> mLstSubjects;

   FloatingActionButton fabDetectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.surface_camera_preview);
        fabDetectText = findViewById(R.id.fab_detect);

        flag = false;
        //Get prerequisite List
        mSubjects = getSubjectList();
        mPrerequisiteSubjects = new ArrayList<String>(Arrays.asList(mSubjects));

        fabDetectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectText();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Start camera to detect the text
        startCameraSource();
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
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

                        if(mCameraSource != null)
                        mCameraSource.start(mCameraView.getHolder());

                    } catch (IOException e) {
                        //.printStackTrace();
                        Toast.makeText(MainActivity.this,"Unable to access camera..Please try again",Toast.LENGTH_SHORT).show();
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
//                    if (mCameraSource != null) {
//                        mCameraSource.release();
//                        mCameraSource = null;
//                        flag = false;
//
//                    }
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                /**
                 * Detect all the text from camera.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();

                    getItems(items);
                    flag = true;
                }

            });


        }
    }
    public void detectText(){

        if(flag) {

            mLstSubjects = new ArrayList<Subject>();

            for(Integer i : mapSubs.keySet()){

                  if(mapGrades.get(i) != null){
                      String subjectName = mapSubs.get(i);
                      String subjectGrade = mapGrades.get(i);

                      Subject sub = new Subject(subjectName,subjectGrade);
                      mLstSubjects.add(sub);

                  }
              }

            if(mLstSubjects != null && mLstSubjects.size() > 0) {

                Intent intent = new Intent(MainActivity.this, DisplayCourseDetailsActivity.class);
                intent.putParcelableArrayListExtra("subjects", mLstSubjects);
                startActivity(intent);

            }
            else
                Toast.makeText(MainActivity.this, "Unable to recognize courses,Please try Again", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(MainActivity.this, "Text not detected,Please Click Again", Toast.LENGTH_SHORT).show();

        }
    }
    private void  getItems(SparseArray<TextBlock> items) {

        if (flag) {

            mapSubs = new HashMap<>();
            mapGrades = new HashMap<>();

            if (items.size() != 0) {

                for (int i = 0; i < items.size(); i++) {

                    TextBlock item = items.valueAt(i);

                    List<? extends Text> components = item.getComponents();

                    for (int j = 0; j < components.size(); j++) {

                        Text component = components.get(j);

                        String value = component.getValue();

                        if (mPrerequisiteSubjects.contains(value.toLowerCase())) {
                            mapSubs.put(j,value);
                        }
                        if(value.equals("A+")|| value.equals("A") || value.equals("B") || value.equals("B+") || value.equals("C") || value.equals("C+"))
                            mapGrades.put(j,value);
                    }
                }
                flag = true;

            }


        }
    }
    public String[] getSubjectList(){
        Resources res = getResources();
        String[] subjects = res.getStringArray(R.array.subject_names);
        return subjects;
    }



}

package android.myapp.textrecognization;

import android.content.Intent;
import android.myapp.textrecognization.adapter.Subject;
import android.myapp.textrecognization.adapter.SubjectsAdapter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayCourseDetailsActivity extends AppCompatActivity {




    private RecyclerView mRecyclerView;
    private SubjectsAdapter mSubjectsAdapter;

    private ArrayList<Subject> mSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_course_details);

        mRecyclerView = findViewById(R.id.recycler_view_subjects);




//        if(intent.hasExtra("course_names")){
//
//            mPrerequisiteSubjects = intent.getStringArrayListExtra("course_names");
//            if (mPrerequisiteSubjects != null){
//                mSubjectsAdapter = new SubjectsAdapter(DisplayCourseDetailsActivity.this,mPrerequisiteSubjects);
//                mRecyclerView.setAdapter(mSubjectsAdapter);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            }
//        }
        Intent intent = getIntent();
        if(intent.hasExtra("subjects")) {

            mSubjects = intent.getParcelableArrayListExtra("subjects");

            if(mSubjects != null) {
            //    for (Subject subject : mSubjects) {
//                    String s = subject.getName() + " " + subject.getGrade();
//                    Toast.makeText(DisplayCourseDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
//                }

                mSubjectsAdapter = new SubjectsAdapter(DisplayCourseDetailsActivity.this,mSubjects);
                mRecyclerView.setAdapter(mSubjectsAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

        }

    }



}

package android.myapp.textrecognization;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayCourseDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_course_details);

        Intent intent = getIntent();

        if(intent.hasExtra("course_names")){
            Toast.makeText(DisplayCourseDetailsActivity.this,intent.getStringExtra("course_names"),Toast.LENGTH_SHORT).show();
        }
    }
}

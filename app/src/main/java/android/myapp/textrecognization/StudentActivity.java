package android.myapp.textrecognization;

import android.myapp.textrecognization.database.Student;
import android.myapp.textrecognization.database.StudentDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentActivity extends AppCompatActivity {

    private StudentDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        mDatabase = StudentDatabase.getsInstance(this);
    }
    public void saveStudentDetails(View view){

        Student student = new Student("Deepa","XYZ","A");
        mDatabase.studentDaoDao().insertStudent(student);

        int totalRecords = mDatabase.studentDaoDao().getTotalStudent();

        Toast.makeText(StudentActivity.this,String.valueOf(totalRecords),Toast.LENGTH_SHORT).show();
    }
}

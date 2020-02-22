package android.myapp.textrecognization.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_table")
public class Student {

    @PrimaryKey(autoGenerate = true)
    long studentId;

    @ColumnInfo(name = "student_name")
    String studentName;

    @ColumnInfo(name = "subject_name")
    String subjectName;

    @ColumnInfo(name = "subject_grade")
    String subjectGrade;

    public Student(String studentName,String subjectName,String subjectGrade){
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.subjectGrade = subjectGrade;
    }
}

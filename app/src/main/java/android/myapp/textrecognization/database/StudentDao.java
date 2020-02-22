package android.myapp.textrecognization.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StudentDao {

 @Insert
 public void insertStudent(Student student);

 @Query("select * from student_table where student_name = :name")
 public Student getStudentDetails(String name);

 @Query("Select count(*) from student_table")
 public int getTotalStudent();
}

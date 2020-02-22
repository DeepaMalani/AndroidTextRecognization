package android.myapp.textrecognization.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class}, version = 1)
public abstract class StudentDatabase extends RoomDatabase {

    public abstract StudentDao studentDaoDao();

    private static final String LOG_TAG = StudentDatabase.class.getSimpleName();
    private static final String DATA_BASE_NAME = "student_records";
    private static final Object LOCK = new Object();
    private static StudentDatabase sInstance;

    public static StudentDatabase getsInstance(Context context)
    {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                Log.d(LOG_TAG,"Creating database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        StudentDatabase.class,DATA_BASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG,"getting the database");
        return sInstance;
    }
}

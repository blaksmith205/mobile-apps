package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;

@Database(entities = {MessageDataEntry.class}, version = 1, exportSchema = false)
public abstract class DatabaseManager extends RoomDatabase {
    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "get_out_of_it";
    private static DatabaseManager sInstance;


    public static DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DatabaseManager.class, DatabaseManager.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        if (BuildConfig.DEBUG) Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MessageDataDao messageDataDao();
}

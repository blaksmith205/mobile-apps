package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.BuildConfig;
import edu.floridapoly.mobiledeviceapps.spring20.getoutofit.helpers.AppExecutors;

@Database(entities = {MessageDataEntry.class, TextAlarmEntry.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
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

    public abstract TextAlarmDao textAlarmDao();

    /**
     * Inserts or Updates the MessageDataEntry.
     * <p>
     * Update will happen only if the newSummary and newMessage are different from the message
     * stored inside the messageData
     *
     * @param messageData The MessageDataEntry instance. Insertion happens if null
     * @param newSummary  New summary to update the messageData with
     * @param newMessage  New message to update the messageData with
     */
    public MessageDataEntry insertOrUpdateMessage(MessageDataEntry messageData, String newSummary, String newMessage) {
        if (messageData == null) {
            // Insert into database
            final MessageDataEntry dataEntry = new MessageDataEntry(newSummary, newMessage);
            AppExecutors.getInstance().diskIO().execute(() -> sInstance.messageDataDao().insert(dataEntry));
            return dataEntry;
        } else {
            // Only update if user changed text
            if (messageData.getSummary().equals(newSummary) && messageData.getMessage().equals(newMessage)) {
                return messageData; // Don't do anything
            }
            // Update database
            messageData.setSummary(newSummary);
            messageData.setMessage(newMessage);
            AppExecutors.getInstance().diskIO().execute(() -> sInstance.messageDataDao().update(messageData));
            return messageData;
        }
    }
}

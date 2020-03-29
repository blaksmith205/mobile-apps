package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Date;

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
     * @return Created or Updated MessageDataEntry
     */
    public MessageDataEntry insertOrUpdateMessage(MessageDataEntry messageData, String newSummary, String newMessage) {
        if (messageData == null) {
            // Insert into database
            MessageDataEntry dataEntry = new MessageDataEntry(newSummary, newMessage);
            AppExecutors.getInstance().diskIO().execute(() -> {
                long id = sInstance.messageDataDao().insert(dataEntry);
                dataEntry.setMessageId((int) id);
            });
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

    /**
     * Inserts or Updates the MessageDataEntry.
     * <p>
     * Update will happen only if the newDate, newSenderInfo and newMessageData are different from
     * information stored inside the textAlarm
     *
     * @param textAlarm      The TextAlarmEntry instance. Insertion happens if null
     * @param newDate        New Date to update the textAlarm with
     * @param newSenderInfo  New Sender information to update the textAlarm with
     * @param newMessageData New MessageDataEntry to update the textAlarm with
     * @return Created or Updated TextAlarmEntry
     */
    public TextAlarmEntry insertOrUpdateAlarm(TextAlarmEntry textAlarm, Date newDate, String newSenderInfo, @NonNull MessageDataEntry newMessageData) {
        if (textAlarm == null) {
            // Insert into database
            final TextAlarmEntry newAlarm = new TextAlarmEntry(newDate, newSenderInfo, newMessageData);
            AppExecutors.getInstance().diskIO().execute(() -> sInstance.textAlarmDao().insert(newAlarm));
            return newAlarm;
        } else {
            // Only update if user changed information
            boolean noChanges = textAlarm.getDateTime().equals(newDate) && textAlarm.getSenderInfo().equals(newSenderInfo) && newMessageData.equals(textAlarm.getMessageData());
            if (noChanges) {
                return textAlarm; // Don't do anything
            }
            // Update database
            textAlarm.setDateTime(newDate);
            textAlarm.setSenderInfo(newSenderInfo);
            textAlarm.setMessageData(newMessageData);
            AppExecutors.getInstance().diskIO().execute(() -> sInstance.textAlarmDao().update(textAlarm));
            return textAlarm;
        }
    }
}

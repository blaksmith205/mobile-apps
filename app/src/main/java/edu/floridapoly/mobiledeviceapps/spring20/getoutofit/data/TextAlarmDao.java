package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TextAlarmDao {

    @Transaction
    @Query("SELECT * FROM text_alarms ORDER BY dateTime ASC")
    LiveData<List<TextAlarmEntry>> loadTextAlarms();

    @Transaction
    @Query("SELECT * FROM text_alarms WHERE alarmId = :id")
    TextAlarmEntry loadById(int id);

    @Insert
    long insert(TextAlarmEntry textAlarm);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TextAlarmEntry textAlarm);

    @Delete
    void delete(TextAlarmEntry textAlarm);
}

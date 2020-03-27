package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TextAlarmDao {
    @Query("SELECT * FROM text_alarms")
    LiveData<List<TextAlarmEntry>> loadTextAlarms();

    @Query("SELECT * FROM text_alarms WHERE textAlarmId = :id")
    TextAlarmEntry loadById(int id);

    @Insert
    void insert(TextAlarmEntry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TextAlarmEntry entry);

    @Delete
    void delete(TextAlarmEntry entry);
}

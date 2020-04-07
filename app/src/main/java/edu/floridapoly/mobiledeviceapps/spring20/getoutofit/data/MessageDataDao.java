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
public interface MessageDataDao {

    // Obtain the messages with the templates first, then sorted alphabetically by summary
    @Query("SELECT * FROM message_data ORDER BY template DESC, summary ASC")
    LiveData<List<MessageDataEntry>> loadMessages();

    @Query("SELECT * FROM message_data WHERE messageId = :id")
    MessageDataEntry loadDataById(int id);

    @Insert
    long insert(MessageDataEntry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(MessageDataEntry entry);

    @Delete
    void delete(MessageDataEntry entry);
}

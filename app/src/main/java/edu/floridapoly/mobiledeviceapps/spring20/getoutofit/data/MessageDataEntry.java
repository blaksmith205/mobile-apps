package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_data")
public class MessageDataEntry {
    // Unique ID of the entry
    @PrimaryKey(autoGenerate = true)
    private int id;
    // User entered summary
    private String summary;
    // User entered message
    private String message;

    @Ignore
    public MessageDataEntry(String summary, String message) {
        this.summary = summary;
        this.message = message;
        id = -1;
    }

    public MessageDataEntry(String summary, String message, int id) {
        this.summary = summary;
        this.message = message;
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

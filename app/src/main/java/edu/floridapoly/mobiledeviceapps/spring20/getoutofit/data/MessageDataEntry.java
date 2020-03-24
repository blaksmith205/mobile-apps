package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "message_data")
public class MessageDataEntry {

    private String summary;
    private String message;
    private int id;

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

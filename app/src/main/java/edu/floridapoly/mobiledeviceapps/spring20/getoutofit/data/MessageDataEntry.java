package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_data")
public class MessageDataEntry {
    // Unique ID of the entry
    @PrimaryKey(autoGenerate = true)
    private int messageId;
    // User entered summary
    private String summary;
    // User entered message
    private String message;

    @Ignore
    public MessageDataEntry(String summary, String message) {
        this.summary = summary;
        this.message = message;
    }

    public MessageDataEntry(int messageId, String summary, String message) {
        this.messageId = messageId;
        this.summary = summary;
        this.message = message;
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

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}

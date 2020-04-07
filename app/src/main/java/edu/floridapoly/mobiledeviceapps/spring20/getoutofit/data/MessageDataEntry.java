package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    // If the message is a template or not
    private boolean template;

    @Ignore
    public MessageDataEntry(String summary, String message) {
        this.summary = summary;
        this.message = message;
    }

    @Ignore
    public MessageDataEntry(String summary, String message, boolean template) {
        this.summary = summary;
        this.message = message;
        this.template = template;
    }

    public MessageDataEntry(int messageId, String summary, String message, boolean template) {
        this.messageId = messageId;
        this.summary = summary;
        this.message = message;
        this.template = template;
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

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    @NonNull
    @Override
    public String toString() {
        return summary;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof MessageDataEntry))
            return false;
        MessageDataEntry other = (MessageDataEntry) obj;
        return (other.getMessageId() == messageId) && (other.getSummary().equals(summary)) &&
                (other.getMessage().equals(message)) && (other.isTemplate() == template);
    }
}

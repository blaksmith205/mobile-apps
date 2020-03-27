package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "text_alarms")
public class TextAlarmEntry {

    @PrimaryKey(autoGenerate = true)
    private int textAlarmId;
    private Date dateTime;
    private String from;
    private int messageId;
    @Ignore
    private MessageDataEntry data;

    @Ignore
    public TextAlarmEntry(Date dateTime, String from, MessageDataEntry data) {
        this.dateTime = dateTime;
        this.from = from;
        this.data = data;
    }

    public TextAlarmEntry(int textAlarmId, Date dateTime, String from, int messageId) {
        this.dateTime = dateTime;
        this.from = from;
        this.textAlarmId = textAlarmId;
        this.messageId = messageId;
        // TODO: link the message from database using proper message id
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getTextAlarmId() {
        return textAlarmId;
    }

    public void setTextAlarmId(int textAlarmId) {
        this.textAlarmId = textAlarmId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public MessageDataEntry getData() {
        return data;
    }

    public void setData(MessageDataEntry data) {
        this.data = data;
    }
}

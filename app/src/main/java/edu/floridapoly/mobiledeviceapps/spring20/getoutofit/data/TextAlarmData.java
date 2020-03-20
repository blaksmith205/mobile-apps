package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import java.util.Date;

public class TextAlarmData {

    private Date date;
    private String time;
    private String from;
    private MessageData message;
    private int id;

    public TextAlarmData(Date date, String time, String from, MessageData message) {
        this.date = date;
        this.time = time;
        this.from = from;
        this.message = message;
        id = -1;
    }

    // TODO: Set id as the id from the database
    public TextAlarmData(Date date, String time, String from, String summary, String message, int id) {
        this.date = date;
        this.time = time;
        this.from = from;
        // TODO: link the message from database using proper message id
        this.message = new MessageData(summary, message);
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public MessageData getMessage() {
        return message;
    }

    public void setMessage(MessageData message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

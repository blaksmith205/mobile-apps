package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import java.util.Date;

public class TextAlarmDataEntry {

    private Date date;
    private String time;
    private String from;
    private MessageDataEntry data;
    private int id;

    public TextAlarmDataEntry(Date date, String time, String from, MessageDataEntry data) {
        this.date = date;
        this.time = time;
        this.from = from;
        this.data = data;
        id = -1;
    }

    // TODO: Set id as the id from the database
    public TextAlarmDataEntry(Date date, String time, String from, String summary, String data, int id) {
        this.date = date;
        this.time = time;
        this.from = from;
        // TODO: link the message from database using proper message id
        this.data = new MessageDataEntry(summary, data);
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

    public MessageDataEntry getData() {
        return data;
    }

    public void setData(MessageDataEntry data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

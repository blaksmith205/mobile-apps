package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import java.util.Date;

public class TextAlarmData {

    private Date date;
    private String time;
    private String from;
    private String summary;
    private String message;
    private int id;

    // TODO: Set id as the id from the database
    public TextAlarmData(Date date, String time, String from, String summary, String message, int id) {
        this.date = date;
        this.time = time;
        this.from = from;
        this.summary = summary;
        this.message = message;
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

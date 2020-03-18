package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import java.util.Date;

public class TextAlarmData {

    private Date date;
    private String time;
    private String from;
    private String summary;
    private String message;

    public TextAlarmData(Date date, String time, String from, String summary, String message) {
        this.date = date;
        this.time = time;
        this.from = from;
        this.summary = summary;
        this.message = message;
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
}

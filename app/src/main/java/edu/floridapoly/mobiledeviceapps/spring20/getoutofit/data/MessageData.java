package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

public class MessageData {

    private String summary;
    private String message;
    private int id;

    public MessageData(String summary, String message) {
        this.summary = summary;
        this.message = message;
        id = -1;
    }

    // TODO: set the id from the database
    public MessageData(String summary, String message, int id) {
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

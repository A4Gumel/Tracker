package com.eamatracker.Models;

public class Messages {

    private String date, message, server_time, receiver_username, sender_username;
    private Boolean isSeen;
    private String time;
    private long timestamp;

    public Messages(String date, String message, String server_time, String receiver_username, String sender_username, Boolean isSeen, String time, long timestamp) {
        this.date = date;
        this.message = message;
        this.server_time = server_time;
        this.receiver_username = receiver_username;
        this.sender_username = sender_username;
        this.isSeen = isSeen;
        this.time = time;
        this.timestamp = timestamp;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getReceiver_username() {
        return receiver_username;
    }

    public void setReceiver_username(String receiver_username) {
        this.receiver_username = receiver_username;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

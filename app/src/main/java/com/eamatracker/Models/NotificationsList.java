package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsList {

    private int count;
    private String next, previous;

    @SerializedName("results")
    private List<Notification> notificationList;


    public NotificationsList() {
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}

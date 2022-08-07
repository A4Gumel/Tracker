package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityList {

    //Activity List

    private int count;

    @SerializedName("activity")
    private List<Activity> mActivityList;

    public ActivityList() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Activity> getActivityList() {
        return mActivityList;
    }

    public void setActivityList(List<Activity> activityList) {
        mActivityList = activityList;
    }
}

package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Coupon {

    private String code;

    @SerializedName("time_valid")
    private Date timeValid;

    @SerializedName("time_invalid")
    private Date timeInvalid;

    private int percent;

    @SerializedName("new_users_only")
    private boolean newUserOnly;


    private String type;


    public Coupon() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTimeValid() {
        return timeValid;
    }

    public void setTimeValid(Date timeValid) {
        this.timeValid = timeValid;
    }

    public Date getTimeInvalid() {
        return timeInvalid;
    }

    public void setTimeInvalid(Date timeInvalid) {
        this.timeInvalid = timeInvalid;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isNewUserOnly() {
        return newUserOnly;
    }

    public void setNewUserOnly(boolean newUserOnly) {
        this.newUserOnly = newUserOnly;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

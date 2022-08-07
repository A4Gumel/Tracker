package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Resolve {

    private boolean status;
    private String message;

    @SerializedName("data")
    private ResolveData resolveData;


    public Resolve() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResolveData getResolveData() {
        return resolveData;
    }

    public void setResolveData(ResolveData resolveData) {
        this.resolveData = resolveData;
    }
}

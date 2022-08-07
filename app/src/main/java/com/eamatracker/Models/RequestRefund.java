package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class RequestRefund {

    @SerializedName("ref_code")
    private String refCode;

    private String message;


    public RequestRefund() {
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Transfer {

    private boolean status;
    private String message;

    @SerializedName("data")
    private TransferData transferData;


    public Transfer() {
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

    public TransferData getTransferData() {
        return transferData;
    }

    public void setTransferData(TransferData transferData) {
        this.transferData = transferData;
    }
}

package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class TransferRecipient {

    private boolean status;
    private String message;

    @SerializedName("data")
    private TransferRecipientData transferRecipientData;

    public TransferRecipient() {
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

    public TransferRecipientData getTransferRecipientData() {
        return transferRecipientData;
    }

    public void setTransferRecipientData(TransferRecipientData transferRecipientData) {
        this.transferRecipientData = transferRecipientData;
    }
}

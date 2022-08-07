package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BankList {

    private boolean status;
    private String message;

    @SerializedName("data")
    private List<Bank> banks;

    public BankList() {
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

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }
}

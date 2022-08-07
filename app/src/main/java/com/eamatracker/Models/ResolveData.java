package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class ResolveData {

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("bank_id")
    private Integer bankId;


    public ResolveData() {
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }
}

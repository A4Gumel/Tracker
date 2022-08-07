package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class TransferRecipientDetails {

    @SerializedName("authorization_code")
    private String authorizationCode;

    @SerializedName("account_number")
    private Integer accountNumber;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("bank_code")
    private Integer bankCode;

    @SerializedName("bank_name")
    private String bankName;


    public TransferRecipientDetails() {
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getBankCode() {
        return bankCode;
    }

    public void setBankCode(Integer bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

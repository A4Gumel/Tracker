package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class DriverDetails {

    private Integer id;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("bank_account_name")
    private String bankAccountName;

    @SerializedName("bank_account_number")
    private Integer bankAccountNumber;

    @SerializedName("user_details")
    private UserDetails userDetails;


    public DriverDetails() {
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public Integer getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(Integer bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
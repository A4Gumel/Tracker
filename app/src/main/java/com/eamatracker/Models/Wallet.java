package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Wallet {

    @SerializedName("owner_type")
    private String ownerType;

    private String slug;

    @SerializedName("balance_currency")
    private String balanceCurrency;

    private Double balance;

    public Wallet() {
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBalanceCurrency() {
        return balanceCurrency;
    }

    public void setBalanceCurrency(String balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

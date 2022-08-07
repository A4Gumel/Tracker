package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class BuyNow {

    @SerializedName("payment_gateway")
    private String paymentGateway;

    private Double amount;

    @SerializedName("ref_code")
    private String refCode;

    private String message;


    public BuyNow() {
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

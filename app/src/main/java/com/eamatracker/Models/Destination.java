package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("product_id")
    private Integer productId;


    public Destination(String countryCode, Integer productId) {
        this.countryCode = countryCode;
        this.productId = productId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}

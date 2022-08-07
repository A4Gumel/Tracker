package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableShipping {

    private Integer count;


    @SerializedName("available_shipping")
    private List<ShippingCom> availableShippingCompanies;

    public AvailableShipping() {
    }

    public List<ShippingCom> getAvailableShippingCompanies() {
        return availableShippingCompanies;
    }

    public void setAvailableShippingCompanies(List<ShippingCom> availableShippingCompanies) {
        this.availableShippingCompanies = availableShippingCompanies;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

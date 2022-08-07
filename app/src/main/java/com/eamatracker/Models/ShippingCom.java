package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShippingCom {

    private int id;

    private String name;

    private String type;

    @SerializedName("price_per_kg")
    private Double pricePerKg;

    @SerializedName("estimated_date_of_delivery")
    private Integer estDeliveryDate;

    public ShippingCom() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(Double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public Integer getEstDeliveryDate() {
        return estDeliveryDate;
    }

    public void setEstDeliveryDate(Integer estDeliveryDate) {
        this.estDeliveryDate = estDeliveryDate;
    }

}

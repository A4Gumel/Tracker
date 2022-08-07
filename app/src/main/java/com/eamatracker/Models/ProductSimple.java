package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class ProductSimple {

    private Integer id;

    @SerializedName("price_a")
    private Double priceA;

    @SerializedName("price_b")
    private Double priceB;

    @SerializedName("price_c")
    private Double priceC;

    private String photo1;

    private String photo2;

    private String units;

    private String name;

    private Double weight;

    @SerializedName("min_order")
    private Integer minOrder;

    @SerializedName("num_orders")
    private Integer numOrders;

    private Integer discount;

    @SerializedName("user_details")
    private SimpleUser simpleUser;

    public ProductSimple() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPriceA() {
        return priceA;
    }

    public void setPriceA(Double priceA) {
        this.priceA = priceA;
    }

    public Double getPriceB() {
        return priceB;
    }

    public void setPriceB(Double priceB) {
        this.priceB = priceB;
    }

    public Double getPriceC() {
        return priceC;
    }

    public void setPriceC(Double priceC) {
        this.priceC = priceC;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }


    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    public Integer getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(Integer numOrders) {
        this.numOrders = numOrders;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public SimpleUser getSimpleUser() {
        return simpleUser;
    }

    public void setSimpleUser(SimpleUser simpleUser) {
        this.simpleUser = simpleUser;
    }
}

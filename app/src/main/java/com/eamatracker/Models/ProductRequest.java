package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ProductRequest {

    private Integer id;

    @SerializedName("time_invalid")
    private Date timeInvalid;

    private String grade;

    private Integer quantity;

    @SerializedName("requested_product")
    private ProductSimple product;

    public ProductRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimeInvalid() {
        return timeInvalid;
    }

    public void setTimeInvalid(Date timeInvalid) {
        this.timeInvalid = timeInvalid;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductSimple getProduct() {
        return product;
    }

    public void setProduct(ProductSimple product) {
        this.product = product;
    }
}

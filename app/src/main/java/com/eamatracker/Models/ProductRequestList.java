package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductRequestList {

    private Integer count;
    private String next, previous;

    @SerializedName("results")
    private List<ProductRequest> productRequestList;

    public ProductRequestList() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<ProductRequest> getProductRequestList() {
        return productRequestList;
    }

    public void setProductRequestList(List<ProductRequest> productRequestList) {
        this.productRequestList = productRequestList;
    }
}

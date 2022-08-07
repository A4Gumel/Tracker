package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductList {

    private int count;

    private String next, previous;

    @SerializedName("results")
    private List<ProductSimple> productSimpleList;


    public ProductList() {
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<ProductSimple> getProductSimpleList() {
        return productSimpleList;
    }

    public void setProductSimpleList(List<ProductSimple> productSimpleList) {
        this.productSimpleList = productSimpleList;
    }
}

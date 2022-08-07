package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WarehouseList {

    private Integer count;

    private String next;

    private String previous;

    @SerializedName("results")
    private List<Warehouse> warehouseList;


    public WarehouseList() {
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

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }
}
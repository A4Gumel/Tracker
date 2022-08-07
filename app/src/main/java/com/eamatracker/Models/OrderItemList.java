package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderItemList {

    private int count;
    private String next;
    private String previous;

    @SerializedName("results")
    public List<OrderItem> orderItemList;

    public OrderItemList() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
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

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}

package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartList {

    @SerializedName("results")
    private List<Cart> mCartList;
    private int count;
    private String next, previous;

    public CartList() {
    }

    public List<Cart> getCartList() {
        return mCartList;
    }

    public void setCartList(List<Cart> cartList) {
        mCartList = cartList;
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
}

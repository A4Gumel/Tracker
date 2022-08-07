package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponList {

    private int count;
    private String next, previous;

    @SerializedName("results")
    private List<Coupon> mCouponList;

    public CouponList() {
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

    public List<Coupon> getCouponList() {
        return mCouponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        mCouponList = couponList;
    }
}

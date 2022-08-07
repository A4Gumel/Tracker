package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressList {

    //Address List model

    @SerializedName("results")
    private List<Address> mAddressList;

    private int count;
    private String previous;
    private String next;

    public AddressList() {
    }


    public List<Address> getAddressList() {
        return mAddressList;
    }

    public void setAddressList(List<Address> addressList) {
        mAddressList = addressList;
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
}

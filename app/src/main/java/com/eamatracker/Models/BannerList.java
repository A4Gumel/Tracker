package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BannerList {

    private int count;
    private int next;
    private int previous;

    @SerializedName("results")
    private List<Banner> mBannerList;

    public BannerList() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public List<Banner> getBannerList() {
        return mBannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        mBannerList = bannerList;
    }
}


package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList {

    private int count;
    private String next, previous;

    @SerializedName("results")
    private List<Category> mCategoryList;

    public CategoryList() {
    }

    public List<Category> getCategoryList() {
        return mCategoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        mCategoryList = categoryList;
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

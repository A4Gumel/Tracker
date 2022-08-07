package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FAQS {

    private int count;
    private String next;
    private String previous;

    @SerializedName("results")
    private List<FaqsTranslations> results;

    public FAQS() {
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

    public List<FaqsTranslations> getResults() {
        return results;
    }

    public void setResults(List<FaqsTranslations> results) {
        this.results = results;
    }
}

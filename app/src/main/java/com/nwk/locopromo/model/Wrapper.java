package com.nwk.locopromo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andy on 12/22/2014.
 */
public class Wrapper<I extends Object> {
    @SerializedName("count")
    private Integer count;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("results")
    private I results;

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

    public I getResults() {
        return results;
    }

    public void setResults(I results) {
        this.results = results;
    }
}

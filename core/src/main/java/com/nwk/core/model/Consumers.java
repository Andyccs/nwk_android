package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Andy on 12/22/2014.
 */
@Parcel
public class Consumers {

    @SerializedName("user")
    String user;

    @SerializedName("picture")
    String picture;

    @SerializedName("point")
    Integer point;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}

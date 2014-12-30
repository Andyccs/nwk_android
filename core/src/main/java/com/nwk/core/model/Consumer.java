package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Andy on 12/22/2014.
 */
@Parcel
public class Consumer {

    @SerializedName("user")
    String user;

    @SerializedName("picture")
    String picture;

    @SerializedName("point")
    Integer point;

    @SerializedName("favorite_shops")
    List<String> favoriteShops;

    @SerializedName("id")
    Integer id;

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

    public List<String> getFavoriteShops() {
        return favoriteShops;
    }

    public void setFavoriteShops(List<String> favoriteShops) {
        this.favoriteShops = favoriteShops;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

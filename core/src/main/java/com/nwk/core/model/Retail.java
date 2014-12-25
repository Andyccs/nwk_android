package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Andy on 12/22/2014.
 */
@Parcel
public class Retail {
    @SerializedName("user")
    String user;

    @SerializedName("shop_name")
    String shop_name;

    @SerializedName("mall")
    String mall;

    @SerializedName("logo_url")
    String logo_url;

    @SerializedName("location_level")
    String location_level;

    @SerializedName("location_unit")
    String location_unit;

    @SerializedName("id")
    Integer id;

    public Retail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getShopName() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getLocationLevel() {
        return location_level;
    }

    public void setLocation_level(String location_level) {
        this.location_level = location_level;
    }

    public String getLocationUnit() {
        return location_unit;
    }

    public void setLocation_unit(String location_unit) {
        this.location_unit = location_unit;
    }
}

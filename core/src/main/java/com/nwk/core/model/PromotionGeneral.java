package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Andy on 12/23/2014.
 */
@Parcel
public class PromotionGeneral extends Promotion {

    @SerializedName("price")
    Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

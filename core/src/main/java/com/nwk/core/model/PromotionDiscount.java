package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Andy on 12/23/2014.
 */
@Parcel
public class PromotionDiscount extends Promotion{
    @SerializedName("discount")
    Double discount;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}

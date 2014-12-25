package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Andy on 12/23/2014.
 */
@Parcel
public class PromotionReduction extends Promotion{
    @SerializedName("original_price")
    Double originalPrice;

    @SerializedName("discount_price")
    Double discountPrice;

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
}

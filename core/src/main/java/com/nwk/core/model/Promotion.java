package com.nwk.core.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Andy on 12/23/2014.
 */
@Parcel
public class Promotion {
    @SerializedName("retail")
    String retail;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("time_expiry")
    String timeExpiry;

    @SerializedName("image_url")
    String imageUrl;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("id")
    Integer id;

    public String getRetail() {
        return retail;
    }

    public void setRetail(String retail) {
        this.retail = retail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeExpiry() {
        return timeExpiry;
    }

    public void setTimeExpiry(String timeExpiry) {
        this.timeExpiry = timeExpiry;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

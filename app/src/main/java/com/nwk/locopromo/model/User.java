package com.nwk.locopromo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Andy on 12/22/2014.
 */
@Parcel
public class User {
    @SerializedName("username")
    String username;

    @SerializedName("email")
    String email;

    @SerializedName("groups")
    List<String> groups;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}

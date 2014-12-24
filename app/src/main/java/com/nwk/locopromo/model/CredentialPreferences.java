package com.nwk.locopromo.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Andy on 12/24/2014.
 */
public class CredentialPreferences {
    public static final String PREFS_PRIMARY_KEY = "__PRIMARY__";
    public static final String PREFS_USERNAME_KEY = "__USERNAME__";
    public static final String PREFS_EMAIL_KEY = "__EMAIL__";
    public static final String PREFS_PICTURE_KEY = "__PICTURE__";
    public static final String PREFS_POINT_KEY = "__POINT__";

    public static void saveCredential(Context context, int pk, String username, String email, String picture, int point){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_USERNAME_KEY,username);
        editor.putString(PREFS_EMAIL_KEY,email);
        editor.putString(PREFS_PICTURE_KEY,picture);
        editor.putInt(PREFS_POINT_KEY, point);
        editor.putInt(PREFS_PRIMARY_KEY,pk);
        editor.commit();
    }

    public static int getPrimaryKey(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREFS_PRIMARY_KEY,-1);
    }

    public static String getUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFS_USERNAME_KEY,null);
    }

    public static String getEmail(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFS_EMAIL_KEY,null);
    }

    public static String getPicture(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFS_PICTURE_KEY,null);
    }

    public static int getPoint(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREFS_POINT_KEY, -1);
    }
}

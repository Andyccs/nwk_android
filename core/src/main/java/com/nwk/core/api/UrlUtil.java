package com.nwk.core.api;

import com.nwk.core.Constant;

import timber.log.Timber;

public class UrlUtil {
    public static String getUserUrl(String userPrimaryKey) {
        return "/nwk/users/" + userPrimaryKey + "/";
    }

    public static String getRetailUrl(String retailPrimaryKey) {
        return "/nwk/retails/" + retailPrimaryKey + "/";
    }

    public static String getConsumerUrl(String consumerPrimaryKey) {
        return "/nwk/consumers/" + consumerPrimaryKey + "/";
    }

    public static String getPromotionUrl(String promotionPrimaryKey) {
        return "/nwk/promotions/" + promotionPrimaryKey + "/";
    }

    public static Integer getUserPrimaryKeyByUrl(String userUrl){
        String primaryKey = userUrl.replaceFirst(Constant.SERVER_IP_ADDRESS,"");
        primaryKey = primaryKey.replaceFirst("/nwk/users/","");
        primaryKey = primaryKey.replaceAll("/","");
        try {
            Integer pk = Integer.parseInt(primaryKey);
            return pk;
        }catch(NumberFormatException e){
            Timber.e(e.getMessage());
        }

        return -1;
    }

    public static Integer getRetailPrimaryKeyByUrl(String retailUrl){
        String primaryKey = retailUrl.replaceFirst(Constant.SERVER_IP_ADDRESS,"");
        primaryKey = primaryKey.replaceFirst("/nwk/retails/","");
        primaryKey = primaryKey.replaceAll("/","");
        try {
            Integer pk = Integer.parseInt(primaryKey);
            return pk;
        }catch(NumberFormatException e){
            Timber.e(e.getMessage());
        }

        return -1;
    }
}
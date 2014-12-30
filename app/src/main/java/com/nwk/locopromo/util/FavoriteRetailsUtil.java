package com.nwk.locopromo.util;

import com.nwk.core.Constant;
import com.nwk.core.api.BackendService;
import com.nwk.core.model.Consumers;
import com.nwk.locopromo.PromoApplication;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by andyccs on 28/12/14.
 */
public class FavoriteRetailsUtil {
    public static String getUserUrl(String userPrimaryKey){
        return "/nwk/users/"+userPrimaryKey+"/";
    }

    public static String getRetailUrl(String retailPrimaryKey){
        return "/nwk/retails/"+retailPrimaryKey+"/";
    }

    public static String getConsumerUrl(String consumerPrimaryKey){
        return "/nwk/consumers/"+consumerPrimaryKey+"/";
    }

    public static String getPromotionUrl(String promotionPrimaryKey){
        return "/nwk/promotions/"+promotionPrimaryKey+"/";
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

    public static List<String> getStringForAddFavoriteRetails(PromoApplication context, String userPrimaryKey, List<String> retailPrimaryKeys){
        //get user original favorite shops
        BackendService service = context.getService();
        Consumers consumers = service.getConsumerByUrl(userPrimaryKey);
        List<String> retails = consumers.getFavoriteShops();

        if(retails==null){
            retails = new ArrayList<>();
        }
        //add new retails to original key
        for(String pk : retailPrimaryKeys){
            retails.add(FavoriteRetailsUtil.getRetailUrl(pk));
        }

        return retails;
    }

    public static List<String> getStringForRemoveFavoriteRetail(PromoApplication context, String userPrimaryKey, String retailPrimaryKeys){
        //get user original favorite shops
        BackendService service = context.getService();
        Consumers consumers = service.getConsumerByUrl(userPrimaryKey);
        List<String> retails = consumers.getFavoriteShops();

        if(retails==null){
            retails = new ArrayList<>();
        }

        //add new retails to original key
        for(int i=0;i<retails.size();i++){
            if(retails.get(i).contains(FavoriteRetailsUtil.getRetailUrl(retailPrimaryKeys))){
                retails.remove(i);
                break;
            }
        }

        return retails;
    }
}

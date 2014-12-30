package com.nwk.locopromo.util;

import com.nwk.core.Constant;
import com.nwk.core.api.BackendService;
import com.nwk.core.api.UrlUtil;
import com.nwk.core.model.Consumer;
import com.nwk.locopromo.PromoApplication;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by andyccs on 28/12/14.
 */
public class FavoriteRetailsUtil {

    public static List<String> getStringForAddFavoriteRetails(PromoApplication context, String userPrimaryKey, List<String> retailPrimaryKeys){
        //get user original favorite shops
        BackendService service = context.getService();
        Consumer consumers = service.getConsumerByUrl(userPrimaryKey);
        List<String> retails = consumers.getFavoriteShops();

        if(retails==null){
            retails = new ArrayList<>();
        }
        //add new retails to original key
        for(String pk : retailPrimaryKeys){
            retails.add(UrlUtil.getRetailUrl(pk));
        }

        return retails;
    }

    public static List<String> getStringForRemoveFavoriteRetail(PromoApplication context, String userPrimaryKey, String retailPrimaryKeys){
        //get user original favorite shops
        BackendService service = context.getService();
        Consumer consumers = service.getConsumerByUrl(userPrimaryKey);
        List<String> retails = consumers.getFavoriteShops();

        if(retails==null){
            retails = new ArrayList<>();
        }

        //add new retails to original key
        for(int i=0;i<retails.size();i++){
            if(retails.get(i).contains(UrlUtil.getRetailUrl(retailPrimaryKeys))){
                retails.remove(i);
                break;
            }
        }

        return retails;
    }
}

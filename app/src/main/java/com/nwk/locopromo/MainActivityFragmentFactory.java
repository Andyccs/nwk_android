package com.nwk.locopromo;

import android.support.v4.app.Fragment;

import com.nwk.locopromo.state.ShopCategory;
import com.nwk.locopromo.state.ViewType;

/**
 * Created by Andy on 12/9/2014.
 */
public class MainActivityFragmentFactory {
    private MainActivityFragmentFactory() {
    }

    public static Fragment createFragment(ViewType viewType, ShopCategory shopCategory){
        if(viewType == ViewType.ALL_PROMOTION){
            return MainPromotionFragment.newInstance();
        }else{
            if(shopCategory == ShopCategory.FOOD){
                return MainPromotionFragment.newInstance();
            }
            else if(shopCategory == ShopCategory.FASHION){
                return MainPromotionFragment.newInstance();
            }
            else if(shopCategory == ShopCategory.LIFESTYLE){
                return MainPromotionFragment.newInstance();
            }
            else if(shopCategory == ShopCategory.OTHER){
                return MainPromotionFragment.newInstance();
            }
            else{
                return MainPromotionFragment.newInstance();
            }
        }

    }
}

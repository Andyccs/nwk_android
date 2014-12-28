package com.nwk.locopromo;

import android.support.v4.app.Fragment;

import com.nwk.core.api.BackendService;
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
            return MainPromotionFragment.newInstance(BackendService.Category.OTHER);
        }else{
            if(shopCategory == ShopCategory.FOOD){
                return MainPromotionFragment.newInstance(BackendService.Category.FOOD);
            }
            else if(shopCategory == ShopCategory.FASHION){
                return MainPromotionFragment.newInstance(BackendService.Category.FASHION);
            }
            else if(shopCategory == ShopCategory.LIFESTYLE){
                return MainPromotionFragment.newInstance(BackendService.Category.LIFESTYLE);
            }
            else if(shopCategory == ShopCategory.OTHER){
                return MainPromotionFragment.newInstance(BackendService.Category.OTHER);
            }
            else{
                return MainPromotionFragment.newInstance(BackendService.Category.FOOD);
            }
        }

    }
}

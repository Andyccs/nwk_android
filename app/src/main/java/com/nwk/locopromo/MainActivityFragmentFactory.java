package com.nwk.locopromo;

import android.support.v4.app.Fragment;

import com.nwk.core.api.BackendService;
import com.nwk.locopromo.state.ShopCategory;
import com.nwk.locopromo.state.ViewType;
import com.nwk.locopromo.widget.LevelRetailsWithCategoryFragment;

import timber.log.Timber;

/**
 * Created by Andy on 12/9/2014.
 */
public class MainActivityFragmentFactory {
    private MainActivityFragmentFactory() {
    }

    public static Fragment createFragment(ViewType viewType, ShopCategory shopCategory){
        if(viewType == ViewType.FAVORITE_SHOP){
            Timber.d("favorite shop view type");
            Timber.d("view type: "+viewType);
            return FavoriteRetailsFragment.newInstance();
        }
        else if(viewType == ViewType.ALL_PROMOTION){
            return AllPromotionsFragment.newInstance();
        }
        else if(viewType == ViewType.ALL_SHOP){
            return AllRetailsWithCategoryFragment.newInstance(getCategoryByShopCategory(shopCategory));
        }
        else if(viewType == ViewType.Level1){
            return LevelRetailsWithCategoryFragment.newInstance(getCategoryByShopCategory(shopCategory),1);
        }
        else if(viewType == ViewType.Level2){
            return LevelRetailsWithCategoryFragment.newInstance(getCategoryByShopCategory(shopCategory),2);

        }

        return AllRetailsWithCategoryFragment.newInstance(BackendService.Category.FOOD);

    }

    private static String getCategoryByShopCategory(ShopCategory shopCategory){

        if(shopCategory == ShopCategory.FOOD){
            return BackendService.Category.FOOD;
        }
        else if(shopCategory == ShopCategory.FASHION){
            return BackendService.Category.FASHION;
        }
        else if(shopCategory == ShopCategory.LIFESTYLE){
            return BackendService.Category.LIFESTYLE;
        }
        else if(shopCategory == ShopCategory.OTHER){
            return BackendService.Category.OTHER;
        }
        return null;
    }
}

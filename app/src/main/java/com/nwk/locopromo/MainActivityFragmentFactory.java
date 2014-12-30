package com.nwk.locopromo;

import android.support.v4.app.Fragment;

import com.nwk.core.api.BackendService;
import com.nwk.locopromo.state.ShopCategory;
import com.nwk.locopromo.state.ViewType;

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
            if(shopCategory == ShopCategory.FOOD){
                return AllRetailsWithCategoryFragment.newInstance(BackendService.Category.FOOD);
            }
            else if(shopCategory == ShopCategory.FASHION){
                return AllRetailsWithCategoryFragment.newInstance(BackendService.Category.FASHION);
            }
            else if(shopCategory == ShopCategory.LIFESTYLE){
                return AllRetailsWithCategoryFragment.newInstance(BackendService.Category.LIFESTYLE);
            }
            else if(shopCategory == ShopCategory.OTHER){
                return AllRetailsWithCategoryFragment.newInstance(BackendService.Category.OTHER);
            }
        }

        return AllRetailsWithCategoryFragment.newInstance(BackendService.Category.FOOD);

    }
}

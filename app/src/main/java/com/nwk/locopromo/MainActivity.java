package com.nwk.locopromo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.nwk.core.model.CredentialPreferences;
import com.nwk.locopromo.state.ShopCategory;
import com.nwk.locopromo.state.ViewType;
import com.nwk.locopromo.widget.SelectableButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.Icicle;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.spinner)
    Spinner spinner;

    @InjectView(R.id.shopping_cart)
    ImageButton shoppingCart;

    @OnClick(R.id.shopping_cart)
    public void clickShoppingCart(){
        Intent intent = new Intent(this,ShoppingCartActivity.class);
        startActivity(intent);
    }

    @InjectView(R.id.profile)
    ImageButton profile;

    @OnClick(R.id.profile)
    public void clickProfile(){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

    @InjectView(R.id.category_food)
    SelectableButton foodButton;

    @OnClick(R.id.category_food)
    public void clickFood(SelectableButton view){
        shopCategory = ShopCategory.FOOD;
        renderFragment();
        selectButton();
    }

    @InjectView(R.id.category_fashion)
    SelectableButton fashionButton;

    @OnClick(R.id.category_fashion)
    public void clickFashion(SelectableButton view){
        shopCategory = ShopCategory.FASHION;
        renderFragment();
        selectButton();
    }

    @InjectView(R.id.category_lifestyle)
    SelectableButton lifestyleButton;

    @OnClick(R.id.category_lifestyle)
    public void clickLifestyle(SelectableButton view){
        shopCategory = ShopCategory.LIFESTYLE;
        renderFragment();
        selectButton();
    }

    @InjectView(R.id.category_other)
    SelectableButton otherButton;

    @OnClick(R.id.category_other)
    public void clickOther(SelectableButton view){
        shopCategory = ShopCategory.OTHER;
        renderFragment();
        selectButton();
    }

    @Icicle
    ShopCategory shopCategory;

    @Icicle
    ViewType viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if(viewType==null){
            viewType = ViewType.FAVORITE_SHOP;
            shopCategory = ShopCategory.FOOD;
        }

        if(shopCategory==null){
            shopCategory = ShopCategory.FOOD;
        }

        //Set up Spinner that is used to choose ViewType
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.main_activity_spinner));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        viewType = ViewType.ALL_SHOP;
                        shopCategory = ShopCategory.FOOD;
                    case 1:
                        viewType = ViewType.SHOP_WITH_PROMOTION;
                        shopCategory = ShopCategory.FOOD;
                    case 2:
                        viewType = ViewType.ALL_PROMOTION;
                    case 3:
                        viewType = ViewType.FAVORITE_SHOP;
                        shopCategory = ShopCategory.FOOD;
                }
                renderFragment();
                selectButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        renderFragment();
        selectButton();

        //TODO remove this in production
        CredentialPreferences.saveFirstTime(this,true);

        //TODO if this is user's first time, then
        if(CredentialPreferences.getFirstTime(this)) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            CredentialPreferences.saveFirstTime(this,false);
        }
    }

    private void renderFragment() {
        Fragment fragment = MainActivityFragmentFactory.createFragment(viewType, shopCategory);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void selectButton(){
        foodButton.setSelected(false);
        fashionButton.setSelected(false);
        lifestyleButton.setSelected(false);
        otherButton.setSelected(false);

        if(shopCategory==ShopCategory.FOOD){
            foodButton.setSelected(true);
        }else if(shopCategory==ShopCategory.FASHION){
            fashionButton.setSelected(true);
        }else if(shopCategory==ShopCategory.LIFESTYLE){
            lifestyleButton.setSelected(true);
        }else if(shopCategory==ShopCategory.OTHER){
            otherButton.setSelected(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}

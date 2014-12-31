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
import android.widget.RelativeLayout;
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
import timber.log.Timber;


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
        startActivityForResult(intent,REQUEST_SIGN_OUT);
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

    @InjectView(R.id.category_bar)
    RelativeLayout categoryBar;

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
            viewType = ViewType.ALL_SHOP;
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
                Timber.d("item " + i + " in spinner is clicked");
                switch (i){
                    case 0:
                        viewType = ViewType.ALL_SHOP;
                        shopCategory = ShopCategory.FOOD;
                        categoryBar.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        viewType = ViewType.ALL_PROMOTION;
                        categoryBar.setVisibility(View.GONE);
                        break;
                    case 2:
                        viewType = ViewType.FAVORITE_SHOP;
                        categoryBar.setVisibility(View.GONE);
                        break;
                    case 3:
                        viewType = ViewType.Level1;
                        shopCategory = ShopCategory.FOOD;
                        break;
                    case 4:
                        viewType = ViewType.Level2;
                        shopCategory = ShopCategory.FOOD;
                        break;
                }
                Timber.d("set viewType to: "+viewType);
                renderFragment();
                selectButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        renderFragment();
        selectButton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CredentialPreferences.getFirstTime(this)) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            CredentialPreferences.saveFirstTime(this,false);
        }
    }

    private void renderFragment() {
        Timber.d("creating fragment for "+viewType+" and "+ shopCategory);
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

    public static final int REQUEST_SIGN_OUT = 1;
    public static final int RESULT_SIGN_OUT = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_SIGN_OUT){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

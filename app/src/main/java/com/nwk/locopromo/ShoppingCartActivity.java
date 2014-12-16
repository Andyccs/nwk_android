package com.nwk.locopromo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.nwk.locopromo.adapter.PromotionListViewAdapter;
import com.nwk.locopromo.adapter.ShoppingCartListViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import timber.log.Timber;


public class ShoppingCartActivity extends ActionBarActivity {
    @InjectView(R.id.progress)
    CircularProgressBar progressBar;

    @InjectView(R.id.promotion_list)
    ListView promotionList;

    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    ShoppingCartListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.inject(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setTitle("Shopping Cart");

        adapter = new ShoppingCartListViewAdapter(this);
        promotionList.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(R.color.color_red_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                initializeData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        initializeData();
    }

    private void initializeData() {
        //TODO remove this part
        ParseQuery<ParseObject> promotionQuery = ParseQuery.getQuery("Promotion");
        promotionQuery.include("retail");
        promotionQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> promotions, ParseException e) {
                Timber.d("Size: " + promotions.size());
                if (e == null) {
                    List<Promotion> promotionList = new ArrayList<Promotion>();
                    for (ParseObject object : promotions) {
                        Promotion promotion = new Promotion();
                        promotion.setId(object.getObjectId());
                        promotion.setTitle(object.getString("title"));
                        promotion.setDescription(object.getString("description"));
                        promotion.setImage(object.getParseFile("image").getUrl());
                        promotion.setQuantity(object.getInt("quantity"));
                        promotion.setTimeExpiry(object.getDate("timeExpiry"));
                        promotion.setDiscountPrice(object.getInt("discountPrice"));
                        promotion.setOriginalPrice(object.getInt("originalPrice"));
                        promotion.setPercentage(object.getInt("percentage"));
                        promotion.setType(object.getInt("type"));
                        promotion.setRetail(object.getParseObject("retail").getObjectId());
                        promotionList.add(promotion);
                    }
                    adapter.setPromotions(promotionList);
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}

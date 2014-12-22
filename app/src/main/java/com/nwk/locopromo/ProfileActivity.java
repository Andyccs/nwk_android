package com.nwk.locopromo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.nwk.locopromo.adapter.RedeemHistoryListViewAdapter;
import com.nwk.locopromo.model.OldPromotion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class ProfileActivity extends ActionBarActivity {

    @InjectView(R.id.history_list)
    ListView historyList;

    RedeemHistoryListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.show();
        actionBar.setTitle("Profile");

        ButterKnife.inject(this);

        adapter = new RedeemHistoryListViewAdapter(this);
        historyList.setAdapter(adapter);

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
                    List<OldPromotion> promotionList = new ArrayList<OldPromotion>();
                    for (ParseObject object : promotions) {
                        OldPromotion promotion = new OldPromotion();
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
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

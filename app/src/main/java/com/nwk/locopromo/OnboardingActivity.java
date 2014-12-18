package com.nwk.locopromo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;

import com.nwk.locopromo.adapter.RetailRectangleGridViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class OnboardingActivity extends ActionBarActivity {

    @InjectView(R.id.retail_list)
    GridView retailList;

    RetailRectangleGridViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.inject(this);

        adapter = new RetailRectangleGridViewAdapter(this);
        retailList.setAdapter(adapter);

        initializeData();
    }

    private void initializeData() {
        ParseQuery promotionQuery = ParseQuery.getQuery("Retail");
        promotionQuery.include("user");
        promotionQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List retail, ParseException e) {
                Timber.d("Size: " + retail.size());
                if (e == null) {
                    adapter.addItems(retail);
                }
            }
        });

    }
}

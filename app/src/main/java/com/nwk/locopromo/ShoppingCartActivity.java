package com.nwk.locopromo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nwk.core.api.BackendService;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.GrabPromotion;
import com.nwk.locopromo.adapter.ShoppingCartListViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ShoppingCartActivity extends ActionBarActivity {
    @InjectView(R.id.progress)
    CircularProgressBar progressBar;

    @InjectView(R.id.promotion_list)
    ListView promotionList;

    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.placeholder)
    TextView placeholder;

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
            }
        });
        initializeData();
    }

    private void initializeData() {
        BackendService service = ((PromoApplication)getApplication()).getService();
        service.getGrabCart(
                ""+ CredentialPreferences.getCustomerPrimaryKey(getApplicationContext()),
                new Callback<List<GrabPromotion>>() {
                    @Override
                    public void success(List<GrabPromotion> grabPromotions, Response response) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        adapter.setGrabPromotions(grabPromotions);
                        adapter.notifyDataSetChanged();

                        if (grabPromotions.size() > 0) {
                            placeholder.setVisibility(View.GONE);
                        } else {
                            placeholder.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {

                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

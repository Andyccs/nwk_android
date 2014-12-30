package com.nwk.locopromo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nwk.core.api.BackendService;
import com.nwk.core.model.Consumers;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.Promotion;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.adapter.PromotionListViewAdapter;
import com.nwk.locopromo.util.FavoriteRetailsUtil;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import icepick.Icepick;
import icepick.Icicle;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class PromotionListActivity extends ActionBarActivity {
    @InjectView(R.id.progress)
    CircularProgressBar progressBar;

    @InjectView(R.id.promotion_list)
    ListView promotionList;

    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.placeholder)
    TextView placeholder;

    Menu menu;

    PromotionListViewAdapter adapter;

    Retail retail;

    UserFavoriteTask favoriteTask;

    UserUnfavoriteTask unfavoriteTask;

    @Icicle
    boolean favorite;

    @Icicle
    Parcelable retailParcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_promotion_list);
        ButterKnife.inject(this);

        if(retailParcelable==null) {
            Object retailObject = getIntent().getExtras().get("retail");
            retailParcelable = (Parcelable) retailObject;
            retail = Parcels.unwrap((android.os.Parcelable) retailObject);
        }else{
            retail = Parcels.unwrap(retailParcelable);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(retail.getShopName());

        adapter = new PromotionListViewAdapter(this,retail);
        promotionList.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(R.color.color_red_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                initializeData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        initializeData();
    }

    private void initializeData() {
        ((PromoApplication)getApplication()).getService().listPromotionsByRetail(retail.getId(),new Callback<List<Promotion>>() {
            @Override
            public void success(List<Promotion> promotions, Response response) {
                adapter.setPromotions(promotions);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if(promotions.size()>0){
                    placeholder.setVisibility(View.GONE);
                }else{
                    placeholder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
                adapter.setPromotions(null);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                placeholder.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_promotion_list, menu);

        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.action_favorite);

        //TODO check if the shop has been favorite
        favorite = false;

        new CheckUserFavoriteRetailTask(
                ""+ CredentialPreferences.getCustomerPrimaryKey(getApplicationContext())).execute();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Timber.d("clicking favorite button");
            if(favorite){
                setUnFavoriteUI(item);
                unfavoriteRetail(item);
            }else {
                setFavoriteUI(item,true);
                favoriteRetail(item);
            }
            return true;
        }
        else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteRetail(final MenuItem item) {
        Timber.d("creating task to favorite retails");
        favoriteTask = new UserFavoriteTask(
                ""+ CredentialPreferences.getCustomerPrimaryKey(getApplicationContext()),
                ""+retail.getId(),
                CredentialPreferences.getUserUrl(getApplicationContext()));
        favoriteTask.execute();
    }

    private void unfavoriteRetail(MenuItem item){
        unfavoriteTask = new UserUnfavoriteTask(
                ""+ CredentialPreferences.getCustomerPrimaryKey(getApplicationContext()),
                ""+retail.getId(),
                CredentialPreferences.getUserUrl(getApplicationContext()));
        unfavoriteTask.execute();
    }

    private void setFavoriteUI(final MenuItem item, boolean isloading) {
        MenuItemCompat.setActionView(item, R.layout.action_favorite);

        MenuItemCompat.getActionView(item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnFavoriteUI(item);
                unfavoriteRetail(item);
            }
        });

        if(!isloading){
            MenuItemCompat.getActionView(item).setClickable(true);

            ProgressBar progressBar = (ProgressBar) MenuItemCompat.getActionView(item).findViewById(R.id.progress);
            progressBar.setVisibility(View.GONE);

            ImageView favoriteImage = (ImageView) MenuItemCompat.getActionView(item).findViewById(R.id.favorite_image);
            favoriteImage.setVisibility(View.VISIBLE);
        }else{
            MenuItemCompat.getActionView(item).setClickable(false);
        }

    }

    private void setUnFavoriteUI(MenuItem item){
        MenuItemCompat.setActionView(item, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public class CheckUserFavoriteRetailTask extends AsyncTask<Void,Void,Void>{

        String consumerPrimaryKey, retailUrl;
        CheckUserFavoriteRetailTask(String consumerPrimaryKey) {
            this.consumerPrimaryKey = consumerPrimaryKey;
        }
        @Override
        protected Void doInBackground(Void... params) {
            Timber.d("checking user favorite shops");
            BackendService service = ((PromoApplication)getApplication()).getService();
            Consumers consumers = service.getConsumerByUrl(consumerPrimaryKey);
            List<String> favoriteRetails = consumers.getFavoriteShops();

            Timber.d("number of favorite retails: "+favoriteRetails.size());

            if(favoriteRetails==null||favoriteRetails.size()==0){
                favorite = false;
                return null;
            }

            List<Integer> favoriteRetailsPK = new ArrayList<>(favoriteRetails.size());

            for(String url : favoriteRetails){
                Integer pk = FavoriteRetailsUtil.getRetailPrimaryKeyByUrl(url);
                favoriteRetailsPK.add(pk);
                Timber.d("primary key: "+pk);

            }

            if(favoriteRetailsPK.contains(retail.getId())){
                favorite = true;
                Timber.d("favorite is true");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MenuItem menuItem = menu.findItem(R.id.action_favorite);
            if(favorite){
                setFavoriteUI(menuItem,false);
            }else{
                setUnFavoriteUI(menuItem);
            }
        }
    }

    public class UserFavoriteTask extends AsyncTask<Void, Void, Boolean> {

        String consumerPrimaryKey, retailUrl,userUrl;
        UserFavoriteTask(String consumerPrimaryKey, String retailUrl, String userUrl) {
            this.consumerPrimaryKey = consumerPrimaryKey;
            this.retailUrl = retailUrl;
            this.userUrl = userUrl;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Timber.d("executing favorite retail task");
            List<String> addedRetailList = new ArrayList<>();
            addedRetailList.add(retailUrl);

            List<String> newRetailList =
                    FavoriteRetailsUtil.getStringForAddFavoriteRetails(
                            (PromoApplication)getApplication(),
                            consumerPrimaryKey,
                            addedRetailList);

            BackendService service = ((PromoApplication)getApplication()).getService();
            service.updateFavoriteRetailsOfConsumer(
                    consumerPrimaryKey,
                    userUrl,
                    newRetailList
            );

            favorite = true;

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Timber.d("showing favorite button and clickable");
            setFavoriteUI(menu.findItem(R.id.action_favorite),false);
        }
    }

    public class UserUnfavoriteTask extends AsyncTask<Void, Void, Boolean> {

        String consumerPrimaryKey, retailUrl,userUrl;
        UserUnfavoriteTask(String consumerPrimaryKey, String retailUrl, String userUrl) {
            this.consumerPrimaryKey = consumerPrimaryKey;
            this.retailUrl = retailUrl;
            this.userUrl = userUrl;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Timber.d("executing favorite retail task");

            List<String> newRetailList =
                    FavoriteRetailsUtil.getStringForRemoveFavoriteRetail(
                            (PromoApplication) getApplication(),
                            consumerPrimaryKey,
                            retailUrl);

            BackendService service = ((PromoApplication)getApplication()).getService();
            service.updateFavoriteRetailsOfConsumer(
                    consumerPrimaryKey,
                    userUrl,
                    newRetailList
            );

            favorite = false;

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Timber.d("showing favorite button and clickable");
            setUnFavoriteUI(menu.findItem(R.id.action_favorite));
        }
    }
}

package com.nwk.locopromo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.nwk.core.api.BackendService;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.adapter.RetailRectangleGridViewAdapter;
import com.nwk.locopromo.util.FavoriteRetailsUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class OnboardingActivity extends ActionBarActivity {

    @InjectView(R.id.retail_list)
    GridView retailList;

    @InjectView(R.id.skip_button)
    Button skipButton;

    @InjectView(R.id.next_button)
    Button nextButton;

    @OnClick(R.id.skip_button)
    public void clickSkipButton(){
        finish();
    }

    @OnClick(R.id.next_button)
    public void clickNextButton(){
        new UserFavoriteTask(
                ""+CredentialPreferences.getCustomerPrimaryKey(getApplicationContext()),
                CredentialPreferences.getUserUrl(getApplicationContext()),
                adapter.getSelectedRetails()).execute();
    }

    @InjectView(R.id.progress)
    CircularProgressBar progressBar;

    @InjectView(R.id.placeholder)
    TextView placeholder;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData() {
        ((PromoApplication)(getApplication())).getService().listRetails(
                null, null, new Callback<List<Retail>>() {
            @Override
            public void success(List<Retail> retails, Response response) {
                Timber.d("Size: " + retails.size());
                progressBar.setVisibility(View.GONE);
                adapter.setPromotionList(retails);
                adapter.notifyDataSetChanged();

                if (retails.size() > 0) {
                    placeholder.setVisibility(View.GONE);
                } else {
                    placeholder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
                progressBar.setVisibility(View.GONE);
                placeholder.setVisibility(View.VISIBLE);
            }
        });

    }


    public class UserFavoriteTask extends AsyncTask<Void, Void, Boolean> {

        String consumerPrimaryKey;
        List<String> retailUrls;
        String userUrl;

        UserFavoriteTask(String consumerPrimaryKey, String userUrl, List<String> retailUrls) {
            this.consumerPrimaryKey = consumerPrimaryKey;
            this.retailUrls = retailUrls;
            this.userUrl = userUrl;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Timber.d("executing favorite retail task");

            List<String> newRetailList =
                    FavoriteRetailsUtil.getStringForAddFavoriteRetails(
                            (PromoApplication) getApplication(),
                            consumerPrimaryKey,
                            retailUrls);

            BackendService service = ((PromoApplication)getApplication()).getService();
            service.updateFavoriteRetailsOfConsumer(
                    consumerPrimaryKey,
                    userUrl,
                    newRetailList
            );

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            finish();
        }
    }
}

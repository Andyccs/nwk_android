package com.nwk.locopromo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import com.nwk.locopromo.adapter.RetailRectangleGridViewAdapter;
import com.nwk.locopromo.model.Retail;
import com.nwk.locopromo.model.Wrapper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

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
        finish();
    }

    @InjectView(R.id.progress)
    CircularProgressBar progressBar;

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
        ((PromoApplication)(getApplication())).getService().listRetails(new Callback<Wrapper<List<Retail>>>() {
            @Override
            public void success(Wrapper<List<Retail>> retails, Response response) {
                Timber.d("Size: " + retails.getResults().size());
                adapter.addItems(retails.getResults());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}

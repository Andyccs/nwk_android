package com.nwk.locopromo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nwk.core.api.BackendService;
import com.nwk.core.model.GrabPromotion;
import com.nwk.locopromo.adapter.RedeemHistoryListViewAdapter;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.OldPromotion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class ProfileActivity extends ActionBarActivity {

    @InjectView(R.id.history_list)
    ListView historyList;

    @InjectView(R.id.name)
    TextView name;

    @InjectView(R.id.email_address)
    TextView emailAddress;

    @InjectView(R.id.total_point)
    TextView point;

    @InjectView(R.id.profile_picture)
    ImageView profilePicture;

    @InjectView(R.id.progress)
    CircularProgressBar progressBar;

    @InjectView(R.id.placeholder)
    TextView placeholder;

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

        name.setText(CredentialPreferences.getUsername(this));
        emailAddress.setText(CredentialPreferences.getEmail(this));
        point.setText(""+CredentialPreferences.getPoint(this));
        Picasso.with(this).load(CredentialPreferences.getPicture(this)).into(profilePicture);

        initializeData();
    }


    private void initializeData() {
        BackendService service = ((PromoApplication)getApplication()).getService();
        service.getGrabHistory(
                ""+CredentialPreferences.getPrimaryKey(this),
                new Callback<List<GrabPromotion>>() {
                    @Override
                    public void success(List<GrabPromotion> grabPromotions, Response response) {
                        progressBar.setVisibility(View.GONE);
                        adapter.setPromotions(grabPromotions);
                        adapter.notifyDataSetChanged();

                        if(grabPromotions.size()>0) {
                            placeholder.setVisibility(View.GONE);
                        }else{
                            placeholder.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                });

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

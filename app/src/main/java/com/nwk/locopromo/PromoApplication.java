package com.nwk.locopromo;

import android.app.Application;

import com.nwk.locopromo.R;
import com.nwk.locopromo.api.BackendService;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import retrofit.RestAdapter;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class PromoApplication extends Application {
    BackendService service;
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault("fonts/Roboto-Medium.ttf", R.attr.fontPath);

        Timber.plant(new Timber.DebugTree());
        Parse.initialize(this, "t0LKj3AP9JFjwRZNfUQun9DLgHnepOc24FvhrQeo", "DhhFgeimW4YYScVOuLIuZ9wA07LAHIEhWQQLiFgW");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.1.186:8000/nwk/")
                .build();

        service = restAdapter.create(BackendService.class);
    }

    public BackendService getService() {
        return service;
    }
}

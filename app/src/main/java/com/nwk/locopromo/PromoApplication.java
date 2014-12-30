package com.nwk.locopromo;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nwk.core.Constant;
import com.nwk.core.api.ApiRequestInterceptor;
import com.nwk.core.api.BackendService;
import com.nwk.core.model.Promotion;
import com.nwk.core.serializer.PromotionSerializer;
import com.parse.Parse;
import com.parse.ParseInstallation;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
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

    }

    public BackendService getService() {
        return service;
    }

    public void setService(String token){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Promotion.class, new PromotionSerializer())
                .create();

        ApiRequestInterceptor interceptor = new ApiRequestInterceptor(token);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(interceptor)
                .build();

        service = restAdapter.create(BackendService.class);
    }
}

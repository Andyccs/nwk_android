package com.nwk.merchant;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nwk.core.api.BackendService;
import com.nwk.core.model.Promotion;
import com.nwk.core.serializer.PromotionSerializer;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

/**
 * Created by Andy on 12/25/2014.
 */
public class MerchantApplication extends Application {
    BackendService service;
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Promotion.class, new PromotionSerializer())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.1.186:8000/nwk/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        service = restAdapter.create(BackendService.class);
    }

    public BackendService getService() {
        return service;
    }
}

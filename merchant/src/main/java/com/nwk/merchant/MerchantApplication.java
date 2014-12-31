package com.nwk.merchant;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nwk.core.Constant;
import com.nwk.core.api.ApiRequestInterceptor;
import com.nwk.core.api.BackendService;
import com.nwk.core.model.CredentialPreferences;
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

    }
    public BackendService getService() {
        if(service==null){
            setService(
                    CredentialPreferences.getAccessToken(getApplicationContext())
            );
        }
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

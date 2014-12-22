package com.nwk.locopromo.api;

import com.nwk.locopromo.model.Consumers;
import com.nwk.locopromo.model.Promotion;
import com.nwk.locopromo.model.Retail;
import com.nwk.locopromo.model.Wrapper;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Andy on 12/22/2014.
 */
public interface BackendService {
    @GET("/retails")
    void listRetails(Callback<Wrapper<List<Retail>>> retails);

    @GET("/consumers?user_url={user_url}")
    Consumers getConsumerByUrl(@Path("user_url") String userUrl);

    @GET("/retails/{pk}/all_promotions")
    void listPromotionsByRetail(@Path("pk") int primaryKey, Callback<List<Promotion>> promotions);

}

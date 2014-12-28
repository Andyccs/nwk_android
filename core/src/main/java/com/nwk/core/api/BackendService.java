package com.nwk.core.api;

import com.nwk.core.model.Consumers;
import com.nwk.core.model.Promotion;
import com.nwk.core.model.Retail;
import com.nwk.core.model.Wrapper;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Andy on 12/22/2014.
 */
public interface BackendService {
    public interface Category{
        public static final String FOOD = "FOOD";
        public static final String FASHION = "FASHION";
        public static final String LIFESTYLE = "LIFESTYLE";
        public static final String OTHER = "OTHER";
    }

    @GET("/retails")
    void listRetails(@Query("category") String category, Callback<List<Retail>> retails);

    @GET("/consumers?user_url={user_url}")
    Consumers getConsumerByUrl(@Path("user_url") String userUrl);

    @GET("/retails/{pk}/all_promotions")
    void listPromotionsByRetail(@Path("pk") int primaryKey, Callback<List<Promotion>> promotions);

}

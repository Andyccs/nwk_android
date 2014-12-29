package com.nwk.core.api;

import com.nwk.core.model.Consumers;
import com.nwk.core.model.GrabPromotion;
import com.nwk.core.model.Promotion;
import com.nwk.core.model.Retail;
import com.nwk.core.model.Wrapper;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
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
    void listRetails(@Query("category") String category, Callback<List<Retail>> retailsCallback);

    @GET("/consumers/{user_url}")
    Consumers getConsumerByUrl(@Path("user_url") String userUrl);

    @GET("/retails/{pk}/all_promotions")
    void listPromotionsByRetail(@Path("pk") int primaryKey, Callback<List<Promotion>> promotionsCallback);

    @GET("/consumers/{user_url}/favorite_shops/")
    void listConsumerFavoriteRetails(@Path("user_url") String userUrl,
                                     Callback<List<Retail>> promotions);

    @FormUrlEncoded
    @PUT("/consumers/{customer_url}/")
    Consumers updateFavoriteRetailsOfConsumer(@Path("customer_url") String customerUrl,
                                       @Field("user") String user,
                                       @Field("favorite_shops") List<String> favoriteShopsCallback);

    public interface isApproved{
        public static final String WAITING = "None";
        public static final String YES = "True";
        public static final String NO = "False";
    }

    @FormUrlEncoded
    @POST("/grab_promotions/")
    void grabPromotions(@Field("consumer") String consumerUrl,
                        @Field("promotion") String promotionUrl,
                        @Field("is_approved") String isApproved,
                        Callback<GrabPromotion> grabPromotionCallback);

    @GET("/consumers/{customer_url}/grab_history/")
    void getGrabHistory(@Path("customer_url") String customerUrl,Callback<List<GrabPromotion>> grabPromotionListCallback);
}

package com.nwk.core.api;

import com.nwk.core.model.Consumer;
import com.nwk.core.model.Retail;
import com.nwk.core.model.User;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by andyccs on 31/12/14.
 */
public interface UserService {
    @GET("/users")
    public Consumer getConsumerByUsername(@Query("username") String username);

    @GET("/users")
    public Retail getRetailByUsername(@Query("username") String username);

    @GET("/users/{id}")
    public User getUserById(@Path("id") String id);
}

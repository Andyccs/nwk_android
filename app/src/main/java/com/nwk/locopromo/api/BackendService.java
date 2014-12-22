package com.nwk.locopromo.api;

import com.nwk.locopromo.model.Retail;
import com.nwk.locopromo.model.Wrapper;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Andy on 12/22/2014.
 */
public interface BackendService {
    @GET("/retails")
    void listRetails(Callback<Wrapper<List<Retail>>> retails);
}

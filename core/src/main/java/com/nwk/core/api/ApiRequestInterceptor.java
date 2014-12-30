package com.nwk.core.api;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;
import timber.log.Timber;

/**
 * Interceptor used to authorize requests.
 */
public class ApiRequestInterceptor implements RequestInterceptor {

    private String accessToken;

    public ApiRequestInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void intercept(RequestFacade request) {
        // Add the access_token to this request as the "Authorization"
        // header.
        request.addHeader("Authorization", "Bearer " + accessToken);
    }
}
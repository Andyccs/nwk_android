package com.nwk.core.api;

import retrofit.RequestInterceptor;

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
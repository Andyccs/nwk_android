package com.nwk.core.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nwk.core.model.Token;

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

public class Oauth2Util {

    public static Token getToken(
        Client client, String tokenIssuingEndpoint,
        String username, String password, String clientId,
        String clientSecret) throws IOException {
        // This code below programmatically builds an OAuth 2.0 password
        // grant request and sends it to the server.

        // Encode the username and password into the body of the request.
        FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
        to.addField("username", username);
        to.addField("password", password);

        // Add the client ID and client secret to the body of the request.
        to.addField("client_id", clientId);
        to.addField("client_secret", clientSecret);

        // Indicate that we're using the OAuth Password Grant Flow
        // by adding grant_type=password to the body
        to.addField("grant_type", "password");

        List<Header> headers = new ArrayList<Header>();

        // Create the actual password grant request using the data above
        Request req = new Request("POST", tokenIssuingEndpoint, headers, to);

        // Request the password grant.
        Response resp = client.execute(req);

        // Make sure the server responded with 200 OK
        if (resp.getStatus() < 200 || resp.getStatus() > 299) {
            // If not, we probably have bad credentials
            throw new SecuredRestException("Login failure: "
                    + resp.getStatus() + " - " + resp.getReason(),resp.getStatus());
        }

        // Extract the string body from the response
        String body = IOUtils.toString(resp.getBody().in());

        // Extract the access_token (bearer token) from the response so that we
        // can add it to future requests.
        Token token = new Gson().fromJson(body, Token.class);

        return token;
    }

    public static Token getTokenUsingRefreshToken(
            Client client, String tokenIssuingEndpoint,
            String refreshToken, String clientId,
            String clientSecret) throws IOException {
        // This code below programmatically builds an OAuth 2.0 password
        // grant request and sends it to the server.

        // Encode the username and password into the body of the request.
        FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();

        // Add the client ID and client secret to the body of the request.
        to.addField("client_id", clientId);
        to.addField("client_secret", clientSecret);

        // Indicate that we're using the OAuth Password Grant Flow
        // by adding grant_type=password to the body
        to.addField("grant_type", "refresh_token");
        to.addField("refresh_token",refreshToken);

        List<Header> headers = new ArrayList<Header>();

        // Create the actual password grant request using the data above
        Request req = new Request("POST", tokenIssuingEndpoint, headers, to);

        // Request the password grant.
        Response resp = client.execute(req);

        // Make sure the server responded with 200 OK
        if (resp.getStatus() < 200 || resp.getStatus() > 299) {
            // If not, we probably have bad credentials
            throw new SecuredRestException("Login failure: "
                    + resp.getStatus() + " - " + resp.getReason(),resp.getStatus());
        }

        // Extract the string body from the response
        String body = IOUtils.toString(resp.getBody().in());

        // Extract the access_token (bearer token) from the response so that we
        // can add it to future requests.
        Token token = new Gson().fromJson(body, Token.class);

        return token;
    }
}
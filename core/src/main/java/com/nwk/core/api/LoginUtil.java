package com.nwk.core.api;

import android.content.Context;

import com.nwk.core.Constant;
import com.nwk.core.model.Consumer;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.Retail;
import com.nwk.core.model.Token;
import com.nwk.core.model.User;

import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import timber.log.Timber;

/**
 * Created by andyccs on 30/12/14.
 */
public class LoginUtil {

    public static enum Role{
        CONSUMER, RETAIL
    }

    public static boolean login(Context context, String username, String password,Role role) throws IOException {
        Token token = null;
        Timber.d("logging in");
        try {
            token = Oauth2Util.getToken(
                    new ApacheClient(), Constant.TOKEN_END_POINT,
                    username, password,
                    Constant.CLIENT_ID, Constant.CLIENT_SECRET);
            Timber.d("token: "+token.getAccessToken());
        }catch (SecuredRestException e){
            Timber.e(e.getMessage());
            return false;
        }
        CredentialPreferences.saveToken(context,token.getAccessToken(),token.getRefreshToken());
        CredentialPreferences.saveFirstTime(context,true);

        ApiRequestInterceptor interceptor = new ApiRequestInterceptor(token.getAccessToken());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(interceptor)
                .build();
        UserService service = restAdapter.create(UserService.class);

        if(role==Role.CONSUMER) {
            Consumer consumer = service.getConsumerByUsername(username);
            User user = service.getUserById("" + UrlUtil.getUserPrimaryKeyByUrl(consumer.getUser()));

            CredentialPreferences.saveUserCredential(
                    context, consumer.getUser(),
                    consumer.getId(), user.getUsername(), user.getEmail(),
                    consumer.getPicture(),
                    consumer.getPoint()
            );
        }
        else if(role==Role.RETAIL){
            Retail retail = service.getRetailByUsername(username);
            User user = service.getUserById("" + UrlUtil.getUserPrimaryKeyByUrl(retail.getUser()));

            //TODO make another Credentail Preferences for Retail
            CredentialPreferences.saveUserCredential(
                    context, retail.getUser(),
                    retail.getId(), user.getUsername(), user.getEmail(),
                    retail.getLogoUrl(),
                    0
            );
        }

        return true;
    }

    public static boolean logout(Context context){
        CredentialPreferences.clearToken(context);
        CredentialPreferences.clearFirstTime(context);
        CredentialPreferences.clearUserCredential(context);
        return true;
    }

    public static boolean isLoggedInBefore(Context context){
        if(CredentialPreferences.getAccessToken(context)==null){
            return false;
        }
        return true;
    }
}

package com.nwk.core.api;

import android.content.Context;

import com.nwk.core.Constant;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.Token;

import java.io.IOException;

import retrofit.client.ApacheClient;
import timber.log.Timber;

/**
 * Created by andyccs on 30/12/14.
 */
public class LoginUtil {

    public static boolean login(Context context, String username, String password) throws IOException {
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

        //TODO replace this
        CredentialPreferences.saveUserCredential(
                context, "http://192.168.0.105:8000/nwk/users/10/",
                1, "andyccs", "andyccs@gmail.com",
                "http://twimgs.com/informationweek/galleries/automated/879/01_Steve-Jobs_full.jpg",
                999
        );

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

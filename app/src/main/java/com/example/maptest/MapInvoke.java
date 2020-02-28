package com.example.maptest;

import android.app.Application;

import com.mapbox.mapboxsdk.MapmyIndia;
import com.mmi.services.account.MapmyIndiaAccountManager;


public class MapInvoke extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String restKey="5ew84y7opzxb91ewj4z6o2zkz1g3ch9g";
        String mapKey="85zbf7y5i8fqk5yf8xl9bk5mxlo9eaol";
        String clientID="7UJsJ538S2Fl1G96PsEzAuUhHbXpHVx1eAPAf-UvflU-DEohvE1VVz6kpKRWG0Dr4mFdnvdA1Bd3q0XngkeOKg==";
        String clientSecret="NdJUAD9O1c2m2fe6yCeNfwwD8V6Gzq1rJPZ6swrYEqdtrojcksUQdlWQ2EMttbaUJMBnYrppRWkwN9LxtrXYjMqDiv9SQnDD";
        String credentials="client_credentials";
        MapmyIndiaAccountManager.getInstance().setRestAPIKey(restKey);
        MapmyIndiaAccountManager.getInstance().setMapSDKKey(mapKey);
        MapmyIndiaAccountManager.getInstance().setAtlasClientId(clientID);
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(clientSecret);
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType(credentials);
        MapmyIndia.getInstance(this);
    }

}

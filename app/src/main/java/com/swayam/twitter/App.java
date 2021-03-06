package com.swayam.twitter;

import android.app.Application;

import com.parse.Parse;

public class App extends Application{

    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_id))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }
}
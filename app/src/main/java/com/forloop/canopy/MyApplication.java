package com.forloop.canopy;

/**
 * Created by Jibola on 1/6/2017.
 */

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.paperdb.Paper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Paper.init(getApplicationContext());
    }
}

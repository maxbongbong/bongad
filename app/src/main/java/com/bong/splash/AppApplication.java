package com.bong.splash;

import android.app.Application;

import com.bong.splash.BuildConfig;
import com.facebook.stetho.Stetho;

public class AppApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);

    }
}

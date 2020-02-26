package com.bong.fragment;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);
    }
}

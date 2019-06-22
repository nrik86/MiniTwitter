package com.enriquejimenez.minitwitter.utils;

import android.app.Application;
import android.content.Context;

public class MiniTwitterApp extends Application {

    private static MiniTwitterApp instance;

    public static MiniTwitterApp getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}

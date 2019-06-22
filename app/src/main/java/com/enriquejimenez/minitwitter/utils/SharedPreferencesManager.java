package com.enriquejimenez.minitwitter.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTNGS_FILE = "APP_SETTINGS";

    private SharedPreferencesManager(){}

    private static SharedPreferences getSharedPreferences(){
        return MiniTwitterApp.getContext()
                .getSharedPreferences(APP_SETTNGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setString(String label, String value){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(label, value);
        editor.commit();
    }

    public static void setBoolean(String label, boolean value){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(label, value);
        editor.commit();
    }

    public static String getString(String label){
        return getSharedPreferences().getString(label,null);
    }

    public static boolean getBoolean(String label){
        return getSharedPreferences().getBoolean(label,false);
    }
}


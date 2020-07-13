package com.example.cats.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {
    public static String SOUND_KEY = "CATS_USE_SOUND";
    public static String CONTROLS_KEY = "CATS_USE_CONTROLS";

    public static void write(String key, Boolean value, Activity activity){
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean read(String key, Activity activity){
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        if(key.equals(SOUND_KEY))
            return prefs.getBoolean(key, true);
        else
            return prefs.getBoolean(key, false);
    }
}

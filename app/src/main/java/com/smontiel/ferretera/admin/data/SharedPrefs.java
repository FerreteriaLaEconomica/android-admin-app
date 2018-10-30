package com.smontiel.ferretera.admin.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Salvador Montiel on 29/10/18.
 */

public class SharedPrefs {
    private static SharedPrefs INSTANCE;

    public static SharedPrefs getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new SharedPrefs(context);
        return INSTANCE;
    }

    private final SharedPreferences prefs;

    private SharedPrefs(Context context) {
        prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }
}

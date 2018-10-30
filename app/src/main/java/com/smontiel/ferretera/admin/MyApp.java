package com.smontiel.ferretera.admin;

import android.app.Application;
import android.content.Context;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}

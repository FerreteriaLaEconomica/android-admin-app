package com.smontiel.ferretera.admin;

import android.app.Application;
import android.content.Context;

import com.google.firebase.messaging.FirebaseMessaging;

import timber.log.Timber;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this.getApplicationContext();
        Timber.plant(new Timber.DebugTree());
        FirebaseMessaging.getInstance().subscribeToTopic("inventario");
    }

    public static Context getContext() {
        return context;
    }
}

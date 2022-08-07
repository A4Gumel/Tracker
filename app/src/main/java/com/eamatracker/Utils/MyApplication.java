package com.eamatracker.Utils;

import android.app.Application;

import com.eamatracker.Service.MyFirebaseMessagingService;
import com.google.firebase.FirebaseError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }
}

package com.pushbots.notificationshistory;

import android.app.Application;

import com.pushbots.push.Pushbots;

/**
 * Created by Muhammad on 1/17/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Pushbots.sharedInstance().init(this);

    }
}

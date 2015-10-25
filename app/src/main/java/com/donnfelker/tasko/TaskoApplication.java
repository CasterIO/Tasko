package com.donnfelker.tasko;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public abstract class TaskoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        //Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
    }
}

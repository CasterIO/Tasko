package com.donnfelker.tasko;

import android.app.Application;
import android.content.Intent;

import com.donnfelker.tasko.services.CurrentConditionService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public abstract class TaskoApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                            .applicationModule(new ApplicationModule(this))
                            .apiModule(new ApiModule())
                            .build();

        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("tasko.realm")
                .build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        // Check the current conditions
        startService(new Intent(this, CurrentConditionService.class));
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}

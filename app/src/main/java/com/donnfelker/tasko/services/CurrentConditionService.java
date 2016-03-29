package com.donnfelker.tasko.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.donnfelker.tasko.TaskoApplication;
import com.donnfelker.tasko.http.ForecastListener;
import com.donnfelker.tasko.http.ForecastService;
import com.donnfelker.tasko.http.ForecastServiceImpl;
import com.donnfelker.tasko.models.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import io.forecast.models.Forecast;
import io.realm.Realm;
import retrofit.RestAdapter;


public class CurrentConditionService extends Service {

    private static final String TAG = CurrentConditionService.class.getSimpleName();

    @Inject ForecastService forecastService;

    @Override
    public void onCreate() {
        super.onCreate();
        ((TaskoApplication)getApplication()).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Attempting to get current conditions.");

        // Forecast service is an ASYNC call.
        // hard coded to NYC. In prod: Use a location listener to get the GPS coords of the user.
        forecastService.getForecastFor("40.7146", "-74.0072", new ForecastListener() {
            @Override
            public void onForecastLoaded(Forecast forecast) {

                if(forecast != null) {
                    Log.d(TAG, "Forecast loaded.");
                }

                if(forecast != null && forecast.getCurrently() != null && forecast.getCurrently().getIcon() != null) {
                    // icon options: clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
                    String icon = forecast.getCurrently().getIcon();

                    String dateFormat = "MM/dd/yyyy";
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);

                    // Add a task to fire up the snow blower.
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    Task task = realm.createObject(Task.class);
                    task.setUuid(UUID.randomUUID().toString());
                    task.setName(String.format("%s Weather: %s", dateFormatter.format(Calendar.getInstance().getTime()), icon));
                    task.setDescription("Forecast.io said we're getting " + icon + ".");
                    realm.commitTransaction();
                }

                stopSelf();
            }

            @Override
            public void onForecastFailed(Exception e) {
                Log.e(TAG, e.getMessage(), e);
                stopSelf();
            }
        });

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

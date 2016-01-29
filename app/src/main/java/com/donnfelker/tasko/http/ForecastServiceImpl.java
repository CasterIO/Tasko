package com.donnfelker.tasko.http;

import android.os.AsyncTask;

import io.forecast.models.Forecast;
import retrofit.RestAdapter;

public class ForecastServiceImpl implements ForecastService {

    private RestAdapter restAdapter;

    public ForecastServiceImpl(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    @Override
    public void getForecastFor(final String latitude, final String longitude, final ForecastListener forecastListener) {
        new AsyncTask<Void, Void, Forecast>() {

            public Exception e;

            @Override
            protected Forecast doInBackground(Void... params) {
                try {
                    return restAdapter.create(ForecastApiService.class).getForecast(latitude, longitude);
                } catch (Exception e) {
                    this.e = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Forecast forecast) {
                super.onPostExecute(forecast);
                if(forecastListener != null) {
                    if(e != null) {
                        forecastListener.onForecastFailed(e);
                    } else {
                        forecastListener.onForecastLoaded(forecast);
                    }
                }
            }
        }.execute();
    }
}

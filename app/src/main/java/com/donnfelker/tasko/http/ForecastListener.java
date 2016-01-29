package com.donnfelker.tasko.http;

import io.forecast.models.Forecast;

public interface ForecastListener {
    void onForecastLoaded(Forecast forecast);
    void onForecastFailed(Exception e);
}

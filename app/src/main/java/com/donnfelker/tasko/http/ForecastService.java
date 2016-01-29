package com.donnfelker.tasko.http;

public interface ForecastService {
    void getForecastFor(String latitude, String longitude, ForecastListener forecastListener);
}

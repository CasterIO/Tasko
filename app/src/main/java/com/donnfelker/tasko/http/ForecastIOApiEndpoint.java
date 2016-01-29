package com.donnfelker.tasko.http;

import retrofit.Endpoint;

public class ForecastIOApiEndpoint implements Endpoint {
    private static final String BASE = "https://api.forecast.io/forecast/";

    private String url;

    public ForecastIOApiEndpoint setApiKey(String apiKey) {
        url = BASE + apiKey;
        return this;
    }

    @Override public String getName() {
        return "default Forecast.io endpoint";
    }

    @Override public String getUrl() {
        if (url == null) throw new IllegalStateException("API key not set.");
        return url;
    }
}

package io.forecast.models;


public class Forecast
{
    private String timezone;

    private Currently currently;

    private String longitude;

    private String latitude;

    public String getTimezone ()
    {
        return timezone;
    }

    public void setTimezone (String timezone)
    {
        this.timezone = timezone;
    }


    public Currently getCurrently ()
    {
        return currently;
    }

    public void setCurrently (Currently currently)
    {
        this.currently = currently;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

}

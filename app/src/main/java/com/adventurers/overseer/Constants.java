package com.adventurers.overseer;

public class Constants {
    public static final int RC_ACCESS_FINE_LOCATION = 4003;
    public static final int RC_GPS_SERVICE = 4004;
    public static final int RC_CAMERA = 4005;
    public static final int RC_WRITE_EXTERNAL_STORAGE = 4006;

    public static final int EC_SERVER_FAILED = 4444;
    public static final String EM_SERVER_FAILED = "Can't connect to server.";

    public static String TAG_FLOOD_FORECAST_DETAILED_FRAGMENT = "FloodForecastDetailedFragment";
    public static String TAG_REPORT = "ReportModule";

    public static String BASE_URL_OVERSEER = "http://192.168.254.102:8000/";
    public static String BASE_URL_WEATHER = "https://api.openweathermap.org/";

    public static String OPENWEATHER_APPID = "67aa636d02df1df62ef01de2db58fa49";
}

package com.adventurers.overseer;

public class Constants {
    // Request Codes
    public static final int RC_ACCESS_FINE_LOCATION = 4001;
    public static final int RC_GPS_SERVICE = 4002;
    public static final int RC_CAMERA = 4003;
    public static final int RC_WRITE_EXTERNAL_STORAGE = 4004;
    public static final int RC_SEARCH_TYPE = 4005;

    // Error Codes
    public static final int EC_SERVER_FAILED = 4444;
    public static final int EC_SERVER_ERROR = 4445;
    public static final int EC_ACCOUNT_NOT_EXIST = 4446;
    public static final int EC_WRONG_PASSWORD = 4447;
    public static final int EC_EMAIL_REGISTERED = 4448;

    // Error Messages
    public static final String EM_SERVER_FAILED = "Can't connect to server.";
    public static final String EM_SERVER_ERROR = "Something went wrong.";
    public static final String EM_ACCOUNT_NOT_EXIST = "Account does not exist.";
    public static final String EM_WRONG_PASSWORD = "Wrong password.";
    public static final String EM_EMAIL_REGISTERED = "Email is already registered.";

    // Tags
    public static final String TAG_FLOOD_FORECAST_MODULE = "FloodForecastModule";
    public static final String TAG_REPORT = "ReportModule";

    // APIs
    public static String BASE_URL_OVERSEER = "http://192.168.1.2:8000/";
    public static final String BASE_URL_WEATHER = "https://api.openweathermap.org/";

    // Keys
    public static final String MAPS_API_KEY = "AIzaSyC-Aad2Zl-pfaVAJg5VnJUyflfdjJogjIY";
    public static final String OPEN_WEATHER_APP_ID = "67aa636d02df1df62ef01de2db58fa49";
    public static final String preferencesKey = "com.adventurers.overseer";
}

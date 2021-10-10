package com.adventurers.overseer.helpers;

public class TemperatureHelper {
    public static int toCelsiusInt(double kelvin) {
        return (int)(kelvin - 273.15);
    }
}

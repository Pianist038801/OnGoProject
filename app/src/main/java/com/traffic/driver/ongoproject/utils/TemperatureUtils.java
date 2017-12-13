package com.traffic.driver.ongoproject.utils;

public class TemperatureUtils {
    public static int KelvinToCesius(float kelvin) {
        return (int) Math.ceil(kelvin - 273.15);
    }

    public static int KelvinToFahrenheit(float kelvin) {
        return (int) Math.ceil(kelvin * (9.0f / 5.0f) - 459.67f);
    }
}

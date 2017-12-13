package com.traffic.driver.ongoproject.utils;

public class SpeedUtils {
    public static int toKmph(float mps) {
        return Math.round(mps * 3600.0f / 1000.0f);
    }

    public static int toMph(float mps) {
        return Math.round(mps * 3600.0f * 0.000621371f);
    }

    public static float toKM(float m) {
        return m / 1000.0f;
    }

    public static float toMiles(float meters) {
        return meters*0.000621371f;
    }
}

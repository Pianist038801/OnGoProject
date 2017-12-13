package com.traffic.driver.ongoproject.utils;

import android.location.Location;

public class LocationsUtils {
    private static final int earthRadius = 6371;

    public static float calculateDistance(Location start, Location end) {
        float e = (float) start.getLatitude();
        float f = (float) start.getLongitude();
        float g = (float) end.getLatitude();
        float h = (float) end.getLongitude();

        float dLat = (float) Math.toRadians(g - e);
        float dLon = (float) Math.toRadians(h - f);
        float a = (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(e))
                * Math.cos(Math.toRadians(g)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = earthRadius * c;
        return d * 1000;
    }

    public static long timeSpan(Location start, Location end) {
        return end.getTime() - start.getTime();
    }

    public static float getSpeed(Location start, Location end) {
        float distance = calculateDistance(start, end);
        float time = (float) (end.getTime() - start.getTime()) / 1000.0f;
        return distance / time;
    }
}
package com.traffic.driver.ongoproject;

import android.graphics.Typeface;

public class AppConfigs {
    private static AppConfigs _instance;

    private AppConfigs() {
        IS_METRIC_SYSTEM_UNIT = true;
    }

    public static AppConfigs getInstance() {
        if (_instance == null) {
            _instance = new AppConfigs();
        }

        return _instance;
    }

    public boolean IS_METRIC_SYSTEM_UNIT;
    public Typeface ROBOTO_THIN;
    public Typeface ROBOTO_CONDENSED_REGULAR;
    public String OPEN_WHEATHER_MAP_API_KEY;
}

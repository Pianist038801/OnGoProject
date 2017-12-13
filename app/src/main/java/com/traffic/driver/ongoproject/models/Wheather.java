package com.traffic.driver.ongoproject.models;

import com.traffic.driver.ongoproject.AppConfigs;
import com.traffic.driver.ongoproject.utils.TemperatureUtils;

import java.util.List;

public class Wheather {

    public String name; //City name
    public MainData main;
    public List<Weather> weather;

    public int getTemperature() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                TemperatureUtils.KelvinToCesius(main.temp) :
                TemperatureUtils.KelvinToFahrenheit(main.temp);
    }

    public String getTemperatureUnit() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                "\u00b0C" : "\u00b0F";
    }

    public class MainData {
        public float temp; //Kelvin
        public float pressure;
        public int humidity;
    }

    public class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }
}

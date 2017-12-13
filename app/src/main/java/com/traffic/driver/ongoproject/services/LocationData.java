package com.traffic.driver.ongoproject.services;

import android.location.Location;

import com.traffic.driver.ongoproject.AppConfigs;
import com.traffic.driver.ongoproject.models.Globals;
import com.traffic.driver.ongoproject.utils.LocationsUtils;
import com.traffic.driver.ongoproject.utils.SpeedUtils;

public class LocationData {
    private static LocationData _instance;

    private Location currLocation;
    private Location prevLocation;
    private float currSpeed; //Current Speed in m/s
    private float maxSpeed; //Max Speed in m/s
    private float avgSpeed; //Averge Speed in m/s
    private float distance; //Distance in meter
    private long time;
    private static float passDist;
    private static float failDist;
    private LocationData() {

    }

    public static LocationData getInstance() {
        if (_instance == null) {
            _instance = new LocationData();
            passDist = failDist = 0;
        }

        return _instance;
    }


    private int convertSpeed(float sp) {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                SpeedUtils.toKmph(sp) :
                SpeedUtils.toMph(sp);
    }

    public long getTime() {
        return time;
    }

    public String getSpeedUnit() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                "kmh" : "mph";
    }

    public String getDistanceUnit() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                "km" : "mi";
    }

    public int getCurrentSpeed() {
        return convertSpeed(currSpeed);
    }

    public float getDistance() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ? SpeedUtils.toKM(distance)
                : SpeedUtils.toMiles(distance);
    }

    public int getMaxSpeed() {
        return convertSpeed(maxSpeed);
    }

    public int getAvgSpeed() {
        return convertSpeed(avgSpeed);
    }

    public Location getCurrentLocation() {
        return currLocation;
    }

    public void setCurrLocation(Location location) {
        prevLocation = currLocation;
        currLocation = location;
        if (prevLocation != null) {
            this.currSpeed = LocationsUtils.getSpeed(prevLocation, currLocation);
            //this.currSpeed = meter per second
            if(this.currSpeed > 33.3333)
                failDist += LocationsUtils.calculateDistance(prevLocation, currLocation);
            else
                passDist  += LocationsUtils.calculateDistance(prevLocation, currLocation);
            if(failDist > 1000)//Bonus
            {
                failDist = failDist - 1000;
                Globals.score++;
            }
            if(passDist>1000)//Fine
            {
                Globals.score--;
                passDist = passDist - 1000;
            }
            if (this.maxSpeed < currSpeed) {
                this.maxSpeed = currSpeed;
            }

            this.distance += LocationsUtils.calculateDistance(prevLocation, currLocation);
            this.time += LocationsUtils.timeSpan(prevLocation, currLocation);
            int sec = (int) (this.time / 1000.0f);
            this.avgSpeed = this.distance / sec;
            this.avgSpeed = this.distance / (this.time / 1000.0f);
        }
    }

    public Location getPreviousLocation() {
        return prevLocation;
    }
}

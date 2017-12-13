package com.traffic.driver.ongoproject.models;

/**
 * Created by pianist on 12/12/17.
 */

public class UserInfo {
    public String email;
    public Integer score;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserInfo(String email, Integer score) {
        this.email = email;
        this.score = score;
    }
}
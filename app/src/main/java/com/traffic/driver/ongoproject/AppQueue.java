package com.traffic.driver.ongoproject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppQueue {
    private static AppQueue _instance;
    private Context context;
    private RequestQueue _request;

    private AppQueue(Context context) {
        this.context = context;
        _request = getRequestQueue();
    }

    public static synchronized AppQueue getInstance(Context context) {
        if (_instance == null) {
            _instance = new AppQueue(context);
        }
        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if (_request == null) {
            _request = Volley.newRequestQueue(context);
        }
        return _request;
    }

    public <T> void addRequest(Request<T> request) {
        getRequestQueue().add(request);
    }
}

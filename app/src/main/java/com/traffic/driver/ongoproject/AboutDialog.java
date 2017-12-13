package com.traffic.driver.ongoproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

public class AboutDialog extends Dialog {

    private Context context;

    public AboutDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_about);

    }
}

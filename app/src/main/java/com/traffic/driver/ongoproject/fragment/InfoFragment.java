package com.traffic.driver.ongoproject.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.traffic.driver.ongoproject.AppConfigs;
import com.traffic.driver.ongoproject.AppQueue;
import com.traffic.driver.ongoproject.R;
import com.traffic.driver.ongoproject.models.Globals;
import com.traffic.driver.ongoproject.models.UserInfo;
import com.traffic.driver.ongoproject.models.Wheather;
import com.traffic.driver.ongoproject.services.LocationData;
import com.traffic.driver.ongoproject.services.TrackingLocationService;
import com.traffic.driver.ongoproject.utils.ViewUtils;

public class InfoFragment extends Fragment {
    private TextView txtSpeed;
    private TextView txtRank;
    private BroadcastReceiver locationChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, 0);
            switch (action) {
                case TrackingLocationService.ACTION_LOCATION_CHANGED:
                    updateSpeedInfo();

                    break;
                default:
                    break;
            }
        }
    };

    private void updateSpeedInfo() {
        txtSpeed.setText(String.valueOf(Globals.score));
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserInfo _user = new UserInfo(Globals.currentUser.getEmail(), 0);
        mDatabase.child("users").child(Globals.currentUser.getUid()).setValue(_user);

    }
    ValueEventListener sortListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            Log.d("UpdateValue", dataSnapshot.toString());
            Integer rank = 1;
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").orderByChild("score");
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                UserInfo _user = (UserInfo)snapshot.getValue(UserInfo.class);
                if(_user.score<Globals.score) rank++;

            }
            txtRank.setText(String.valueOf(rank));
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message

            // ...
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        ViewUtils.setTypeface(AppConfigs.getInstance().ROBOTO_CONDENSED_REGULAR, view);
        txtSpeed = (TextView) view.findViewById(R.id.txtSpeed);
        txtRank = (TextView) view.findViewById(R.id.txtRank);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");
        myRef.addValueEventListener(sortListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
            locationChange, new IntentFilter(TrackingLocationService.LOCATION_SERVICE_UPDATE));
        //Update Speed info when resume
        if (LocationData.getInstance().getCurrentLocation() != null) {
            Intent intent = new Intent(TrackingLocationService.LOCATION_SERVICE_UPDATE);
            intent.putExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, TrackingLocationService.ACTION_LOCATION_CHANGED);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
        //update unit info

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationChange);
        super.onPause();
    }
}

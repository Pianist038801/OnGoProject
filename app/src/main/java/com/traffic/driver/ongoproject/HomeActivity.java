package com.traffic.driver.ongoproject;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.traffic.driver.ongoproject.models.Globals;
import com.traffic.driver.ongoproject.models.UserInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.traffic.driver.ongoproject.adapters.RecyclerViewItemClickListener;
import com.traffic.driver.ongoproject.fragment.CustomMapFragment;
import com.traffic.driver.ongoproject.fragment.DrawerFragment;
import com.traffic.driver.ongoproject.fragment.InfoFragment;
import com.traffic.driver.ongoproject.models.NavDrawerItem;
import com.traffic.driver.ongoproject.services.TrackingLocationService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DrawerFragment.GetNavItemsCallback {

    @Override
    public RecyclerViewItemClickListener getItemClickListener() {
        return new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                switch (position) {
                    case 0: //Home

                        break;
                    case 1: //LeaderBoard
                        Intent intent = new Intent(HomeActivity.this, LeaderBoardActivity.class);
                        startActivity(intent);
                        break;
                    case 2: //LogOut
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));

                }
                drawerFragment.closeNavDrawer();
            }
        };
    }

    @Override
    public List<NavDrawerItem> getNavItems() {
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem(R.drawable.ic_home, "Home"));
        items.add(new NavDrawerItem(R.drawable.ic_globe, "LeaderBoard"));
        items.add(new NavDrawerItem(R.drawable.ic_logout, "Log Out"));
        return items;
    }

    private View home_layout;
    private DrawerFragment drawerFragment;
    private Toolbar toolbar;
    private CustomMapFragment mapFragment;
    private InfoFragment infoFragment;
    private FragmentTransaction transaction;
    private FirebaseUser mUser;
    private Integer mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Get User
        String userString = getIntent().getExtras().getString("authUser");

        Gson gson = new Gson();

        mUser = Globals.currentUser;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start Location Service
        Intent locationService = new Intent(getBaseContext(), TrackingLocationService.class);
        startService(locationService);

        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);

        //Add MAP
        mapFragment = new CustomMapFragment();
        transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.mapContainer, mapFragment);
        //Add Info Fragment
        infoFragment = new InfoFragment();
        transaction.add(R.id.infoContainer, infoFragment);
        transaction.commit();

        //Firebase Setting
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(Globals.currentUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // mScore = dataSnapshot[mUser.getUid()].score;
                UserInfo _user = dataSnapshot.getValue(UserInfo.class);
                Globals.score = _user.score;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void addScore(int value){

        mScore += value;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserInfo _user = new UserInfo(mUser.getEmail(), mScore);
        mDatabase.child("users").child(mUser.getUid()).setValue(_user);

    }

    @Override
    protected void onDestroy() {
        Intent locationService = new Intent(getBaseContext(), TrackingLocationService.class);
        stopService(locationService);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}

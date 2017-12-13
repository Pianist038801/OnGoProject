package com.traffic.driver.ongoproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.traffic.driver.ongoproject.adapters.ListAdapter;
import com.traffic.driver.ongoproject.models.Globals;
import com.traffic.driver.ongoproject.models.ListItem;
import com.traffic.driver.ongoproject.models.UserInfo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {
    ValueEventListener sortListener = new ValueEventListener() {
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message

            // ...
        }
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI

            final ListView yourListView = (ListView) findViewById(R.id.leaderboard_list);
            final List<ListItem> m_List = new ArrayList<ListItem>();

            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();

            Query query = mDatabase.child("users").orderByChild("score").limitToLast(10);

            query.addValueEventListener(new ValueEventListener() {
                int i = 0;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Data is ordered by increasing height, so we want the first entry
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInfo _user = (UserInfo) snapshot.getValue(UserInfo.class);
                        ListItem lst = new ListItem();
                        lst.m_Rank = ++i;
                        lst.m_Email = _user.email;
                        lst.m_Score = -_user.score;
                        m_List.add(lst);
                    }
                    ListAdapter customAdapter = new ListAdapter(LeaderBoardActivity.this, R.layout.itemlist, m_List);

                    yourListView.setAdapter(customAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });




            // ...
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");
        myRef.addListenerForSingleValueEvent(sortListener);

        ListView yourListView = (ListView) findViewById(R.id.leaderboard_list);

// get data from the table by the ListAdapter
        List<ListItem> m_List = new ArrayList<ListItem>();

        ListAdapter customAdapter = new ListAdapter(this, R.layout.itemlist, m_List);

        yourListView.setAdapter(customAdapter);


    }

}

package com.traffic.driver.ongoproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.traffic.driver.ongoproject.R;
import com.traffic.driver.ongoproject.adapters.NavDrawerAdapter;
import com.traffic.driver.ongoproject.adapters.RecyclerViewItemClickListener;
import com.traffic.driver.ongoproject.models.NavDrawerItem;

import java.util.Collections;
import java.util.List;

public class DrawerFragment extends Fragment {

    private List<NavDrawerItem> navItems;

    public interface GetNavItemsCallback {
        /**
         * @return List các item trên Navigation
         */
        List<NavDrawerItem> getNavItems();

        /**
         * @return Callback khi item được click
         */
        RecyclerViewItemClickListener getItemClickListener();
    }

    private RecyclerView drawerList;
    private DrawerLayout drawerLayout;
    private NavDrawerAdapter drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private View container;
    private RecyclerViewItemClickListener itemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof GetNavItemsCallback) {
            navItems = ((GetNavItemsCallback) activity).getNavItems();
            itemClickListener = ((GetNavItemsCallback) activity).getItemClickListener();
        }
        if (navItems == null) {
            navItems = Collections.emptyList();
        }
    }

    public void closeNavDrawer() {
        drawerLayout.closeDrawer(container);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        drawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        drawerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        drawerAdapter = new NavDrawerAdapter(getActivity().getBaseContext(), navItems);
        drawerList.setAdapter(drawerAdapter);
        drawerAdapter.setOnItemClickListener(itemClickListener);
        return view;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        final Activity activity = getActivity();
        container = activity.findViewById(fragmentId);
        this.drawerLayout = drawerLayout;
        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

}
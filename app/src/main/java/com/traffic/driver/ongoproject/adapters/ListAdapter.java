package com.traffic.driver.ongoproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.traffic.driver.ongoproject.R;
import com.traffic.driver.ongoproject.models.ListItem;

import java.util.List;

/**
 * Created by pianist on 12/12/17.
 */

public class ListAdapter extends ArrayAdapter<ListItem> {
    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<ListItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlist, null);
        }

        ListItem p = getItem(position);

        if (p != null) {
            TextView rank = (TextView) v.findViewById(R.id.item_rank);
            TextView email = (TextView) v.findViewById(R.id.item_email);
            TextView score = (TextView) v.findViewById(R.id.item_score);

            if (rank != null) {
                rank.setText( String.format("%d",p.m_Rank) );
            }
            if (email != null) {
                email.setText(p.m_Email);
            }

            if (score != null) {
                score.setText( String.format("%d",p.m_Score) );
            }
        }

        return v;
    }

}

package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.PendingOrganizer;

import java.util.ArrayList;
import java.util.List;

public class LeaderAdapter extends ArrayAdapter<PendingOrganizer> {

    private Context context;
    private List<PendingOrganizer> organizerList;

    public LeaderAdapter(Context context, ArrayList<PendingOrganizer> organizerList) {
        super(context, 0, organizerList);
        this.context = context;
        this.organizerList = organizerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.leader_row_layout, parent, false);
        }

        PendingOrganizer currentOrganizer = organizerList.get(position);
        TextView organizerUsername = view.findViewById(R.id.organizerUsername);
        organizerUsername.setText(currentOrganizer.getUsername());

        TextView organizerFestivalName = view.findViewById(R.id.organizerFestival);
        organizerFestivalName.setText(currentOrganizer.getFestivalName());

        return view;
    }
}
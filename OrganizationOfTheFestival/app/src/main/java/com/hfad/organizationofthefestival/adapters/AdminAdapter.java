package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.hfad.organizationofthefestival.R;
import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private List<String> leaderList = new ArrayList<>();

    public AdminAdapter(Context context, int resource,  ArrayList<String> leaderList) {
        super(context, resource, leaderList);
        this.context = context;
        this.leaderList = leaderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_row_layout, parent, false);
        }

        String currentLeader = leaderList.get(position);
        TextView leaderUsername = view.findViewById(R.id.leaderUsername);
        leaderUsername.setText(currentLeader);
        return view;
    }
}

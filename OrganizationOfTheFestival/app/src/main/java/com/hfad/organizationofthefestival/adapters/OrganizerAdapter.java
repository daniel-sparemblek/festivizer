package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.Festival;
import com.hfad.organizationofthefestival.R;

import java.util.ArrayList;

public class OrganizerAdapter extends ArrayAdapter<Festival> {

    private ArrayList<Festival> festivals;
    Context context;

    private static class ViewHolder {
        TextView festivalName;
        Button appButton;
    }


    public OrganizerAdapter(ArrayList<Festival> festivals, Context context) {
        super(context, R.layout.organizer_row_layout, festivals);
        this.festivals = festivals;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Festival festival = festivals.get(position);
        final View result;
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.organizer_row_layout, parent, false);

            viewHolder.festivalName = convertView.findViewById(R.id.festivalName);
            viewHolder.appButton = convertView.findViewById(R.id.btnApply);

            result = convertView;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.festivalName.setText(festival.getName());
        return convertView;
    }

}

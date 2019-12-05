package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.Festival;
import com.hfad.organizationofthefestival.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrganizerAdapter extends BaseAdapter {

    private HashMap<String, Integer> mData = new HashMap<>();
    private String[] mKeys;
    private Context context;

    private static class ViewHolder {
        TextView festivalName;
        Button appButton;
    }


    public OrganizerAdapter(HashMap<String, Integer> data, Context context) {
        mData = data;
        mKeys = mData.keySet().toArray(new String[data.size()]);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Integer getItem(int position) {
        return mData.get(mKeys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String festival = mKeys[position];
        Integer value = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.organizer_row_layout, parent, false);

            viewHolder.festivalName = convertView.findViewById(R.id.festivalName);
            viewHolder.appButton = convertView.findViewById(R.id.btnApply);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.festivalName.setText(festival);
        if (value == -2)
            viewHolder.appButton.setText("Apply");
        else if (value == -1) {
            viewHolder.appButton.setText("Cancel");
        }
        return convertView;
    }

}

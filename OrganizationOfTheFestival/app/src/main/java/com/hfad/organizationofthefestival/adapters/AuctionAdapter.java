package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.ApplicationAuction;

import java.util.List;

public class AuctionAdapter extends ArrayAdapter<ApplicationAuction> {

    private Context context;
    private int resource;
    private List<ApplicationAuction> applicationList;

    public AuctionAdapter(Context context, int resource, List<ApplicationAuction> applicationList) {
        super(context, resource, applicationList);
        this.context = context;
        this.applicationList = applicationList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.organizer_screen_my_jobs_one_row, null);
        }

        ApplicationAuction application = applicationList.get(position);

        TextView endTime = view.findViewById(R.id.end_time);
        TextView jobName = view.findViewById(R.id.job_name);
        Button extButton = view.findViewById(R.id.btn_extend);


        endTime.setText(application.getEndTime());
        jobName.setText(String.valueOf(application.getJob().getName()));
        return view;
    }
}

package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.List;

public class AuctionAdapter extends ArrayAdapter<ApplicationResponse> {

    private Context context;
    private int resource;
    private List<ApplicationResponse> applicationList;

    public AuctionAdapter(Context context, int resource, List<ApplicationResponse> applicationList) {
        super(context, resource, applicationList);
        this.context = context;
        this.applicationList = applicationList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.leader_screen_job_auc_row_layout, null);
        }

        ApplicationResponse application = applicationList.get(position);

        TextView tvWorkerName = view.findViewById(R.id.tv_app_worker_name);
        TextView tvPrice = view.findViewById(R.id.tv_app_price);
        TextView tvPeopleNumber = view.findViewById(R.id.tv_app_people_num);
        TextView tvDuration = view.findViewById(R.id.tv_app_duration);
        TextView tvJobName = view.findViewById(R.id.tv_app_job_name);


        tvWorkerName.setText(application.getWorker().getUsername());
        tvPrice.setText(String.valueOf(application.getPrice()));
        tvPeopleNumber.setText(String.valueOf(application.getPeopleNumber()));
        tvDuration.setText(String.valueOf(application.getDuration()));
        tvJobName.setText(String.valueOf(application.getAuction().getJob().getName()));
        return view;
    }
}

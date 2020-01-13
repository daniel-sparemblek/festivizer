package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderAdapter> {

    private Context context;
    private int resource;
    private List<Job> JobOrder;

    public OrderAdapter(Context context, int resource, List<Job> jobsList) {
        super(context, resource);
        this.context = context;
        this.JobOrder = jobsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.organizer_order_row_layout, null);
        }

        Job jobs = JobOrder.get(position);

        TextView orderNumber = view.findViewById(R.id.order_number);
        TextView jobName = view.findViewById(R.id.order_job_name);


        orderNumber.setText(jobs.getOrderNumber());
        jobName.setText(jobs.getName());



        //getText!!! on button click

        return view;
    }
}

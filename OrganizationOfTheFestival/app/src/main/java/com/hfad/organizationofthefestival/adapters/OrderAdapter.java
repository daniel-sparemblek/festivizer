package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.ViewEventController;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderAdapter> {

    private Context context;
    private int resource;
    private List<Job> JobOrder;
    private List<String> orderList;
    private ViewEventController viewEventController;

    public OrderAdapter(Context context, int resource, List<Job> jobsList, ViewEventController viewEventController) {
        super(context, resource);
        this.context = context;
        this.JobOrder = jobsList;
        this.viewEventController = viewEventController;
        for(Job job : jobsList) {
            this.orderList.add(job.getOrderNumber());
        }
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
        Button orderUpdate = view.findViewById(R.id.btn_order_update);


        orderNumber.setText(jobs.getOrderNumber());
        jobName.setText(jobs.getName());


        orderUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderAdapter.this.viewEventController.updateJobOrders(orderList, Integer.toString(jobs.getEventId()));
            }
        });

        return view;
    }
}

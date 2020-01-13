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

import java.util.LinkedList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Job> {

    private Context context;
    private int resource;
    private List<Job> jobOrder;
    private List<String> orderList = new LinkedList<>();
    private ViewEventController viewEventController;

    public OrderAdapter(Context context, int resource, List<Job> jobsList, ViewEventController viewEventController) {
        super(context, resource);
        this.context = context;
        this.jobOrder = jobsList;
        this.viewEventController = viewEventController;
        for(Job job : jobsList) {
            this.orderList.add(job.getOrderNumber());
        }
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    public List<String> getItemOrders() {
        for(int i = 0; i < getCount(); i++) {

        }
        return orderList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.organizer_order_row_layout, null);
        }

        Job jobs = jobOrder.get(position);
        TextView orderNumber = view.findViewById(R.id.order_number);
        TextView jobName = view.findViewById(R.id.order_job_name);

        orderNumber.setText(jobs.getOrderNumber());
        jobName.setText(jobs.getName());

        viewEventController.saveList(orderList);

        return view;
    }
}

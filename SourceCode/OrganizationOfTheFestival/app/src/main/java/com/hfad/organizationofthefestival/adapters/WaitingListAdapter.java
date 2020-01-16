package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.WaitingListController;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.List;

public class WaitingListAdapter extends ArrayAdapter<ApplicationResponse> {

    private Context context;
    private int resource;
    private List<ApplicationResponse> applicationList;
    private WaitingListController controller;

    public WaitingListAdapter(Context context, int resource, List<ApplicationResponse> applicationList, WaitingListController controller) {
        super(context, resource, applicationList);
        this.context = context;
        this.applicationList = applicationList;
        this.controller = controller;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.organizer_screen_waiting_list_row, null);
        }

        ApplicationResponse application = applicationList.get(position);

        TextView tvWorkerName = view.findViewById(R.id.org_rowTxt);
        Button btnAcc = view.findViewById(R.id.org_rowBtn1);
        Button btnDec = view.findViewById(R.id.org_rowBtn2);

        tvWorkerName.setText(application.getWorker().getUsername());

        tvWorkerName.setOnClickListener(v -> {
            controller.go(application);
        });
        btnAcc.setOnClickListener(v -> controller.setApplicationStatus(application.getApplicationId(), 1));
        btnDec.setOnClickListener(v -> controller.setApplicationStatus(application.getApplicationId(), 2));

        return view;
    }
}

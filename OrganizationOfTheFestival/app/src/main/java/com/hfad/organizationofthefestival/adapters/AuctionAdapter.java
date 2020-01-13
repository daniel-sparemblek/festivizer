package com.hfad.organizationofthefestival.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.JobsController;
import com.hfad.organizationofthefestival.utility.ApplicationAuction;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AuctionAdapter extends ArrayAdapter<ApplicationAuction> {

    private Context context;
    private int resource;
    private List<ApplicationAuction> applicationList;
    private JobsController jobsController;

    public AuctionAdapter(Context context, int resource, List<ApplicationAuction> applicationList, JobsController jobsController) {
        super(context, resource, applicationList);
        this.context = context;
        this.applicationList = applicationList;
        this.jobsController = jobsController;
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

        endTime.setOnClickListener(v -> jobsController.goToWaitingList(String.valueOf(application.getJob().getId())));
        jobName.setOnClickListener(v -> jobsController.goToWaitingList(String.valueOf(application.getJob().getId())));

        extButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuctionAdapter.this.jobsController.extendAuction(application.getAuctionId());
            }
        });


        endTime.setText(parseDateTime(application.getEndTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        jobName.setText(String.valueOf(application.getJob().getName()));
        return view;
    }

    public ZonedDateTime parseDateTime(String dateTime) {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));
        int second = Integer.parseInt(dateTime.substring(17, 19));

        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }
}

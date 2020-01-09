package com.hfad.organizationofthefestival.organizer.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.JobsController;

public class AuctionJobsFrag extends Fragment {

    public AuctionJobsFrag() {
        // Required empty public constructor
    }
    public static AuctionJobsFrag newInstance() {
        AuctionJobsFrag fragment = new AuctionJobsFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_screen_my_jobs_rows, container, false);
    }
}
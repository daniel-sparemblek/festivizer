package com.hfad.organizationofthefestival.organizer.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.JobsController;

public class CompletedJobsFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String accessToken;
    private String username;


    private JobsController jobsController;

    public CompletedJobsFrag() {
        // Required empty public constructor
    }
    public static CompletedJobsFrag newInstance(String accessToken, String username) {
        CompletedJobsFrag fragment = new CompletedJobsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, accessToken);
        args.putString(ARG_PARAM2, username);
        fragment.setArguments(args);
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

    //@Override
    //public void setUserVisibleHint(boolean isVisibleToUser) {
    //    super.setUserVisibleHint(isVisibleToUser);
    //    if (isVisibleToUser){
    //    }
    //}
}
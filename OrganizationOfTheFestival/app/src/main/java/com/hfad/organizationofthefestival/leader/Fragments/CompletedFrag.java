package com.hfad.organizationofthefestival.leader.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.organizationofthefestival.R;

public class CompletedFrag extends Fragment {

    public CompletedFrag() {
        // Required empty public constructor
    }
    public static CompletedFrag newInstance() {
        CompletedFrag fragment = new CompletedFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_fragment_completed_jobs, container, false);
    }
}

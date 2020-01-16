package com.hfad.organizationofthefestival.leader.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.organizationofthefestival.R;

public class PendingFrag extends Fragment {

    public PendingFrag() {
        // Required empty public constructor
    }
    public static PendingFrag newInstance() {
        PendingFrag fragment = new PendingFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_fragment_pending_jobs, container, false);
    }
}

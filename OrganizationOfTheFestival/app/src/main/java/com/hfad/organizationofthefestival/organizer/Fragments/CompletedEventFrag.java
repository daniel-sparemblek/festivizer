package com.hfad.organizationofthefestival.organizer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.organizationofthefestival.R;

public class CompletedEventFrag extends Fragment {

    public CompletedEventFrag() {
        // Required empty public constructor
    }
    public static CompletedJobsFrag newInstance() {
        CompletedJobsFrag fragment = new CompletedJobsFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_screen_my_events_rows, container, false);
    }
}

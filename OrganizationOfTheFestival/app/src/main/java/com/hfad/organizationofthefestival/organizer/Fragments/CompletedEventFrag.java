package com.hfad.organizationofthefestival.organizer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.EventsActivity;
import com.hfad.organizationofthefestival.utility.EventApply;

import java.util.ArrayList;
import java.util.List;

public class CompletedEventFrag extends Fragment {

    private static EventsActivity eventsActivity;

    public CompletedEventFrag() {
        // Required empty public constructor
    }
    public static AuctionJobsFrag newInstance(EventsActivity eventsActivity) {
        AuctionJobsFrag fragment = new AuctionJobsFrag();
        CompletedEventFrag.eventsActivity = eventsActivity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventApply[] eventApplies = new EventApply[0]; //remove
        ListView thisIsATest = getView().findViewById(R.id.orgJobsList); //Using the same layout as jobs
        ArrayAdapter<String> testAdapter = new ArrayAdapter<>(eventsActivity,
                android.R.layout.simple_list_item_1, format(eventApplies));
        thisIsATest.setAdapter(testAdapter);

    }

    public List<String> format(EventApply[] events) {
        List<String> result = new ArrayList<>();

        for(EventApply event : events) {
            result.add(event.getName());
            System.out.println("POZIV");
            System.out.println(event.getName());
        }

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_screen_my_events_rows, container, false);
    }
}

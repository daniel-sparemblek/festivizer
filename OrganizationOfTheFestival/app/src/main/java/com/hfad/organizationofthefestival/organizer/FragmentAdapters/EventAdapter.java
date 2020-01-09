package com.hfad.organizationofthefestival.organizer.FragmentAdapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.EventsActivity;
import com.hfad.organizationofthefestival.organizer.Fragments.ActiveEventFrag;
import com.hfad.organizationofthefestival.organizer.Fragments.CompletedEventFrag;

public class EventAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.active, R.string.completed};
    private final EventsActivity mContext;


    public EventAdapter(EventsActivity context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                System.out.println("Ovdje sam usao 1");
                fragment = ActiveEventFrag.newInstance();
                break;
            case 1:
                System.out.println("Ovdje sam usao 2");
                fragment = CompletedEventFrag.newInstance(mContext);
                break;
        }
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}

package com.hfad.organizationofthefestival.organizer.FragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.Fragments.AuctionJobsFrag;
import com.hfad.organizationofthefestival.organizer.Fragments.ActiveJobsFrag;
import com.hfad.organizationofthefestival.organizer.Fragments.CompletedJobsFrag;
import com.hfad.organizationofthefestival.organizer.Fragments.PendingJobsFrag;
import com.hfad.organizationofthefestival.organizer.JobsActivity;


public class JobsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.auction, R.string.active, R.string.pending, R.string.completed};
    private final Context mContext;


    public JobsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = AuctionJobsFrag.newInstance();
                break;
            case 1:
                fragment = ActiveJobsFrag.newInstance();
                break;
            case 2:
                fragment = PendingJobsFrag.newInstance();
                break;
            case 3:
                fragment = CompletedJobsFrag.newInstance();
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
        return 4;
    }
}
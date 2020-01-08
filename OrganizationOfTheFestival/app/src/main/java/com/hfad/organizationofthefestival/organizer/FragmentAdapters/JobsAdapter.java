package com.hfad.organizationofthefestival.organizer.FragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.Fragments.CompletedJobsFrag;
import com.hfad.organizationofthefestival.organizer.JobsActivity;


public class JobsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.auction, R.string.active, R.string.pending, R.string.completed};
    private final Context mContext;
    private String accessToken;
    private String refreshToken;
    private String username;
    private JobsActivity jobsActivity;


    public JobsAdapter(Context context, FragmentManager fm, Intent intent, JobsActivity jobsActivity) {
        super(fm);
        mContext = context;
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        this.jobsActivity = jobsActivity;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = CompletedJobsFrag.newInstance(accessToken, username);
                System.out.println("KREIRAO SAM");
                break;
            case 1:
                //fragment = CompletedJobsFrag.newInstance(null, null);
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
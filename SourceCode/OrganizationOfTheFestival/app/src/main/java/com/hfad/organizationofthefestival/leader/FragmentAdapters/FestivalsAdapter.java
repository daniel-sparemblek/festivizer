package com.hfad.organizationofthefestival.leader.FragmentAdapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.leader.Fragments.ActiveFrag;
import com.hfad.organizationofthefestival.leader.Fragments.CompletedFrag;
import com.hfad.organizationofthefestival.leader.Fragments.PendingFrag;


public class FestivalsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.active, R.string.pending, R.string.completed};
    private final Context mContext;


    public FestivalsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = ActiveFrag.newInstance();
                break;
            case 1:
                fragment = PendingFrag.newInstance();
                break;
            case 2:
                fragment = CompletedFrag.newInstance();
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
        return 3;
    }
}
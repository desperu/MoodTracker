package org.desperu.moodtracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MoodAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_PAGES = 5;

    MoodAdapter(FragmentManager fm) { super(fm); }

    @Override
    public int getCount() { return NUMBER_OF_PAGES; }

    @Override
    public Fragment getItem(int position) {
        return MoodFragment.newInstance(position);
    }
}

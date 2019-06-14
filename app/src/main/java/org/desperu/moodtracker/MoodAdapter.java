package org.desperu.moodtracker;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MoodAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_PAGES = 5;
    static int currentPage;

    public MoodAdapter(FragmentManager fm) { super(fm); }

    @Override
    public int getCount() { return NUMBER_OF_PAGES; }

    // save position of the current page in currentPage
    @Override
    public void setPrimaryItem (@NonNull ViewGroup container, int position, @NonNull Object object) {
        currentPage = position;
    }

    @Override
    public Fragment getItem(int position) {
        return MoodFragment.newInstance(position);
    }
}

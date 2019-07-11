package org.desperu.moodtracker.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.desperu.moodtracker.controller.MoodFragment;

import static org.desperu.moodtracker.MoodTools.Constant.numberOfPage;

public class MoodAdapter extends FragmentPagerAdapter {

    public MoodAdapter(FragmentManager fm) { super(fm); }

    @Override
    public int getCount() { return numberOfPage; }

    @Override
    public Fragment getItem(int position) { return MoodFragment.newInstance(position); }
}

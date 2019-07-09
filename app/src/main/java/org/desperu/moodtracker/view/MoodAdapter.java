package org.desperu.moodtracker.view;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.SoundEffectConstants;
import android.view.ViewGroup;

import org.desperu.moodtracker.controller.MoodFragment;

public class MoodAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_PAGES = 5;

    public MoodAdapter(FragmentManager fm) { super(fm); }

    @NonNull
    @Override // TODO : test for son
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() { return NUMBER_OF_PAGES; }

    @Override
    public Fragment getItem(int position) {
        return MoodFragment.newInstance(position);
    }
}

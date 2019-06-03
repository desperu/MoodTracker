package org.desperu.moodtracker;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MyAdapter extends FragmentPagerAdapter {

    static final int NUMBER_OF_PAGES = 5;
    static int currentPage;

    // test frag layout here, not good
    // static  int fragment;

    public MyAdapter(FragmentManager fm) { super(fm); }

    @Override
    public int getCount() { return NUMBER_OF_PAGES; }

    // ça devrais le faire
    @Override
    public Parcelable saveState() { return null; }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}

    @Override
    public int getItemPosition(@NonNull Object object) { return -1; }

    @Override
    public void setPrimaryItem (@NonNull ViewGroup container, int position, @NonNull Object object) {
        currentPage = position;
        //History saveCurrentItem = new History();
        //saveCurrentItem.saveCurrentMood(currentPage);
    }
    //

    @Override
    public Fragment getItem(int position) {
        return MoodFragment.newInstance(position);
    }
}

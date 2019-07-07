package org.desperu.moodtracker;

import android.content.res.Resources;

public final class MoodTools {
    // TODO : to comment
    public static final class Constant {

        public static final int numberOfPage = 4;

        // History
        // To set history size.
        public static final int numberOfDays = 7;
        // You need to create corresponding ids.
        public static final int[] rLayout = {R.id.day1, R.id.day2, R.id.day3, R.id.day4,
                R.id.day5, R.id.day6, R.id.day7};
        public static final int[] imageShow = {R.id.imageShow1, R.id.imageShow2, R.id.imageShow3,
                R.id.imageShow4, R.id.imageShow5, R.id.imageShow6, R.id.imageShow7};
        public static final int[] imageShare = {R.id.imageShare1, R.id.imageShare2, R.id.imageShare3,
                R.id.imageShare4, R.id.imageShare5, R.id.imageShare6, R.id.imageShare7};

        // To set history day in screen.
        public static final int numberInScreen = 7;

    }

    // TODO : use this for key of sharedPreferences ??
    public static final class Keys {}
}

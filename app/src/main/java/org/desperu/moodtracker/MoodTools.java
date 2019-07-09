package org.desperu.moodtracker;

public final class MoodTools {

    /**
     * Constant to set your preferences.
     */
    public static final class Constant {

        // Number of moods.
        public static final int numberOfPage = 4;

        // Minimum size for a slide.
        public static final int minSlide = 150;

        // History
        // To set history size.
        public static final int numberOfDays = 7;

        // To set history day number in screen, so the size of each.
        public static final int numberInScreen = 7;

    }

    /**
     * Keys for SharedPreferences.
     */
    public static final class Keys {
        // Current mood.
        public static final String moodDayFile = "MoodDay";
        public static final String currentMood = "CurrentMood";
        public static final String currentDate = "CurrentDate";
        public static final String currentComment = "CurrentComment";

        // History mood.
        public static final String moodHistoryFile = "MoodHistory";
        public static final String moodHistory = "Mood";
        public static final String dateHistory = "Date";
        public static final String commentHistory = "Comment";
    }
}

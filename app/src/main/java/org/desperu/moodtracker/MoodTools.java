package org.desperu.moodtracker;

public final class MoodTools {

    /**
     * Constant to set your preferences.
     */
    public static final class Constant {

        // History
        // To set history size (number of days).
        public static final int numberOfDays = 7;

        // MainActivity
        // Number of moods.
        public static final int numberOfPage = 4;

        // Minimum size for a slide.
        public static final int minSlide = 150;

        // MoodHistoryActivity
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

    /**
     * Constant to construct history view.
     */
    public static final class ScreenConstant {

        // All constant here is used to made percent of get screen size, with * or / .

        // To correct little difference with get screen size.
        public static final double correctPortrait = 1.004;
        public static final double correctLandscape = 1.006;

        // Vertical size for each mood.
        public static final double sHappyWidth = 1;
        public static final double happyWidth = 0.825;
        public static final double normalWidth = 0.65;
        public static final double disappointedWidth = 0.475;
        public static final double sadWidth = 0.3;
        public static final double noMoodWidth = 1;

        // For mood age text.
        public static final int textMargin = 100;

        // For image button.
        public static final int imageWidth = 10;
        public static final double imageLeftMarginPortrait = 0.125;
        public static final double imageLeftMarginLandscape = imageLeftMarginPortrait / 1.8;

        // For toast to show comment.
        public static final int toastMargin = 50;
        public static final int toastPadding = 50;
        public static final int toastMinWidth = 20;
    }
}

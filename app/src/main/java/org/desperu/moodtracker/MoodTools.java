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
        // Enable or disable wrongDate function.
        public static final boolean wrongDateEnabled = true;

        // Number of moods. If you want change it, you must add/del corresponding values in switch of
        // MainActivity onCreateView, MoodHistoryActivity onCreateHistoryView and MoodUtils moodShareText.
        public static final int numberOfPage = 5;

        // Minimum size for a slide.
        public static final int minSlide = 200;

        // MoodHistoryActivity
        // To set history day number in screen, so the size of each.
        public static final int numberInScreen = 7;

        // MoodUtils
        // One day un millis -> (24 * 60 * 60 * 1000) = 86 400 000.
        public static final int oneDayMillis = 86400000;
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

        // All constant here are used to make percent of get screen size, with * or / .

        // To correct little difference with get screen size.
        public static final double correctPortrait = 1.004;
        public static final double correctLandscape = 1.006;

        // Horizontal size for each mood.
        public static final double sHappyWidth = 1;
        public static final double happyWidth = 0.825;
        public static final double normalWidth = 0.65;
        public static final double disappointedWidth = 0.475;
        public static final double sadWidth = 0.3;
        public static final double noMoodWidth = 1;

        // For image button.
        public static final double imageWidthPortrait = 0.125;
        public static final double imageWidthLandscape = imageWidthPortrait / 2.1;
        public static final int shareMarginLeft = 2;
        public static final int imageSadTop = 4;

        // For mood age text.
        public static final int textMargin = 100;

        // For toast to show comment.
        public static final int toastMargin = 50;
        public static final int toastPadding = 50;
        public static final int toastMinWidth = 20;
    }
}

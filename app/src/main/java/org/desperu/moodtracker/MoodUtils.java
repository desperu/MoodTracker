package org.desperu.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

class MoodUtils {

    private static final String moodDayFile = "MoodDay";
    private static final String currentMood = "CurrentMood";
    private static final String currentDate = "CurrentDate";
    private static final String currentComment = "CurrentComment";
    private static final String moodHistoryFile = "MoodHistory";
    private static final String moodHistory = "Mood";
    private static final String dateHistory = "Date";
    private static final String commentHistory = "Comment";
    private SharedPreferences sharedPreferences;

    /**
     * Save the current mood selected
     * @param moodNum the number of the selected mood view
     * @param context get context from super activity
     */
    void saveCurrentMood(Context context, int moodNum, String comment) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(currentMood, moodNum);
        editor.putLong(currentDate, getTime());
        editor.putString(currentComment, comment);
        editor.apply();
    }

    /**
     * Check if there was a mood saved and print it
     * @param context The base context from the method is call
     * @return The number of the mood view
     */
    int getLastMood(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        int lastMood = sharedPreferences.getInt(currentMood, -1);
        if (lastMood == -1)
            Toast.makeText(context, "Hi! What's your mood today??", Toast.LENGTH_SHORT).show();
        return sharedPreferences.getInt(currentMood, -1);
    }

    /**
     * Get the comment saved for the current day
     * @param context The base context from the method is called
     * @return Return the comment, null if there isn't
     */
    String getLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getString(currentComment, null);
    }

    /**
     * Methods to get values in MoodHistory file
     * @param context The base context from the method is called
     * @param key The key name of the value
     * @return The value
     */
    int getHistoryIntPrefs(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }
    long getHistoryLongPrefs(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
    String getHistoryStringPrefs(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    /**
     * Delete all mood saved
     * @param context The base context from the method is called
     */
    void deleteAllMoods(Context context) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }

    long getTime() {
        return System.currentTimeMillis();
    }

    int convertDate(long currentTime) {
        // TODO : find a better way, with a class of java utils, witch???
        currentTime += TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
        long oneDay = (24 * 60 * 60 * 1000);
        int i = 0;
        int year = 1970;
        int[] yearDays = {365, 365, 366, 365}; // we start in 01/01/1970, first leap year february 1972
        boolean leapYear = false;
        int month = 1; // first month of the year

        while (currentTime > (365 * oneDay)) {
            leapYear = false;
            if (i == 2) leapYear = true;
            currentTime -= (yearDays[i] * oneDay);
            year++;
            i++;
            if (i == 4) i = 0;
        }
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (i = 0; currentTime > (monthDays[i + 1] * oneDay); i++) {
            if (i == 1 && leapYear) monthDays[1] = 29;
            currentTime -= (monthDays[i] * oneDay);
            month++;
        }
        int day = (int) (currentTime / oneDay) + 1;
        currentTime = currentTime % oneDay;

        // TODO : For test only
        int hour = (int) (currentTime / (60 * 60 * 1000));
        currentTime = currentTime % (60 * 60 * 1000);
        int minutes = (int) (currentTime / (60 * 1000));
        //return (day * 10000) + (hour * 100) + minutes;

        return (year * 10000) + (month * 100) + day;
    }

    long checkSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        long savedDate = sharedPreferences.getLong(currentDate, 0);
        if (savedDate == 0) return savedDate;
        return convertDate(getTime()) - convertDate(savedDate);
    }

    void manageHistory(Context context) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();
        int[] mood = new int[7];
        long[] date = new long[7];
        String[] comment = new String[7];

        for (int i = 6; i >= 0; i--) {
            mood[i] = historyFile.getInt(moodHistory + (i + 1), -1);
            date[i] = historyFile.getLong(dateHistory + (i + 1), 0);
            comment[i] = historyFile.getString(commentHistory + (i + 1), null);

            if (i != 6) {
                mood[i + 1] = mood[i]; editorHistory.putInt(moodHistory + (i + 2), mood[i]).apply();
                date[i + 1] = date[i]; editorHistory.putLong(dateHistory + (i + 2), date[i]).apply();
                comment[i + 1] = comment[i]; editorHistory.putString(commentHistory + (i + 2),
                        comment[i]).apply();
            }

            if (i == 0) {
                mood[i] = dayFile.getInt(currentMood, -1);
                date[i] = dayFile.getLong(currentDate, 0);
                comment[i] = dayFile.getString(currentComment, null);
                editorDay.remove(currentMood).apply();
                editorDay.remove(currentDate).apply();
                editorDay.remove(currentComment).apply();
                editorHistory.putInt(moodHistory + (i + 1), mood[i]).apply();
                editorHistory.putLong(dateHistory + (i + 1), date[i]).apply();
                editorHistory.putString(commentHistory + (i + 1), comment[i]).apply();
            }
        }
    }
}
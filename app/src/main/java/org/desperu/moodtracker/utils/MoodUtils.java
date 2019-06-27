package org.desperu.moodtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class MoodUtils {

    private final String moodDayFile = "MoodDay";
    private final String currentMood = "CurrentMood";
    private final String currentDate = "CurrentDate";
    private final String currentComment = "CurrentComment";
    private final String moodHistoryFile = "MoodHistory";
    public static final String moodHistory = "Mood";
    public static final String dateHistory = "Date";
    public static final String commentHistory = "Comment";
    private SharedPreferences sharedPreferences;

    /**
     * Save the current mood selected, current date and the comment
     * @param moodNum the number of the selected mood view
     * @param context get context from super activity
     */
    public void saveCurrentMood(Context context, int moodNum, String comment) {
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
     * @return The number of the mood view, defValue if there isn't
     */
    public int getLastMood(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getInt(currentMood, -1);
    }

    /**
     * Get the comment saved for the current day
     * @param context The base context from the method is call
     * @return Return the date saved, defValue if there isn't
     */
    public long getSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getLong(currentDate, 0);
    }

    /**
     * Get the comment saved for the current day
     * @param context The base context from the method is called
     * @return Return the comment saved, defValue if there isn't
     */
    public String getLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getString(currentComment, null); // TODO : must be ""??
    }

    /**
     * Methods to get values in MoodHistory preference file
     * @param context The base context from the method is called
     * @param key The key name of the value
     * @return The value
     */
    public int getIntHistoryPrefs(Context context, String key) { // TODO : getMoodPrefs...
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }
    public long getLongHistoryPrefs(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
    public String getStringHistoryPrefs(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    /**
     * Delete all mood saved
     * @param context The base context from the method is called
     */
    public void deleteAllMoods(Context context) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    /**
     * Get the current time in milliseconds since 01/01/1970
     * @return time in millis
     */
    public long getTime() { return System.currentTimeMillis(); }

    /**
     * Compare given time with current time
     * @param givenTime Given time to compare, in milliseconds
     * @return The difference between current time and given time, format YYYYMMDD
     */
    public int compareDate(long givenTime) {
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(givenTime);
        int givenDate = givenCalendar.get(Calendar.YEAR) * 10000;
        givenDate += givenCalendar.get(Calendar.MONTH) * 100;
        givenDate += givenCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(getTime());
        int currentDate = currentCalendar.get(Calendar.YEAR) * 10000;
        currentDate += currentCalendar.get(Calendar.MONTH) * 100;
        currentDate += currentCalendar.get(Calendar.DAY_OF_MONTH);

        return currentDate - givenDate;
    }
    public int convertDate(long givenTime) {
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(givenTime);
        int givenDate = givenCalendar.get(Calendar.YEAR) * 10000;
        givenDate += givenCalendar.get(Calendar.MONTH) * 100;
        givenDate += givenCalendar.get(Calendar.DAY_OF_MONTH);
        // TODO : for test only
        /*int givenDate = givenCalendar.get(Calendar.DAY_OF_MONTH) * 10000;
        givenDate += givenCalendar.get(Calendar.HOUR_OF_DAY) * 100;
        givenDate += givenCalendar.get(Calendar.MINUTE);*/

        return givenDate;
    }

    public long checkSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        long savedDate = sharedPreferences.getLong(currentDate, 0);
        if (savedDate == 0) return savedDate;
        return convertDate(getTime()) - convertDate(savedDate);
    }

    /**
     * Manage history when it's a new date
     * @param context The base context from the method is called
     */
    public void manageHistory(Context context) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();
        int[] mood = new int[7];
        long[] date = new long[7];
        String[] comment = new String[7];
        // TODO : use constant class
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
package org.desperu.moodtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static org.desperu.moodtracker.Constant.numberOfDays;

public class MoodUtils {

    // TODO : put in constant or key??
    // Prefs files and keys
    public static final String moodDayFile = "MoodDay";
    public static final String currentMood = "CurrentMood";
    public static final String currentDate = "CurrentDate";
    public static final String currentComment = "CurrentComment";
    public static final String moodHistoryFile = "MoodHistory";
    public static final String moodHistory = "Mood";
    public static final String dateHistory = "Date";
    public static final String commentHistory = "Comment";
    private SharedPreferences sharedPreferences;

    /**
     * Save current mood selected, current date and comment.
     * @param moodNum Number of selected mood view.
     * @param context Get context from super activity.
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
     * Methods to gets values (int, long and string), in preferences files.
     * @param context The base context from this method is called.
     * @param file The name of the file.
     * @param key The key name of the value.
     * @return The value.
     */
    public int getIntPrefs(Context context, String file, String key) {
        sharedPreferences = context.getSharedPreferences(file, MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }
    public long getLongPrefs(Context context, String file, String key) {
        sharedPreferences = context.getSharedPreferences(file, MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
    public String getStringPrefs(Context context, String file, String key) {
        sharedPreferences = context.getSharedPreferences(file, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    /**
     * Delete all moods saved.
     * @param context The base context from this method is called.
     */
    public void deleteAllMoods(Context context) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    /**
     * Get the current time in milliseconds since 01/01/1970.
     * @return Time in millis.
     */
    private long getTime() { return System.currentTimeMillis(); }

    /**
     * Compare given time with current time.
     * @param givenTime Given time to compare, in milliseconds.
     * @return Difference between current time and given time, format YYYYMMDD.
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
    // TODO : for test only
    /*public int compareDate(long givenTime) {
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(givenTime);
        int givenDate = givenCalendar.get(Calendar.DAY_OF_MONTH) * 10000;
        givenDate += givenCalendar.get(Calendar.HOUR_OF_DAY) * 100;
        givenDate += givenCalendar.get(Calendar.MINUTE);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(getTime());
        int currentDate = currentCalendar.get(Calendar.DAY_OF_MONTH) * 10000;
        currentDate += currentCalendar.get(Calendar.HOUR_OF_DAY) * 100;
        currentDate += currentCalendar.get(Calendar.MINUTE);

        return currentDate - givenDate;
    }*/

    /**
     * Manage history when it's a new date.
     * @param context The base context from this method is called.
     */
    public void manageHistory(Context context) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();
        int[] mood = new int[numberOfDays]; // TODO : stop use tabs??? and perfect?
        long[] date = new long[numberOfDays];
        String[] comment = new String[numberOfDays];
        for (int i = (numberOfDays - 1); i >= 0; i--) {
            // Get saved history and put in tabs.
            mood[i] = historyFile.getInt(moodHistory + (i + 1), -1);
            date[i] = historyFile.getLong(dateHistory + (i + 1), 0);
            comment[i] = historyFile.getString(commentHistory + (i + 1), null);

            if (i != (numberOfDays - 1)) {
                // Put given mood in history key + 1.
                mood[i + 1] = mood[i]; editorHistory.putInt(moodHistory + (i + 2), mood[i]).apply();
                date[i + 1] = date[i]; editorHistory.putLong(dateHistory + (i + 2), date[i]).apply();
                comment[i + 1] = comment[i]; editorHistory.putString(commentHistory + (i + 2),
                        comment[i]).apply();
            }

            if (i == 0) {
                // Get current mood and put in tabs.
                mood[i] = dayFile.getInt(currentMood, -1);
                date[i] = dayFile.getLong(currentDate, 0);
                comment[i] = dayFile.getString(currentComment, null);
                // Delete from current mood file.
                editorDay.remove(currentMood).apply();
                editorDay.remove(currentDate).apply();
                editorDay.remove(currentComment).apply();
                // And put in first history mood.
                editorHistory.putInt(moodHistory + (i + 1), mood[i]).apply();
                editorHistory.putLong(dateHistory + (i + 1), date[i]).apply();
                editorHistory.putString(commentHistory + (i + 1), comment[i]).apply();
            }
        }
    }
}
package org.desperu.moodtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static org.desperu.moodtracker.MoodTools.Constant.*;
import static org.desperu.moodtracker.MoodTools.Keys.*;

public class MoodUtils {

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
        editor.putLong(currentDate, System.currentTimeMillis());
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
     * Compare given time with current time, to know if the difference is in day, month or year.
     * @param givenTime Given time to compare, in milliseconds.
     * @return Difference between current time and given time, format YYYY0000, or MM00, or DD, to
     * differentiate them.
     */
    /*public int compareDate(long givenTime) {
        // Set a Calendar at current time.
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        // Set a Calendar at given time.
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(givenTime);

        // Get in witch time meter is the difference.
        int differenceIn = 0;
        if (currentCalendar.get(Calendar.DAY_OF_MONTH) != givenCalendar.get(Calendar.DAY_OF_MONTH))
            differenceIn += 1;
        if (currentCalendar.get(Calendar.MONTH) != givenCalendar.get(Calendar.MONTH)) differenceIn += 2;
        if (currentCalendar.get(Calendar.YEAR) != givenCalendar.get(Calendar.YEAR)) differenceIn += 4;

        // Return the difference in special format to differentiate them.
        if (differenceIn >= 4)
            return (currentCalendar.get(Calendar.YEAR) - givenCalendar.get(Calendar.YEAR)) * 10000;
        if (differenceIn >= 2)
            return (currentCalendar.get(Calendar.MONTH) - givenCalendar.get(Calendar.MONTH)) * 100;
        if (differenceIn == 1)
            return (currentCalendar.get(Calendar.DAY_OF_MONTH) - givenCalendar.get(Calendar.DAY_OF_MONTH));

        return 0;
    }*/

    // TODO : for test only
    public int compareDate(long givenTime) {
        // Set a Calendar at current time.
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        // Set a Calendar at given time.
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(givenTime);

        // Get in witch time meter is the difference.
        int differenceIn = 0;
        if (currentCalendar.get(Calendar.MINUTE) != givenCalendar.get(Calendar.MINUTE))
            differenceIn += 1;
        if (currentCalendar.get(Calendar.HOUR_OF_DAY) != givenCalendar.get(Calendar.HOUR_OF_DAY))
            differenceIn += 2;
        if (currentCalendar.get(Calendar.DAY_OF_MONTH) != givenCalendar.get(Calendar.DAY_OF_MONTH))
            differenceIn += 4;

        // Return the difference in special format to differentiate them.
        if (differenceIn >= 4)
            return (currentCalendar.get(Calendar.DAY_OF_MONTH) - givenCalendar.get(Calendar.DAY_OF_MONTH)) * 10000;
        if (differenceIn >= 2)
            return (currentCalendar.get(Calendar.HOUR_OF_DAY) - givenCalendar.get(Calendar.HOUR_OF_DAY)) * 100;
        if (differenceIn == 1)
            return (currentCalendar.get(Calendar.MINUTE) - givenCalendar.get(Calendar.MINUTE));

        return 0;
    }

    /**
     * Manage history when it's a new date.
     * @param context The base context from this method is called.
     */
    public void manageHistory(Context context) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();

        for (int i = (numberOfDays - 1); i > 0; i--) {
            // Get saved history mood values.
            int mood = historyFile.getInt(moodHistory + i, -1);
            long date = historyFile.getLong(dateHistory + i, 0);
            String comment = historyFile.getString(commentHistory + i, null);

            // Put given mood values in history key + 1.
            editorHistory.putInt(moodHistory + (i + 1), mood).apply();
            editorHistory.putLong(dateHistory + (i + 1), date).apply();
            editorHistory.putString(commentHistory + (i + 1), comment).apply();

            if (i == 1) {
                // Get current mood values.
                mood = dayFile.getInt(currentMood, -1);
                date = dayFile.getLong(currentDate, 0);
                comment = dayFile.getString(currentComment, null);

                // Delete values in current mood file.
                editorDay.remove(currentMood).apply();
                editorDay.remove(currentDate).apply();
                editorDay.remove(currentComment).apply();

                // And put in first history mood.
                editorHistory.putInt(moodHistory + i, mood).apply();
                editorHistory.putLong(dateHistory + i, date).apply();
                editorHistory.putString(commentHistory + i, comment).apply();
            }
        }
    }
}
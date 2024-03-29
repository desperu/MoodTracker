package org.desperu.moodtracker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.desperu.moodtracker.R;

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
     * For mood history time ago.
     * Compare given time with current time, return the difference.
     * @param givenTime Given time to compare, in milliseconds.
     * @return Difference between current time and given time, in days.
     */
    public int compareTime(long givenTime) {
        // Set a Calendar at current time.
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        // Set a Calendar at given time.
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(givenTime);

        // If the mood time saved isn't over 24h than current time, add 1 for this day.
        int notYetOneDay = 0;
        if (currentCalendar.get(Calendar.HOUR_OF_DAY) < givenCalendar.get(Calendar.HOUR_OF_DAY)) notYetOneDay = 1;
        else if (currentCalendar.get(Calendar.HOUR_OF_DAY) == givenCalendar.get(Calendar.HOUR_OF_DAY)) {
            if (currentCalendar.get(Calendar.MINUTE) < givenCalendar.get(Calendar.MINUTE)) notYetOneDay = 1;
            else if (currentCalendar.get(Calendar.MINUTE) == givenCalendar.get(Calendar.MINUTE)) {
                if (currentCalendar.get(Calendar.SECOND) < givenCalendar.get(Calendar.SECOND)) notYetOneDay = 1;
                else if (currentCalendar.get(Calendar.SECOND) == givenCalendar.get(Calendar.SECOND))
                    if (currentCalendar.get(Calendar.MILLISECOND) < givenCalendar.get(Calendar.MILLISECOND)) notYetOneDay = 1;
            }
        }

        // The difference between current time and given time divide by one day give one from 24h exact only,
        // so we add one for the last day witch is not yet over 24h from mood time saved.
        return (int) ((System.currentTimeMillis() - givenTime) / oneDayMillis) + notYetOneDay;
    }

    /**
     * For mood cycle.
     * Compare given time with current time, return the difference.
     * @param givenTime Given time to compare, in milliseconds.
     * @return Difference between current time and given time in corresponding time meter.
     */
    public int compareDate(long givenTime) {
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

        // Return the difference in corresponding time meter.
        if (differenceIn >= 4)
            return (currentCalendar.get(Calendar.YEAR) - givenCalendar.get(Calendar.YEAR));
        if (differenceIn >= 2)
            return (currentCalendar.get(Calendar.MONTH) - givenCalendar.get(Calendar.MONTH));
        if (differenceIn == 1)
            return (currentCalendar.get(Calendar.DAY_OF_MONTH) - givenCalendar.get(Calendar.DAY_OF_MONTH));

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

    /**
     * Set text for intent with given mood number, to construct share text.
     * @param context  The base context from this method is called.
     * @param position Mood number.
     * @param time If it's to share current mood (present), or history mood (past).
     * @return Text for given mood number.
     */
    public String moodShareText(Context context, int position, int time) {
        String presentOrPast = "";
        if (time == -1) presentOrPast = context.getString(R.string.past);
        else if (time == 0) presentOrPast = context.getString(R.string.present);
        // Find the mood for this day, and return correspond text.
        switch (position) {
            case 0: return context.getResources().getString(R.string.mood_day_super_happy, presentOrPast);
            case 1: return context.getResources().getString(R.string.mood_day_happy, presentOrPast);
            case 2: return context.getResources().getString(R.string.mood_day_normal, presentOrPast);
            case 3: return context.getResources().getString(R.string.mood_day_disappointed, presentOrPast);
            case 4: return context.getResources().getString(R.string.mood_day_sad, presentOrPast);
            default: return " : ";
        }
    }

    /**
     * Prepare intent to share mood.
     * @param shareText The given text for the intent, to share mood.
     * @return Intent object for share it.
     */
    public Intent prepareShareIntent (String shareText) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, shareText);
        share.setType("text/plain");
        return share;
    }
}
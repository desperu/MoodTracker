package org.desperu.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

class MoodUtils {

    private static final String moodDayFile = "MoodDay";
    private static final String currentMood = "CurrentMood";
    private static final String currentDate = "CurrentDate";
    private static final String currentComment = "CurrentComment";
    private static final String moodHistory = "MoodHistory";
    private SharedPreferences sharedPreferences;

    // TODO : don't use static, use method to get tab
    // TODO : use string for all Toast
    static int[] mood = new int[7];
    static int[] date = new int[7];
    static String[] comment = new String[7];

    /**
     * Save the current mood selected
     *
     * @param moodNum the number of the selected mood view
     * @param context get context from super activity
     */
    void saveCurrentMood(Context context, int moodNum, String comment) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(currentMood, moodNum);
        editor.putInt(currentDate, getTime());
        editor.putString(currentComment, comment);
        editor.apply();
        Toast.makeText(context, "Current Mood Saved! For : " + moodNum + ",Date " + getTime() +
                "New Date Format" + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Check if there was a mood saved and print it
     *
     * @param context The base context from the method is call
     * @return The number of the mood view
     */
    int getLastMood(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        if (sharedPreferences.contains(currentMood)) {
            int lastMood = sharedPreferences.getInt(currentMood, -1);
            Toast.makeText(context, "Last Mood selected today : " + lastMood,
                    Toast.LENGTH_SHORT).show();
            return lastMood;
        } else {
            Toast.makeText(context, "Hi! What's your mood today??", Toast.LENGTH_SHORT).show();
            // TODO : Useless, defValue give the same...only for the Toast??
            return -1;
        }
    }

    /**
     * Get the comment saved for the current day
     *
     * @param context The base context from the method is call
     * @return Return the comment, null if there isn't
     */
    String getLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getString(currentComment, null);
    }

    void deleteAllMood(Context context) {
        sharedPreferences = context.getSharedPreferences(moodHistory, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }

    /*public int getDate() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.YEAR) * 10000;
        date += (calendar.get(Calendar.MONTH) + 1) * 100;
        date += calendar.get(Calendar.DATE);
        return date;
    }*/

    // For test only
    private int getTime() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.HOUR_OF_DAY) * 100;
        date += calendar.get(Calendar.MINUTE);
        return date;
        //System.currentTimeMillis(); + convertisseur
    }

    long convertDate(long date) {
        long year = (long) (getTime() / (365.25 * 24 * 60 * 60 * 1000));
        long month = (long) ((getTime() % (365.25 * 24 * 60 * 60 * 1000)) / 12);
        long day = (getTime() - month) / 30;
        long convertDate = 0;
        return convertDate;
    }

    int checkSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        int savedDate = sharedPreferences.getInt(currentDate, 0);
        if (savedDate == 0) return savedDate;
        return getTime() - savedDate;
    }

    // TODO : create class SharedPreferences for simplify access to pref ???
    // TODO : simplify -> create method to serialize action read clear, read only, write
    // TODO : use i-1 not length 8
    void manageHistory(Context context, boolean newDate) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistory, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();

        for (int i = 6; i >= 0; i--) {
            mood[i] = historyFile.getInt("Mood" + (i + 1), -1);
            date[i] = historyFile.getInt("Date" + (i + 1), 0);
            comment[i] = historyFile.getString("Comment" + (i + 1), null);

            if (i != 6 && newDate) {
                mood[i + 1] = mood[i]; editorHistory.putInt("Mood" + (i + 2), mood[i]).apply();
                date[i + 1] = date[i]; editorHistory.putInt("Date" + (i + 2), date[i]).apply();
                comment[i + 1] = comment[i]; editorHistory.putString("Comment" + (i + 2),
                        comment[i]).apply();
            }

            if (i == 0 && newDate) {
                mood[i] = dayFile.getInt(currentMood, -1);
                date[i] = dayFile.getInt(currentDate, 0);
                comment[i] = dayFile.getString(currentComment, null);
                editorDay.remove(currentMood).apply();
                editorDay.remove(currentDate).apply();
                editorDay.remove(currentComment).apply();
                editorHistory.putInt("Mood" + (i + 1), mood[i]).apply();
                editorHistory.putInt("Date" + (i + 1), date[i]).apply();
                editorHistory.putString("Comment" + (i + 1), comment[i]).apply();
            }
        }
    }
}

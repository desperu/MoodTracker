package org.desperu.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;

public class MoodHistory extends AppCompatActivity {

    private static final String moodDayFile = "MoodDay";
    private static final String currentMood = "CurrentMood";
    private static final String currentDate = "CurrentDate";
    private static final String currentComment = "CurrentComment";
    private static final String moodHistory = "MoodHistory";
    SharedPreferences sharedPreferences;

    static int[] mood = new int[8];
    int[] date = new int[8];
    static String[] comment = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Check if there was a mood saved and print it
     * @param context The base context from the method is call
     * @return The number of the mood view
     */
    public int getLastMood(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        if (sharedPreferences.contains(currentMood)) {
            int lastMood = sharedPreferences.getInt(currentMood, -1);
            Toast.makeText(context, "Last Mood selected today : " + lastMood, Toast.LENGTH_SHORT).show();
            return lastMood;
        } else {
            Toast.makeText(context, "Hi! What's your mood today??", Toast.LENGTH_SHORT).show();
            // TODO : Useless, defValue give the same...only for the Toast??
            return -1;
        }
    }

    /**
     * Get the comment saved for the current day
     * @param context The base context from the method is call
     * @return Return the comment, null if there isn't
     */
    public String getLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getString(currentComment, null);
    }

    /**
     * Save the current mood selected
     * @param moodNum the number of the selected mood view
     * @param context get context from super activity
     */
    public void saveCurrentMood(Context context, int moodNum) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(currentMood, moodNum);
        editor.putInt(currentDate, getTime());
        editor.putString(currentComment, MainActivity.inputText);
        editor.apply();
        Toast.makeText(context, "Current Mood Saved! For : " + moodNum + " ,Date " + getTime(), Toast.LENGTH_SHORT).show();
    }

    /*public int getDate() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.YEAR) * 10000;
        date += (calendar.get(Calendar.MONTH) + 1) * 100;
        date += calendar.get(Calendar.DATE);
        return date;
    }*/

    // For test only
    public int getTime() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.HOUR_OF_DAY) * 100;
        date += calendar.get(Calendar.MINUTE);
        return date;
    }

    public int checkSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        int savedDate = sharedPreferences.getInt(currentDate, 0);
        if (savedDate == 0) return savedDate;
        return getTime() - savedDate;
        //this.onNewDayWhenRun(context);
        /*if (getTime() - savedDate > 0) {
            this.manageHistory(context, true);
            onRestart();
        } else if (getTime() - savedDate < 0) {
            MainActivity.goodDate = false;
            Toast.makeText(context, "The date isn't good!! You can't change the past!!!",
                    Toast.LENGTH_LONG).show();
            // TODO print this message in a dialog box? and answer for RAZ all prefs??
            //finishAffinity(); // to see the result... do nothing!!
            finish(); // don't kill process
            //onDestroy(); // too violent can't print toast
        } else if (savedDate == 0) Toast.makeText(context, "The date is good!!",
                Toast.LENGTH_LONG).show();*/
    }

    // search on web compte à rebourd
    public int onNewDayWhenRun(Context context) {
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY) * 100;
        time += calendar.get(Calendar.MINUTE);
        int beforeNewMood = 2400 - time;
        boolean again = false;
        do {
            if (getTime() == time) again = true;
            // wait(60000); // object not locked by thread before wait()
            if (beforeNewMood == 41) {
                this.manageHistory(context, true);
                onRestart();
            }
        } while (again);
        return onNewDayWhenRun(context);
    }

    // TODO : create class SharedPreferences for simplify access to pref ???

    // TODO : simplify -> create method to serialize action read clear, read only, write
    // TODO : use i-1 not length 8
    public void manageHistory(Context context, boolean newDate) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistory, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();
        for (int i = 7; i > -1; i--) {
            if (i == 0 && newDate) {
                mood[i] = dayFile.getInt(currentMood, -1);
                date[i] = dayFile.getInt(currentDate, 0);
                comment[i] = dayFile.getString(currentComment, null);
                editorDay.remove(currentMood).apply();
                editorDay.remove(currentDate).apply();
                editorDay.remove(currentComment).apply();
            } else {
                mood[i] = historyFile.getInt("Mood" + i, -1);
                date[i] = historyFile.getInt("Date" + i, 0);
                comment[i] = historyFile.getString("Comment" + i, null);
                editorHistory.remove("Mood" + i).apply();
                editorHistory.remove("Date" + i).apply();
                editorHistory.remove("Comment" + i).apply();
            }
            for (int j = 1; j < 8; j++) {
                if (getTime() - date[i] == j) {
                    editorHistory.putInt("Mood" + j, mood[i]).apply(); mood[j] = mood[i];
                    editorHistory.putInt("Date" + j, date[i]).apply(); date[j] = date[i];
                    editorHistory.putString("Comment" + j, comment[i]).apply(); comment[j] = comment[i];
                }
            }
        }
    }
}

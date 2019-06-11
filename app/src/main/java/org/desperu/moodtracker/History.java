package org.desperu.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;

public class History extends AppCompatActivity {

    private static final String moodDayFile = "MoodDay";
    private static final String currentMood = "CurrentMood";
    private static final String currentDate = "CurrentDate";
    private static final String currentComment = "CurrentComment";
    private static final String moodHistory = "MoodHistory";
    SharedPreferences sharedPreferences;

    int[] mood = new int[8];
    int[] date = new int[8];
    String[] comment = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createHistoryView();
        setContentView(R.layout.history_layout);
    }

    /**
     * Check if there was a mood saved and print it
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

    public String getLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getString(currentComment, null);
    }

    public void delLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(currentComment).apply();
    }

    /**
     * Save the current mood selected
     * @param moodNum    number of the selected mood
     * @param context get context from super activity
     */
    public void saveCurrentMood(Context context, int moodNum) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(currentMood, moodNum);
        editor.putInt(currentDate, getDate());
        editor.putString(currentComment, MainActivity.inputText);
        editor.apply();
        Toast.makeText(context, "Current Mood Saved! For : " + moodNum + " ,Date " + getDate(), Toast.LENGTH_SHORT).show();
    }

    /*public int getDate() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.YEAR) * 10000;
        date += (calendar.get(Calendar.MONTH) + 1) * 100;
        date += calendar.get(Calendar.DATE);
        return date;
    }*/

    // For test only
    public int getDate() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.HOUR_OF_DAY) * 100;
        date += calendar.get(Calendar.MINUTE);
        return date;
    }

    // TODO : use int, new mood->positive, wrong date-> negative
    public int checkSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        //if (sharedPreferences.contains(currentDate)) {
        int savedDate = sharedPreferences.getInt(currentDate, 0);
        if (savedDate == 0) return savedDate;
        return getDate() - savedDate;
        //else return true;
    }

    // TODO : create method for create history, manage and print it, with tab
    // TODO : if no mood for one day blanch and message that's no mood
    // TODO : manage the data history with turn one tab for one type data
    // TODO : create class SharedPreferences for simplify access to pref

    public void manageHistory(Context context) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorCurrent = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistory, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();
        for (int i = 7; i > -1; i--) {
            boolean isGood = false;
            if (i == 0) {
                mood[i] = dayFile.getInt(currentMood, -1);
                editorCurrent.remove(currentMood).apply();
                date[i] = dayFile.getInt(currentDate, 0);
                editorCurrent.remove(currentDate).apply();
                comment[i] = dayFile.getString(currentComment, null);
                editorCurrent.remove(currentComment).apply();
            } else {
                mood[i] = historyFile.getInt("Mood" + i, -1);
                date[i] = historyFile.getInt("Date" + i, 0);
                comment[i] = historyFile.getString("Comment" + i, null);
            }
            for (int j = 1; j < 8; j++) {
                if (getDate() - date[i] == j) {
                    editorHistory.putInt("Mood" + j, mood[i]).apply(); mood[j] = mood[i];
                    editorHistory.putInt("Date" + j, date[i]).apply(); date[j] = date[i];
                    editorHistory.putString("Comment" + j, comment[i]).apply(); comment[j] = comment[i];
                    isGood = true;
                }
            }
            if (!isGood) {
                editorHistory.putInt("Mood" + i, -1).apply(); mood[i] = -1;
                editorHistory.putInt("Date" + i, 0).apply(); date[i] = 0;
                editorHistory.putString("Comment" + i, null).apply(); comment[i] = null;
            }
        }
    }

    public void createHistoryView() {
        //for (int i = )
    }
}

package org.desperu.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
        //this.setHistoryView();
        //setContentView(R.layout.history_layout);
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

    // TODO : create method if date change during run activity, must be run by checkSavedDate
    public void onNewDayWhenRun() {
        //use wait();
    }

    // TODO : create method for create history, manage and print it, with tab
    // TODO : if no mood for one day blanch and message that's no mood
    // TODO : manage the data history with turn one tab for one type data
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
                if (getDate() - date[i] == j) {
                    editorHistory.putInt("Mood" + j, mood[i]).apply(); mood[j] = mood[i];
                    editorHistory.putInt("Date" + j, date[i]).apply(); date[j] = date[i];
                    editorHistory.putString("Comment" + j, comment[i]).apply(); comment[j] = comment[i];
                }
            }
        }
    }

    public void onCreateHistoryView() {
        this.manageHistory(getBaseContext(), false);

        //int[] rLayout = {R.id.Day1, R.id.Day2, R.id.Day3, R.id.Day4, R.id.Day5, R.id.Day6, R.id.Day7};
        RelativeLayout relativeLayout = new RelativeLayout(this);

        RelativeLayout Day1 = new RelativeLayout(this);
        RelativeLayout Day2 = new RelativeLayout(this);
        RelativeLayout Day3 = new RelativeLayout(this);
        RelativeLayout Day4 = new RelativeLayout(this);
        RelativeLayout Day5 = new RelativeLayout(this);
        RelativeLayout Day6 = new RelativeLayout(this);
        RelativeLayout Day7 = new RelativeLayout(this);
        RelativeLayout[] newRelativeLayout = {Day1, Day2, Day3, Day4, Day5, Day6, Day7};

        for (int i = 7; i > 0; i--) {
            newRelativeLayout[i - 1] = new RelativeLayout(this);
            RelativeLayout.LayoutParams params;
            switch (mood[i]) {
                case 0:
                    params = new RelativeLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT/7);
                    newRelativeLayout[i - 1].setLayoutParams(params);
                    newRelativeLayout[i - 1].setBackgroundColor(Color.parseColor("#fff9ec4f"));
                    relativeLayout.addView(newRelativeLayout[i - 1],params);
                    /*params.width = MATCH_PARENT;
                    params.height = MATCH_PARENT/7;
                    findViewById(rLayout[i - 1]).requestLayout();*/
                    break;
                case 1:
                    newRelativeLayout[i - 1].setBackgroundColor(Color.parseColor("#ffb8e986"));
                    params = new RelativeLayout.LayoutParams(MATCH_PARENT*4/5,MATCH_PARENT/7);
                    newRelativeLayout[i - 1].setLayoutParams(params);
                    relativeLayout.addView(newRelativeLayout[i - 1],params);
                    break;
                case 2:
                    newRelativeLayout[i - 1].setBackgroundColor(Color.parseColor("#a5468ad9"));
                    params = new RelativeLayout.LayoutParams(MATCH_PARENT*3/5,MATCH_PARENT/7);
                    newRelativeLayout[i - 1].setLayoutParams(params);
                    relativeLayout.addView(newRelativeLayout[i - 1],params);
                    break;
                case 3:
                    newRelativeLayout[i - 1].setBackgroundColor(Color.parseColor("#ff9b9b9b"));
                    params = new RelativeLayout.LayoutParams(MATCH_PARENT*2/5,MATCH_PARENT/7);
                    newRelativeLayout[i - 1].setLayoutParams(params);
                    relativeLayout.addView(newRelativeLayout[i - 1],params);
                    break;
                case 4:
                    newRelativeLayout[i - 1].setBackgroundColor(Color.parseColor("#ffde3c50"));
                    params = new RelativeLayout.LayoutParams(MATCH_PARENT/5,MATCH_PARENT/7);
                    newRelativeLayout[i - 1].setLayoutParams(params);
                    relativeLayout.addView(newRelativeLayout[i - 1],params);
                    break;
                default:
                    params = new RelativeLayout.LayoutParams(MATCH_PARENT,(MATCH_PARENT/7));
                    newRelativeLayout[i - 1].setLayoutParams(params);
                    newRelativeLayout[i - 1].setBackgroundColor(Color.BLACK);
                    relativeLayout.addView(newRelativeLayout[i - 1],params);
                    break;
            }
        }
        setContentView(relativeLayout);
    }
}

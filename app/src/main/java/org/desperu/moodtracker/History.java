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
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
    }

    /**
     * Check if there was a mood saved and print it
     */
    public int checkLastMood(Context context) {

        int lastMood;
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        //objectif : restore the saved mood
        //pour cela, on commence par regarder si on a déjà des éléments sauvegardés
        if (sharedPreferences.contains(currentMood)) {
            lastMood = sharedPreferences.getInt(currentMood, 0);
            Toast.makeText(context, "Last Mood selected today : " + lastMood, Toast.LENGTH_SHORT).show();
            return lastMood;
        } else {
            lastMood = -1;
            Toast.makeText(context, "Hi! What's your mood today??", Toast.LENGTH_SHORT).show();
            return lastMood;
        }
    }

    /**
     * Save the current mood selected
     * @param mNum number of the selected mood
     * @param context get context from super activity
     */
    public void saveCurrentMood(Context context, int mNum) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(currentMood, mNum);
        editor.apply();
        Toast.makeText(context, "Current Mood Saved! For : " + mNum + " ,Date " + getDate(), Toast.LENGTH_SHORT).show();
    }

    public int getDate () {
        Calendar calendar = Calendar.getInstance();
        int date = (calendar.get(Calendar.YEAR)*10000);
        date += (calendar.get(Calendar.MONTH)*100);
        date += calendar.get(Calendar.DATE);
        return date;
    }

    // TODO : create method for create history, manage and print it, with tab
    // TODO : save the date and check it
}

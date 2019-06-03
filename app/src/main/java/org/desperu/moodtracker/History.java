package org.desperu.moodtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class History extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // à vérif!!
        setContentView(R.layout.activity_main);
        }
    /**
     * Check if there was a mood saved and print it
     * super Instance from Bundle
     */
    public int checkHistory() {//(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState); // quel utilité?
        sharedPreferences = getBaseContext().getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
        //objectif : restore the saved mood
        //pour cela, on commence par regarder si on a déjà des éléments sauvegardés
        if (sharedPreferences.contains(getString(R.string.Current_Mood))) {
            int lastMood = sharedPreferences.getInt(getString(R.string.Current_Mood), 0);

            Toast.makeText(getBaseContext(), "Last Mood selected : " + lastMood, Toast.LENGTH_SHORT).show();
            return lastMood;
        } else {
            Toast.makeText(getBaseContext(), "Hi! What's your mood today??", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    /**
     * Save the current mood selected
     * @param mNum number of the selected mood
     */
    public void saveCurrentMood(int mNum) {

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.Current_Mood), mNum);
        editor.apply();
        Toast.makeText(getBaseContext(), "Current Mood Saved! For : " + mNum, Toast.LENGTH_SHORT).show();
    }

    //create method for create history, manage and print it, with tab
}

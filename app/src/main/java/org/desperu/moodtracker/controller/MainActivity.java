package org.desperu.moodtracker.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.desperu.moodtracker.R;
import org.desperu.moodtracker.utils.MoodUtils;
import org.desperu.moodtracker.view.MoodAdapter;
import org.desperu.moodtracker.view.VerticalViewPager;

import static org.desperu.moodtracker.utils.MoodUtils.currentComment;
import static org.desperu.moodtracker.utils.MoodUtils.currentDate;
import static org.desperu.moodtracker.utils.MoodUtils.currentMood;
import static org.desperu.moodtracker.utils.MoodUtils.moodDayFile;

public class MainActivity extends AppCompatActivity {

    MoodAdapter mAdapter;
    VerticalViewPager mPager;
    MoodUtils moodUtils = new MoodUtils();

    private String comment;
    private boolean goodDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);

        mAdapter = new MoodAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);*/

        setContentView(R.layout.mood_view);
        Intent i = new Intent(MainActivity.this, MoodActivity.class);
        startActivity(i);
    }

    /**
     * Show comment dialog box to edit a comment.
     * @param view The clicked view from dialog is called.
     */
    public void commentClick(View view) {
        AlertDialog.Builder dialogComment = new AlertDialog.Builder(
                MainActivity.this, R.style.InputCommentDialog);
        dialogComment.setTitle(R.string.title_comment);

        // Inflate comment_dialog.xml for custom AlertDialog.
        View viewComment = LayoutInflater.from(MainActivity.this).inflate(R.layout.comment_dialog,
                (ViewGroup) findViewById(R.id.ll_comment_root));
        final EditText inputComment = viewComment.findViewById(R.id.input_comment);
        inputComment.setText(comment);
        dialogComment.setView(viewComment);

        // Positive button to valid comment.
        dialogComment.setPositiveButton(R.string.button_valid_comment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        comment = inputComment.getText().toString();
                    }
                });

        // Negative button to cancel comment.
        dialogComment.setNegativeButton(R.string.button_cancel_comment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        inputComment.getText().clear();
                        comment = "";
                    }
                });

        dialogComment.show();
    }

    /**
     * Show history view.
     * @param view The clicked view from dialog is called.
     */
    public void historyClick(View view) {
        Intent i = new Intent(MainActivity.this, MoodHistoryActivity.class);
        startActivity(i);
    }

    /**
     * If current date is lower than last saved, show dialog not cancelable. Two choice,
     * correct date, or delete all moods saved.
     */
    public void wrongDateDialog() {
        AlertDialog.Builder wrongDate = new AlertDialog.Builder(MainActivity.this);
        wrongDate.setTitle(R.string.title_wrong_date);
        wrongDate.setMessage(R.string.message_wrong_date);
        wrongDate.setCancelable(false);

        // Positive button to valid corrected date.
        wrongDate.setPositiveButton(R.string.button_valid_date,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (moodUtils.compareDate(moodUtils.getLongPrefs(MainActivity.this,
                                moodDayFile, currentDate)) >= 0)
                            goodDate = true;
                        else { // if the date is again lower, restart this dialog.
                            wrongDateDialog();
                            Toast.makeText(MainActivity.this,
                                    R.string.toast_date_always_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Negative button to delete all moods saved.
        wrongDate.setNegativeButton(R.string.button_delete_mood,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        goodDate = true;
                        moodUtils.deleteAllMoods(MainActivity.this);
                        Toast.makeText(MainActivity.this, R.string.toast_all_mood_delete,
                                Toast.LENGTH_SHORT).show();
                    }
                });

        wrongDate.show();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
        // If the user is currently looking at the first step or in comment, allow the system
        // to handle the Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
        // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onResume() {
        goodDate = true;
        // Get current date and last saved date difference.
        int checkDate = moodUtils.compareDate(moodUtils.getLongPrefs(this, moodDayFile, currentDate));
        if (checkDate < 0) {
            // If current date is lower, show wrongDateDialog.
            goodDate = false;
            this.wrongDateDialog();
        } else if (checkDate > 0) {
            // If current date is upper, save last mood. Or on first run.
            moodUtils.manageHistory(this);
            comment = "";
//            mPager.setCurrentItem(1);
            Toast.makeText(this, R.string.toast_new_day, Toast.LENGTH_SHORT).show();
        } else { // So checkDate = 0.
            // If current date is the same, show current mood and get current comment.
            int lastMood = moodUtils.getIntPrefs(this, moodDayFile, currentMood);
//            if (lastMood > -1) mPager.setCurrentItem(lastMood);
            comment = moodUtils.getStringPrefs(this, moodDayFile, currentComment);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (goodDate)
            // When leave activity, and if date isn't wrong, save selected mood, date and comment.
  //          moodUtils.saveCurrentMood(this, mPager.getCurrentItem(), comment);
        super.onPause();
    }
}
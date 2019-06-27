package org.desperu.moodtracker.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.desperu.moodtracker.view.MoodAdapter;
import org.desperu.moodtracker.utils.MoodUtils;
import org.desperu.moodtracker.R;
import org.desperu.moodtracker.view.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    MoodAdapter mAdapter;
    VerticalViewPager mPager;
    MoodUtils moodUtils = new MoodUtils();

    private String comment;
    private boolean goodDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MoodAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
    }

    /**
     * Show comment dialog box to edit a comment
     * @param view The view from dialog is call to set font // TODO : to verify
     */
    public void commentClick(View view) { // TODO : inflate .xml
        AlertDialog.Builder dialogComment = new AlertDialog.Builder(
                MainActivity.this, R.style.InputCommentDialog);
        dialogComment.setTitle(R.string.title_comment);

        View viewComment = LayoutInflater.from(MainActivity.this).inflate(R.layout.comment_dialog,
                null);
        final EditText inputComment = viewComment.findViewById(R.id.input_comment);
        inputComment.setText(comment);
        inputComment.setHint(R.string.hint_comment);
        dialogComment.setView(viewComment);

        // to comment
        dialogComment.setPositiveButton(R.string.button_valid_comment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        comment = inputComment.getText().toString();
                    }
                });

        // to comment
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

    public void historyClick(View view) {
        Intent i = new Intent(MainActivity.this, MoodHistoryActivity.class);
        startActivity(i);
    }

    public void wrongDateDialog() {
        AlertDialog.Builder wrongDate = new AlertDialog.Builder(MainActivity.this);
        wrongDate.setTitle(R.string.title_wrong_date);
        wrongDate.setMessage(R.string.message_wrong_date);
        wrongDate.setCancelable(false);

        wrongDate.setPositiveButton(R.string.button_valid_date,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (moodUtils.checkSavedDate(MainActivity.this) >= 0)
                            goodDate = true;
                        else {
                            wrongDateDialog();
                            Toast.makeText(MainActivity.this,
                                    R.string.toast_date_always_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onResume() {
        goodDate = true;
        if (moodUtils.checkSavedDate(this) < 0) {
            goodDate = false;
            this.wrongDateDialog();
        } else if (moodUtils.checkSavedDate(this) > 0) {
            moodUtils.manageHistory(this);
            comment = "";
            mPager.setCurrentItem(2);
            Toast.makeText(this, R.string.toast_new_day, Toast.LENGTH_SHORT).show();
        } else if (moodUtils.checkSavedDate(this) == 0) {
            int lastMood = moodUtils.getLastMood(this);
            if (lastMood > -1) mPager.setCurrentItem(lastMood);
            comment = moodUtils.getLastComment(this);
        }
        else mPager.setCurrentItem(2);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (goodDate)
            moodUtils.saveCurrentMood(this, mPager.getCurrentItem(), comment);
        super.onPause();
    }
}
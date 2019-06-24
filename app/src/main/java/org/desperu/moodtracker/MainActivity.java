package org.desperu.moodtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

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
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);
    }

    public void commentClick(View view) {
        AlertDialog.Builder dialogComment = new AlertDialog.Builder(
                MainActivity.this, R.style.InputCommentDialog);
        dialogComment.setTitle(R.string.title_comment);

        final EditText inputComment = new EditText(MainActivity.this);
        inputComment.setText(moodUtils.getLastComment(this));
        if (inputComment.getText() != null && inputComment.getText().length() > 0)
            comment = inputComment.getText().toString();
        inputComment.setHint(R.string.hint_comment);
        inputComment.getBackground().mutate().setColorFilter(getResources().getColor(
                R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50, 0, 50, 0);
        inputComment.setLayoutParams(params);
        container.addView(inputComment);
        dialogComment.setView(container);

        dialogComment.setPositiveButton(R.string.button_valid_comment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        comment = inputComment.getText().toString();
                        moodUtils.saveCurrentMood(getBaseContext(),
                              mPager.getCurrentItem(), comment);
                    }
                });

        dialogComment.setNegativeButton(R.string.button_cancel_comment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // TODO : Useless?
                        inputComment.getText().clear();
                        comment = "";
                        moodUtils.saveCurrentMood(getBaseContext(),
                                mPager.getCurrentItem(), comment);
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
                        dialog.cancel(); // TODO : Useless??
                        goodDate = true; // TODO : + go to onCreate to restart apk??
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
        // TODO : Test getCurrentItem from ViewPager
        Toast.makeText(this, "MainActivity.onResume " + mPager.getCurrentItem(),
                Toast.LENGTH_SHORT).show();

        if (moodUtils.checkSavedDate(this) < 0) {
            goodDate = false;
            this.wrongDateDialog();
        } else if (moodUtils.checkSavedDate(this) > 0) {
            moodUtils.manageHistory(this);
            mPager.setCurrentItem(2);
        } else if (moodUtils.checkSavedDate(this) == 0) {
            int lastMood = moodUtils.getLastMood(this);
            if (lastMood > -1) mPager.setCurrentItem(lastMood);
        }
        else mPager.setCurrentItem(2);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (goodDate)
            moodUtils.saveCurrentMood(this, mPager.getCurrentItem(), comment);
        super.onStop();
    }
}
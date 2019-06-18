package org.desperu.moodtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MoodAdapter mAdapter;
    VerticalViewPager mPager;
    MoodUtils moodUtils = new MoodUtils();

    private String comment;
    private boolean goodDate = true;
    // TODO : use string for all Toast
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
        inputComment.setText(moodUtils.getLastComment(getBaseContext()));
        inputComment.setHint(R.string.hint_comment);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        llParams.setMargins(50, 0, 50, 0); //don't function
        inputComment.setLayoutParams(llParams);
        dialogComment.setView(inputComment);

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
                        dialog.cancel();
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

    // TODO : check if pass on onResume, i think no
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
                        goodDate = true; // TODO : go to onCreate to restart apk??
                        //dialog.cancel(); // TODO : Useless??
                        moodUtils.deleteAllMood(MainActivity.this);
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
        Toast.makeText(getBaseContext(), "MainActivity.onResume " + mPager.getCurrentItem(),
                Toast.LENGTH_SHORT).show();

        if (moodUtils.checkSavedDate(getBaseContext()) < 0) {
            goodDate = false;
            this.wrongDateDialog();
        } else if (moodUtils.checkSavedDate(getBaseContext()) > 0) {
            moodUtils.manageHistory(getBaseContext(), true);
            comment = null;
        } else if (moodUtils.checkSavedDate(getBaseContext()) == 0) {
            int lastMood = moodUtils.getLastMood(getBaseContext());
            if (lastMood > -1) mPager.setCurrentItem(lastMood);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (goodDate)
            moodUtils.saveCurrentMood(getBaseContext(), mPager.getCurrentItem(), comment);
        super.onStop();
    }
}
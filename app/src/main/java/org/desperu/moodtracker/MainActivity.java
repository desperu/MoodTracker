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

    private String inputText;
    private boolean goodDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MoodAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);
    }

    public void historyClick(View view) {
            Intent i = new Intent(MainActivity.this, MoodHistoryActivity.class);
            startActivity(i);
    }

    public void commentClick(View view) {
        AlertDialog.Builder dialogComment = new AlertDialog.Builder(
                MainActivity.this, R.style.InputCommentDialog);
        dialogComment.setTitle("Comment");

        final EditText inputComment = new EditText(MainActivity.this);
        inputComment.setText(moodUtils.getLastComment(getBaseContext()));
        inputComment.setHint(R.string.hint_comment);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        llParams.setMargins(50, 0, 50, 0); //don't function
        inputComment.setLayoutParams(llParams);
        dialogComment.setView(inputComment);

        dialogComment.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        inputText = inputComment.getText().toString();
                        moodUtils.saveCurrentMood(getBaseContext(),
                                mPager.getCurrentItem(), inputText);
                    }
                });

        dialogComment.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        inputComment.getText().clear();
                        inputText = "";
                        moodUtils.saveCurrentMood(getBaseContext(),
                                mPager.getCurrentItem(), inputText);
                    }
                });
        dialogComment.show();
    }

    // Call method from ViewPager get and set currentItem
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
        // Test getCurrentItem from ViewPager
        Toast.makeText(getBaseContext(), "MainActivity.onResume " + mPager.getCurrentItem(),
                Toast.LENGTH_SHORT).show();

        if (moodUtils.checkSavedDate(getBaseContext()) < 0) {
            goodDate = false;
            Toast.makeText(getBaseContext(), "The date isn't good!! You can't change the past!!!", Toast.LENGTH_LONG).show();
            // TODO print this message in a dialog box? and answer for RAZ??
            finishAffinity(); // to see the result
            //MainActivity.this.finish(); // don't kill process
            // TODO : do nothing if the date change when the activity have the focus
        } else if (moodUtils.checkSavedDate(getBaseContext()) > 0) {
            moodUtils.manageHistory(getBaseContext(), true);
            inputText = null;
        } else {
            int lastMood = moodUtils.getLastMood(getBaseContext());
            if (lastMood > -1) mPager.setCurrentItem(lastMood);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (goodDate) {
            moodUtils.saveCurrentMood(getBaseContext(), MoodAdapter.currentPage, "");
        }
        super.onStop();
    }
}
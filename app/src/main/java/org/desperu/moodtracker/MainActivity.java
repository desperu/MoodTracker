package org.desperu.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {

    MyAdapter mAdapter;
    VerticalViewPager mPager;

    EditText comment = null;
    static String inputText;

    boolean goodDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);

        // check if there's a mood saved when the activity was killed, and print it
        // TODO : comment after restart print??
        History check = new History();
        int lastMood = check.checkLastMood(getBaseContext());
        if (lastMood > -1)
            mPager.setCurrentItem(lastMood);

        ImageButton history_button = findViewById(R.id.history_button);
        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,History.class);
                startActivity(i);
            }
        });

        // It's rocks but can do better
        // TODO : use dialog box...
        ImageButton comment_button = findViewById(R.id.comment_button);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showNext();
                // TODO : call keyboard and onBackPressed close comment dialog
            }
        });

        comment = findViewById(R.id.write_comment);
        comment.addTextChangedListener(textWatcher);


        Button cancel_button = findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment.clearComposingText();
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showPrevious();
                // TODO : hide keyboard
            }
        });

        Button ok_button = findViewById(R.id.button_ok);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = String.valueOf(comment.getText());
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showPrevious();
            }
        });

        //        EditTextPreference
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
        // Test getCurrentItem for ViewPager
        Toast.makeText(getBaseContext(), "MainActivity.onResume " + mPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
        History checkDate = new History();
        if (!checkDate.checkSavedDate(getBaseContext())) {
            goodDate = false;
            Toast.makeText(getBaseContext(), "The date isn't good!! You can't change the past!!!", Toast.LENGTH_LONG).show();
            // TODO print this message in a dialog box?
            finish(); // don't kill process
            //onDestroy(); // too violent can't print toast
        }
        super.onResume();
    }

    // Better here than MoodFragment.onStop ??? Yes less power used... save current date when is modif!!!
    protected void onStop() {
        if (goodDate) {
            History saveCurrentItem = new History();
            saveCurrentItem.saveCurrentMood(getBaseContext(), MyAdapter.currentPage);
        }
        super.onStop();
    }
}
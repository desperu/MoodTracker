package org.desperu.moodtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    MyAdapter mAdapter;
    VerticalViewPager mPager;

    ImageButton historyButton = null;
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

        onResume(); // to check date before reed in pref file

        History check = new History();
        int lastMood = check.getLastMood(getBaseContext());
        if (lastMood > -1)
            mPager.setCurrentItem(lastMood);

        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(historyButtonListener);

        // Witch code is better??
        // It's rocks but can do better
        // TODO : use dialog box...
        ImageButton commentButton = findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showNext();
                Toast.makeText(getBaseContext(), "MainActivity.onClick-comment_button " + getCurrentFocus(), Toast.LENGTH_SHORT).show();
                // TODO : to test with view id for keyboard and onBackPressed action
                ((InputMethodManager) Objects.requireNonNull(getSystemService(Activity.INPUT_METHOD_SERVICE)))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
                //getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                // TODO : call keyboard not perfect =( and onBackPressed close comment dialog
            }
        });

        Button cancel_button = findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment.getText().clear();
                // fonctionne pas bien, test inputText = null and resave
                History delComment = new History();
                delComment.delLastComment(getBaseContext());
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showPrevious();
                ((InputMethodManager) Objects.requireNonNull(getSystemService(Activity.INPUT_METHOD_SERVICE)))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button ok_button = findViewById(R.id.ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = String.valueOf(comment.getText());
                History save = new History();
                save.saveCurrentMood(getBaseContext(), mPager.getCurrentItem());
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showPrevious();
                ((InputMethodManager) Objects.requireNonNull(getSystemService(Activity.INPUT_METHOD_SERVICE)))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        });

        comment = findViewById(R.id.write_comment);
        comment.addTextChangedListener(textWatcher);
        comment.setText(check.getLastComment(getBaseContext()));

        //        EditTextPreference
    }

    // Witch code is better??
    private View.OnClickListener historyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, History.class);
            startActivity(i);
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        // TODO : override useless???
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    // Call method from ViewPager get and set currentItem
    @Override
    public void onBackPressed() {
        // TODO : si vue active is better than si pair si impaire pensez aux bouttons aussi
        //ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
        //viewSwitcher.showPrevious();
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
        // TODO : all in the same method ? in checkSavedDate???
        History checkDate = new History();
        if (checkDate.checkSavedDate(getBaseContext()) < 0) {
            goodDate = false;
            Toast.makeText(getBaseContext(), "The date isn't good!! You can't change the past!!!", Toast.LENGTH_LONG).show();
            // TODO print this message in a dialog box?
            finish(); // don't kill process
            //onDestroy(); // too violent can't print toast
            // TODO : do nothing if the date change when the activity have the focus
        } else if (checkDate.checkSavedDate(getBaseContext()) > 0) {
            History newDate = new History();
            newDate.manageHistory(getBaseContext());
        }
        super.onResume();
    }

    // Better here than MoodFragment.onStop ??? Yes less power used... save current date when is modif!!!
    // onDestroy??? I think it's dangerous to lose data
    protected void onStop() {
        if (goodDate) {
            History saveCurrentItem = new History();
            saveCurrentItem.saveCurrentMood(getBaseContext(), MyAdapter.currentPage);
        }
        super.onStop();
    }
}
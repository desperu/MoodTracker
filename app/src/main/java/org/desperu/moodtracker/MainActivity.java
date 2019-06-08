package org.desperu.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {

    MyAdapter mAdapter;
    VerticalViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);

        // check if there's a mood saved, and print it
        History check = new History();
        int lastMood = check.checkLastMood(getBaseContext());
        if (lastMood > -1)
            mPager.setCurrentItem(lastMood);

        // TODO : check that the date is not modif by the user
        //button click here or in fragment view
        /*ImageButton comment_button = (ImageButton)findViewById(R.id.comment_button);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Comment.class);
                startActivity(i);
            }
        });*/

        ImageButton history_button = findViewById(R.id.history_button);
        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,History.class);
                startActivity(i);
            }
        });

        // It's rocks but can do better
        ImageButton comment_button = findViewById(R.id.comment_button);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
                viewSwitcher.showNext();
            }
        });

        //        EditTextPreference

        /*ImageButton show_ctrls = (ImageButton)findViewById(R.id.show_ctrls);
        show_ctrls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSwitcher ctrls = (ViewSwitcher)findViewById(R.id.ctrls);
                ctrls.showNext();
            }
        });*/
        /*FloatingActionButton comment_layout = findViewById(R.id.comment_layout);
        comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,comment_layout.class);
                startActivity(i);
            }
        });*/
    }

    // Call method from ViewPager get and set currentItem
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "MainActivity.onResume " + mPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
        // TODO : check date saved with the current date
    }

    /* Better here than MoodFragment.onStop ???
    @Override
    protected void onStop() {
        History saveCurrentItem = new History();
        saveCurrentItem.saveCurrentMood(getBaseContext(), MyAdapter.currentPage);
        super.onStop();
    }*/
}
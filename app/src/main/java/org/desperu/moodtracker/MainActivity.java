package org.desperu.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    MyAdapter mAdapter;
    VerticalViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent i = new Intent(MainActivity.this,History.class);
        //startActivity(i);
        // il en veut pas
        //Comment test = new Comment();
        //test.onCreate(savedInstanceState);
        //History checkLastMood = new History();
        //checkLastMood.onCreate(savedInstanceState);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);

        // TODO : onback pressed!!!
        // TODO : check that the date is not modiate by the user
        //button click here or in fragment view
        ImageButton comment_button = (ImageButton)findViewById(R.id.comment_button);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Comment.class);
                startActivity(i);
            }
        });

        ImageButton history_button = (ImageButton)findViewById(R.id.history_button);
        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,History.class);
                startActivity(i);
            }
        });
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
}
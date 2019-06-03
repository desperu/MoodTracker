package org.desperu.moodtracker;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    MyAdapter mAdapter;
    VerticalViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //vue obligatoire sinon ça bug
        setContentView(R.layout.activity_main);

        //History checkLastMood = new History();
        //checkLastMood.checkHistory();
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);

        //button click here or in fragment view
        /*ImageButton button_comment = (ImageButton)findViewById(R.id.button_comment);
        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText ctrls = (TextInputEditText)findViewById(R.id.input_text_comment);
                ctrls.isShown();
            }
        });*/
        /*ImageButton show_ctrls = (ImageButton)findViewById(R.id.show_ctrls);
        show_ctrls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSwitcher ctrls = (ViewSwitcher)findViewById(R.id.ctrls);
                ctrls.showNext();
            }
        });*/
        /*FloatingActionButton comment = findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Comment.class);
                startActivity(i);
            }
        });*/
    }
}
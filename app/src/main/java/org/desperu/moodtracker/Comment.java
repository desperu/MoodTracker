package org.desperu.moodtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//ne fonctionne pas
public class Comment extends AppCompatActivity {
    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        // bug, call with another method
        //setContentView(findViewById(R.id.comment_dialog));

        Toast.makeText(getBaseContext(), "comment!!", Toast.LENGTH_SHORT).show();
    }
}

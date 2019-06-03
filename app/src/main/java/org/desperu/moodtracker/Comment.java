package org.desperu.moodtracker;

import android.os.Bundle;
import android.widget.Toast;

//ne fonctionne pas
public class Comment extends MainActivity {
    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);

        Toast.makeText(getBaseContext(), "Comment!!", Toast.LENGTH_SHORT).show();
    }
}

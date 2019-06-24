package org.desperu.moodtracker.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.desperu.moodtracker.R;

public class MoodFragment extends Fragment {

    View moodFragment = null;
    private static final String ARG_PARAM1 = "param1";
    private int layoutId;

    public MoodFragment() {
        // Required empty public constructor
    }

    public static MoodFragment newInstance(int position) {
        MoodFragment fragment = new MoodFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layoutId = getArguments().getInt(ARG_PARAM1);
        }
    }

    public void setMoodFragment(String color, int drawable) {
        moodFragment.findViewById(R.id.fragment).setBackgroundColor(Color.parseColor(color));
        ImageView superHappy = moodFragment.findViewById(R.id.mood_image);
        superHappy.setImageResource(drawable);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moodFragment = inflater.inflate(R.layout.mood_fragment, container, false);

        switch (layoutId) { // TODO : use color.xml, and model MVC, cmment the code to explain
            case 0: this.setMoodFragment("#fff9ec4f", R.drawable.smiley_super_happy); break;
            case 1: this.setMoodFragment("#ffb8e986", R.drawable.smiley_happy); break;
            case 2: this.setMoodFragment("#a5468ad9", R.drawable.smiley_normal); break;
            case 3: this.setMoodFragment("#ff9b9b9b", R.drawable.smiley_disappointed); break;
            case 4: this.setMoodFragment("#ffde3c50", R.drawable.smiley_sad); break;
            default: this.setMoodFragment("#a5468ad9", R.drawable.smiley_normal);
        }
        return moodFragment;
    }
}

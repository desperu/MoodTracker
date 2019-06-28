package org.desperu.moodtracker.controller;

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

    /**
     * Create mood fragment view with color and smiley given.
     * @param color Background color to show.
     * @param drawable Smiley to show.
     */
    public void setMoodFragment(int color, int drawable) {
        moodFragment.findViewById(R.id.fragment).setBackgroundColor(getResources().getColor(color));
        ImageView superHappy = moodFragment.findViewById(R.id.mood_image);
        superHappy.setImageResource(drawable);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moodFragment = inflater.inflate(R.layout.mood_fragment, container, false);

        // Switch between color and smiley depending of position given.
        switch (layoutId) { // TODO : use color.xml, and model MVC, comment the code to explain
            case 0: this.setMoodFragment(R.color.colorSuperHappy, R.drawable.smiley_super_happy); break;
            case 1: this.setMoodFragment(R.color.colorHappy, R.drawable.smiley_happy); break;
            case 2: this.setMoodFragment(R.color.colorNormal, R.drawable.smiley_normal); break;
            case 3: this.setMoodFragment(R.color.colorDisappointed, R.drawable.smiley_disappointed); break;
            case 4: this.setMoodFragment(R.color.colorSad, R.drawable.smiley_sad); break;
            default: this.setMoodFragment(R.color.colorNormal, R.drawable.smiley_normal);
        }
        return moodFragment;
    }
}

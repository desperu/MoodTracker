package org.desperu.moodtracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoodFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private int layoutId;

    public MoodFragment() {
        // Required empty public constructor
    }

    // TODO : switch between background and smiley
    public static MoodFragment newInstance(int position) {
        MoodFragment fragment = new MoodFragment();
        Bundle args = new Bundle();

        int layoutFragment;
        switch (position) {
            case 0 : layoutFragment = R.layout.super_happy_layout; break;
            case 1 : layoutFragment = R.layout.happy_layout; break;
            case 2 : layoutFragment = R.layout.normal_layout; break;
            case 3 : layoutFragment = R.layout.disappointed_layout; break;
            case 4 : layoutFragment = R.layout.sad_layout; break;
            default : layoutFragment = R.layout.super_happy_layout;
        }

        args.putInt(ARG_PARAM1, layoutFragment);
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(layoutId, container, false);
    }
}

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

    // TODO : switch between background and smiley or Relative =)
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
        // Why need to put in Bundle??? Because Fragment function with?
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

    /*
    @Override
    public void onStart() {
        super.onStart();
        // TODO : test call sharedpreferences, it's rocks but prechargement!!!
        History saveCurrentItem = new History();
        saveCurrentItem.saveCurrentMood(getActivity(), MyAdapter.currentPage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(context, "MoodFragment.onAttach", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getContext(), "MoodFragment.onSaveInstanceState", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Toast.makeText(getContext(), "MoodFragment.onViewStateRestored", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onStop() {
        History saveCurrentItem = new History();
        saveCurrentItem.saveCurrentMood(getContext(), MyAdapter.currentPage);
        super.onStop();
    }
}

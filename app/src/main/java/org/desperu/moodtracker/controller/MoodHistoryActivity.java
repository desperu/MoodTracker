package org.desperu.moodtracker.controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.desperu.moodtracker.R;
import org.desperu.moodtracker.utils.MoodUtils;

import static org.desperu.moodtracker.MoodTools.Constant.*;
import static org.desperu.moodtracker.MoodTools.ScreenConstant.*;
import static org.desperu.moodtracker.MoodTools.Keys.*;

public class MoodHistoryActivity extends AppCompatActivity {

    private View historyView = null;
    private MoodUtils moodUtils = new MoodUtils();
    private int screenWidth;
    private int screenHeight;
    private boolean landscape = false;
    private int[] mood = new int[numberOfDays];
    private long[] date = new long[numberOfDays];
    private String[] comment = new String[numberOfDays];
    private int[] idRl = new int[numberOfDays];
    private int[] idShow = new int[numberOfDays];
    private int[] idShare = new int[numberOfDays];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getHistoryTabs();
        setContentView(this.onCreateHistoryView());
    }

    /**
     * Get history values, and put in tabs.
     */
    private void getHistoryTabs() {
        for (int i = 0; i <= (numberOfDays - 1); i++) {
            mood[i] = moodUtils.getIntPrefs(this, moodHistoryFile, moodHistory + (i + 1));
            date[i] = moodUtils.getLongPrefs(this, moodHistoryFile, dateHistory + (i + 1));
            comment[i] = moodUtils.getStringPrefs(this,
                    moodHistoryFile, commentHistory + (i + 1));
        }
    }

    /**
     * Create history mood view, set params for each history mood.
     * @return The view create.
     */
    private View onCreateHistoryView() {
        historyView = LayoutInflater.from(this).inflate(R.layout.activity_mood_history,
                (ViewGroup)findViewById(R.id.history_linear));

        // Switch between color and size for each history mood.
        for (int i = (numberOfDays - 1); i >= 0; i--) {
            switch (mood[i]) {
                case 0: this.onCreateHistoryMood(i, R.color.colorSuperHappy, sHappyWidth);
                    break;
                case 1: this.onCreateHistoryMood(i, R.color.colorHappy, happyWidth);
                    break;
                case 2: this.onCreateHistoryMood(i, R.color.colorNormal, normalWidth);
                    break;
                case 3: this.onCreateHistoryMood(i, R.color.colorDisappointed, disappointedWidth);
                    break;
                case 4: this.onCreateHistoryMood(i, R.color.colorSad, sadWidth);
                    break;
                default: this.onCreateHistoryMood(i, R.color.colorNoMood, noMoodWidth);
            }
        }
        return historyView;
    }

    /**
     * Create RelativeLayout for each history mood, with given params.
     * @param i Number of the history mood, so its position.
     * @param color Background color depending of the mood.
     * @param moodWidth Size of the RelativeLayout, depending of the mood.
     */
    private void onCreateHistoryMood(int i, int color, double moodWidth) {
        this.getScreenWidthHeight(); // Get screen size

        // Create RelativeLayout and set basic params.
        RelativeLayout rLayoutDay = new RelativeLayout(this);
        rLayoutDay.setBackgroundColor(getResources().getColor(color));
        rLayoutDay.setTop(screenHeight * ((numberOfDays - 1 - i) / numberInScreen));
        rLayoutDay.setId(View.generateViewId());
        idRl[i] = rLayoutDay.getId();

        // Create LayoutParams object to set more params.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (screenWidth * (moodWidth)), screenHeight / numberInScreen);

        // Add the RelativeLayout created to the root view and apply params.
        LinearLayout history = historyView.findViewById(R.id.history_linear);
        history.addView(rLayoutDay, params);

        // If there's a comment for this mood, create image button to show comment
        // and another to share mood.
        if (comment[i] != null && comment[i].length() > 0) {

            // Change left margin imageButtons depending of screen orientation.
            double imageLeftMargin = imageWidthPortrait;
            if (landscape) imageLeftMargin = imageWidthLandscape;

            // Create show comment button.
            this.onCreateImageButton(i, (int) (screenWidth * (moodWidth - imageLeftMargin)),
                    R.drawable.ic_comment_black_48px, showCommentListener, idShow);

            // Create share button.
            this.onCreateImageButton(i, (int) (screenWidth * (moodWidth - imageLeftMargin * shareMarginLeft)),
                    R.drawable.baseline_share_black_24, shareListener, idShare);
        }
        this.onCreateMoodAgeTextView(i);// Create TextView for mood age, and set params.
    }

    /**
     * Get real usable screen size for the view, Width and Height.
     */
    private void getScreenWidthHeight() {
        // Get status bar height (on top of screen).
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0)
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        // Get action bar height (below status bar, where's activity name).
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        TypedArray a = this.obtainStyledAttributes(new TypedValue().data, textSizeAttr);
        int actionBarHeight = a.getDimensionPixelSize(0, 0);
        a.recycle();

        // Get screen orientation.
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            landscape = true;

        // To correct little screen size difference.
        double correct = correctPortrait;
        if (landscape) correct = correctLandscape;

        //Get full screen size with DisplayMetrics, minus statusBarHeight and actionBarHeight.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = (int) ((displayMetrics.heightPixels - statusBarHeight- actionBarHeight ) *
                correct);
    }

    /**
     * Create Comment Image Button and Share Image Button, for given mood.
     * @param i Number of the history mood.
     * @param marginLeft Margin left, to position share and comment image button.
     * @param drawable To set corresponding image.
     * @param listener To set corresponding listener.
     * @param idTab To save create resource id.
     */
    private void onCreateImageButton(int i, int marginLeft, int drawable,
                                    View.OnClickListener listener, int[] idTab) {
        int marginTop = 0;
        // Create ImageButton and set basic params.
        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageResource(drawable);
        imageButton.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        imageButton.setOnClickListener(listener);
        imageButton.setId(View.generateViewId());
        idTab[i] = imageButton.getId();

        // For sad mood, push a little to bottom the imageButtons
        // for they don't superpose with mood age text.
        if (mood[i] == 4) marginTop = screenHeight / (numberInScreen * imageSadTop);

        // Change width imageButtons depending of screen orientation.
        double imageWidth = imageWidthPortrait;
        if (landscape) imageWidth = imageWidthLandscape;

        // Create LayoutParams object to set more params.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (screenWidth * imageWidth), ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(marginLeft, marginTop, 0, 0);

        // Add childView with corresponding Mood RelativeLayout and apply params.
        RelativeLayout rlDay = historyView.findViewById(idRl[i]);
        rlDay.addView(imageButton, params);
    }

    /**
     * Set mood age text, for given mood.
     * @param i Number of the history mood.
     * @return Mood age text.
     */
    private String getMoodAgeText(int i) {
        int age = moodUtils.compareTime(date[i]);
        if (age == moodUtils.compareTime(0)) return getString(R.string.no_mood);
        else if (age == 1) return getString(R.string.text_yesterday);
        else if (age < 7) return getResources().getString(R.string.text_day, age);
        else if (age < 30) return getResources().getString(R.string.text_week, (int) (age / 7));
        else if (age < 365) return getResources().getString(R.string.text_month, (int) (age / 30));
        else if (age < 36500) return getResources().getString(R.string.text_year, (int) (age / 365));
        return getString(R.string.text_problem);
    }

    /**
     * Create mood age text, for given mood.
     * @param i Number of the history mood.
     */
    private void onCreateMoodAgeTextView(int i) {
        // Create mood age text and set basic params.
        TextView moodAgeText = new TextView(this);
        moodAgeText.setText(getMoodAgeText(i));
        moodAgeText.setTextSize(16); // It use sp

        // Create LayoutParams object to set more params.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenWidth / textMargin, screenWidth / textMargin, 0, 0);

        // Add childView with corresponding Mood RelativeLayout and apply params.
        RelativeLayout rlDay = historyView.findViewById(idRl[i]);
        rlDay.addView(moodAgeText, params);
    }

    /**
     * Listener to show corresponding mood comment with a toast message.
     */
    private View.OnClickListener showCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = (numberOfDays - 1); i >= 0; i--) {
                if (v.getId() == idShow[i]) { // Get history mood number from this listener is called.
                    Toast customToast = Toast.makeText(MoodHistoryActivity.this,
                            comment[i], Toast.LENGTH_LONG);

                    // Get toast view and toast message to set custom params.
                    View toastView = customToast.getView();
                    TextView toastMessage = toastView.findViewById(android.R.id.message);

                    // Set params and show toast.
                    toastMessage.setTextSize(18); // It use sp
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setPadding(screenWidth / toastPadding, 0,
                            screenWidth / toastPadding, 0);
                    toastView.setBackgroundColor(getResources().getColor(
                            R.color.colorBackgroundShowComment));
                    toastView.setMinimumWidth(screenWidth - screenWidth / toastMinWidth);
                    customToast.setGravity(Gravity.BOTTOM,0, screenHeight / toastMargin);
                    customToast.show();
                }
            }
        }
    };

    /**
     * Listener to share mood age, mood and corresponding comment, with wanted user send method.
     */
    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = (numberOfDays - 1); i >= 0; i--) {
                if (v.getId() == idShare[i]) { // Get history mood number from this listener is called.
                    // Get text for corresponding mood.
                    String moodDay = moodUtils.moodShareText(
                            MoodHistoryActivity.this, mood[i], -1);

                    // Create intent with share text.
                    Intent share = moodUtils.prepareShareIntent(getMoodAgeText(i) + moodDay
                            + getString(R.string.with_comment) + comment[i]);

                    // Create application chooser menu to send intent.
                    startActivity(Intent.createChooser(share, getString(R.string.share_title)));
                }
            }
        }
    };
}
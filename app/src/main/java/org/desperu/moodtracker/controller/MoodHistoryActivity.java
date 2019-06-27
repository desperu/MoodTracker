package org.desperu.moodtracker.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.desperu.moodtracker.utils.MoodUtils;
import org.desperu.moodtracker.R;

public class MoodHistoryActivity extends AppCompatActivity {

    View historyView = null;
    MoodUtils moodUtils = new MoodUtils();
    private int[] mood = new int[7];
    private long[] date = new long[7];
    private String[] comment = new String[7];
    private int[] rLayout = {R.id.day1, R.id.day2, R.id.day3,
            R.id.day4, R.id.day5, R.id.day6, R.id.day7};
    private int[] imageS = {R.id.imageS1, R.id.imageS2, R.id.imageS3,
            R.id.imageS4, R.id.imageS5, R.id.imageS6, R.id.imageS7};
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getHistoryTabs();
        setContentView(this.onCreateHistoryView());
        // TODO : for test only
        Toast.makeText(this, "Test time 0 = " + moodUtils.convertDate(0) +
                " , " + date[6], Toast.LENGTH_LONG).show();
    }

    public void getHistoryTabs() {
        for (int i = 0; i <= 6; i++) {
            mood[i] = moodUtils.getIntHistoryPrefs(this, MoodUtils.moodHistory + (i + 1));
            date[i] = moodUtils.getLongHistoryPrefs(this, MoodUtils.dateHistory + (i + 1));
            comment[i] = moodUtils.getStringHistoryPrefs(this,
                    MoodUtils.commentHistory + (i + 1));
        }
    }

    public View onCreateHistoryView() {

        historyView = LayoutInflater.from(this).inflate(R.layout.activity_mood_history, null);

        for (int i = 6; i >= 0; i--) {
            switch (mood[i]) {
                case 0: this.setHistoryView(i, R.color.colorSuperHappy, 1, 0.85);
                    break;
                case 1: this.setHistoryView(i, R.color.colorHappy, 0.825, 0.675);
                    break;
                case 2: this.setHistoryView(i, R.color.colorNormal, 0.65, 0.50);
                    break;
                case 3: this.setHistoryView(i, R.color.colorDisappointed, 0.475, 0.325);
                    break;
                case 4: this.setHistoryView(i, R.color.colorSad, 0.3, 0.15);
                    break;
                default: this.setHistoryView(i, R.color.colorNoMood, 1, 0);
            }
        }
        return historyView;
    }

    public void setHistoryView(int i, int color, double moodWidth, double smsWidht) {
        this.getScreenWidthHeight();
        RelativeLayout rLayoutDay = new RelativeLayout(this);
        rLayoutDay.setBackgroundColor(getResources().getColor(color));
        rLayoutDay.setTop(screenHeight * ((6 - i) / 7));
        rLayoutDay.setId(rLayout[i]);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = (int) (screenWidth * (moodWidth));
        params.height = screenHeight / 7;
        LinearLayout history = historyView.findViewById(R.id.history);
        history.addView(rLayoutDay, params);
        if (comment[i] != null && comment[i].length() > 0) {
            RelativeLayout rlButton = historyView.findViewById(rLayout[i]);
            rlButton.setOnClickListener(showCommentListener);
            this.setImageShare(i, smsWidht);
            ImageButton imageButtonSMS = historyView.findViewById(imageS[i]);
            imageButtonSMS.setOnClickListener(shareListener);
        }
        this.setMoodAgeText(i);
    }

    public void getScreenWidthHeight() { // TODO : pb if not same decor!!!
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // TODO : to test this.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        if (this.getResources().getConfiguration().orientation == 1) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = (int) (displayMetrics.heightPixels * 0.887);
        } else if (this.getResources().getConfiguration().orientation == 2) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = (int) (displayMetrics.heightPixels * 0.825); // TODO : little white
        }
        //double layoutDecorations = screenHeight * 0.1678; // so 0.832
        // TODO : this.getWindowManager().getDefaultDisplay().getPresentationDeadlineNanos(toto);
    }

    public String getMoodAgeText(int i) {
        int age = moodUtils.convertDate(moodUtils.getTime()) - moodUtils.convertDate(date[i]);
        if(age == (moodUtils.convertDate(moodUtils.getTime()) - moodUtils.convertDate(0)))
            return getString(R.string.no_mood); // TODO : never show, should be good now
        else if (age == 1) return getString(R.string.text_yesterday);
        else if (age < 7) return getResources().getString(R.string.text_day, age);
        else if (age < 100) return getResources().getString(R.string.text_week, (int) (age / 7));
        else if (age < 10000) return getResources().getString(R.string.text_month, (int) (age / 100));
        else if (age < 1000000) return getResources().getString(R.string.text_year, (int) (age / 10000));
        return getString(R.string.problem);
    }

    public void setMoodAgeText(int i) {
        TextView moodAgeText = new TextView(this);
        moodAgeText.setText(getMoodAgeText(i));
        moodAgeText.setTextSize(16); // TODO : use dp TypedValue_COMPLEX_UNIT_SP, 16
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenWidth / 100, screenHeight / 100, 0, 0);
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(moodAgeText, params);
    }

    public void setImageShare(int i, double smsWidht) {// TODO : Comment code !!!!!
        ImageButton imageSMS = new ImageButton(this);
        imageSMS.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        imageSMS.setImageResource(R.drawable.ic_comment_black_48px);
        imageSMS.setId(imageS[i]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) (screenWidth * smsWidht), 0,
                0, 0);
        params.width = screenWidth / 6;
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(imageSMS, params);
    }

    public View.OnClickListener showCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // TODO : inflate .xml
            for (int i = 6; i >= 0; i--) {
                if (v.getId() == rLayout[i]) {
                    Toast toast = Toast.makeText(MoodHistoryActivity.this,
                            comment[i], Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18); // it use sp
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setPadding(20, 0, 20, 0); // TODO : use dp
                    toastView.setBackgroundColor(getResources().getColor(
                            R.color.colorBackgroundShowComment));
                    toastView.setMinimumWidth(screenWidth - screenWidth / 20);
                    /*RelativeLayout container = new RelativeLayout(MoodHistoryActivity.this);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(50, 0, 50, 0);
                    toastView.setLayoutParams(params);
                    container.addView(toastView);
                    toast.setView(container);*/
                    toast.setGravity(Gravity.BOTTOM,0, 50); // TODO : use dp
                    toast.show();
                }
            }
        }
    };

    public View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 6; i >= 0; i--) {
                if (v.getId() == imageS[i]) {
                    String moodDay;
                    switch (mood[i]) {
                        case 0: moodDay = getString(R.string.mood_day_super_happy); break;
                        case 1: moodDay = getString(R.string.mood_day_happy); break;
                        case 2: moodDay = getString(R.string.mood_day_normal); break;
                        case 3: moodDay = getString(R.string.mood_day_disappointed); break;
                        case 4: moodDay = getString(R.string.mood_day_sad); break;
                        default: moodDay = " : ";
                    }
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, getMoodAgeText(i) + moodDay +
                            comment[i]);
                    startActivity(Intent.createChooser(share, getString(R.string.share_title)));
                }
            }
        }
    };
}
package org.desperu.moodtracker.controller;

import android.content.Intent;
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

    // TODO : Comment code !!!!!
    public void setHistoryView(int i, int color, double moodWidth, double smsWidht) {
        this.getScreenWidthHeight();
        RelativeLayout rLayoutDay = new RelativeLayout(this); // TODO : use ImageButton
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
            ImageButton imageButtonShare = historyView.findViewById(imageS[i]);
            imageButtonShare.setOnClickListener(shareListener);
        }
        this.setMoodAgeText(i);
    }

    /**
     * Get usable screen Width and Height for the view, minus action bar, title application and status bar
     */
    public void getScreenWidthHeight() {
        // Get action bar height
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        TypedArray a = this.obtainStyledAttributes(new TypedValue().data, textSizeAttr);
        int actionBarHeight = a.getDimensionPixelSize(0, 0);
        a.recycle();
        // Get status bar height
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) // TODO : ???
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        //Get full screen size with DisplayMetrics, minus actionBarHeight(with title app) and statusBarHeight
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels - actionBarHeight - statusBarHeight;

        /*if (this.getResources().getConfiguration().orientation == 1) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = (int) (displayMetrics.heightPixels * 0.887);
        } else if (this.getResources().getConfiguration().orientation == 2) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = (int) (displayMetrics.heightPixels * 0.825);
        }*/
        //double layoutDecorations = screenHeight * 0.1678; // so 0.832
    }

    public String getMoodAgeText(int i) {
        int age = moodUtils.convertDate(moodUtils.getTime()) - moodUtils.convertDate(date[i]);
        if(age == (moodUtils.convertDate(moodUtils.getTime()) - moodUtils.convertDate(0)))
            return getString(R.string.no_mood);
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

    public void setImageShare(int i, double smsWidht) {
        // create ImageButton and set basic params
        ImageButton imageShare = new ImageButton(this);
        imageShare.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        //imageShare.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        imageShare.setImageResource(R.drawable.ic_comment_black_48px);
        imageShare.setId(imageS[i]);
        // create RelativeLayout.LayoutParams object to set special params
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) (screenWidth * smsWidht), 0,
                0, 0);
        params.width = screenWidth / 10;//6;
        // object to add imageShare with corresponding Mood RelativeLayout
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(imageShare, params);
    }

    public View.OnClickListener showCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // TODO : inflate .xml
            for (int i = 6; i >= 0; i--) {
                if (v.getId() == rLayout[i]) {
                    /*View customToast = LayoutInflater.from(MoodHistoryActivity.this).
                            inflate(R.layout.custom_toast, null);
                    //customToast.setMinimumWidth(screenWidth - screenWidth / 20);

                    TextView showComment = customToast.findViewById(R.id.show_comment);
                    showComment.setText(comment[i]);

                    Toast toast =  new Toast(MoodHistoryActivity.this);
                    toast.setGravity(Gravity.BOTTOM, 0, screenHeight / 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    //toast.setMargin(1f, 0f);
                    toast.setView(customToast);
                    //toast.setMargin((screenWidth / 100f), (screenHeight / 50f));
                    toast.show();*/

                    Toast toast = Toast.makeText(MoodHistoryActivity.this,
                            comment[i], Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18); // it use sp
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setPadding(screenWidth / 50, 0, screenWidth / 50, 0);
                    toastView.setBackgroundColor(getResources().getColor(
                            R.color.colorBackgroundShowComment));
                    toastView.setMinimumWidth(screenWidth - screenWidth / 20);
                    /*RelativeLayout container = new RelativeLayout(MoodHistoryActivity.this);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
                    params.setMargins(20, 0, 20, 0);
                    toastView.setLayoutParams(params);*/
                    /*container.setLayoutParams(params);
                    container.addView(toastView);
                    toast.setView(container);*/
                    toast.setGravity(Gravity.BOTTOM,0, screenHeight / 50);
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
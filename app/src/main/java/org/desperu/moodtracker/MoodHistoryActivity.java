package org.desperu.moodtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoodHistoryActivity extends AppCompatActivity {

    View historyView = null;
    MoodUtils moodUtils = new MoodUtils();
    int[] rLayout = {R.id.day1, R.id.day2, R.id.day3,
            R.id.day4, R.id.day5, R.id.day6, R.id.day7};
    int[] imageS = {R.id.imageS1, R.id.imageS2, R.id.imageS3,
            R.id.imageS4, R.id.imageS5, R.id.imageS6, R.id.imageS7};
    int screenWidth;
    int screenHeight;

    // TODO : get tabs here to not use static
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.onCreateHistoryView());
    }

    public View onCreateHistoryView() {

        historyView = LayoutInflater.from(this).inflate(R.layout.activity_mood_history, null);

        MoodUtils getTabs = new MoodUtils();
        getTabs.manageHistory(this, false);

        for (int i = 6; i >= 0; i--) {
            switch (MoodUtils.mood[i]) {
                case 0:
                    this.setHistoryView(i, "#fff9ec4f", 1, 0.85); break;//0.915
                case 1:
                    this.setHistoryView(i, "#ffb8e986", 0.825, 0.675); break;//0.74
                case 2:
                    this.setHistoryView(i, "#a5468ad9", 0.65, 0.50); break;//0.565
                case 3:
                    this.setHistoryView(i, "#ff9b9b9b", 0.475, 0.325); break;//0.39
                case 4:
                    this.setHistoryView(i, "#ffde3c50", 0.3, 0.15); break;//0.175; 0.215
                default:
                    this.setHistoryView(i, "#ffffffff", 1, 0);
            }
        }
        return historyView;
    }

    public void setHistoryView(int i, String color, double moodWidth, double smsWidht) {
        this.getScreenWidthHeight();
        RelativeLayout rLayoutDay = new RelativeLayout(this);
        rLayoutDay.setBackgroundColor(Color.parseColor(color));
        rLayoutDay.setTop(screenHeight * ((6 - i) / 7));
        rLayoutDay.setId(rLayout[i]);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = (int) (screenWidth * (moodWidth));
        params.height = screenHeight / 7;
        LinearLayout history = historyView.findViewById(R.id.history);
        history.addView(rLayoutDay, params);

        /*historyView.findViewById(rLayout[i]).setBackgroundColor(Color.parseColor(color));
        historyView.findViewById(rLayout[i]).setTop(screenHeight * ((6 - i) / 7));
        ViewGroup.LayoutParams params = historyView.findViewById(rLayout[i]).getLayoutParams();
        params.width = (int) (screenWidth * (moodWidth));
        params.height = screenHeight / 7;
        historyView.findViewById(rLayout[i]).setLayoutParams(params);*/

        if (MoodUtils.comment[i] != null && MoodUtils.comment[i].length() > 0) {
            RelativeLayout rlButton = historyView.findViewById(rLayout[i]);
            rlButton.setOnClickListener(showCommentListener);
            this.setImageSMS(i, smsWidht);
            ImageButton imageButtonSMS = historyView.findViewById(imageS[i]);
            imageButtonSMS.setOnClickListener(shareBySMSListener);
        }
        // TODO : test to write on button share, and if no Mood??
        //if (MoodUtils.mood[i] != -1) this.setMoodAgeText(i);
        this.setMoodAgeText(i);
    }

    public void getScreenWidthHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if (this.getResources().getConfiguration().orientation == 1) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = (int) (displayMetrics.heightPixels * 0.887);
            //double layoutDecorations = screenHeight * 0.1678; // so 0.832
        } else if (this.getResources().getConfiguration().orientation == 2) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = (int) (displayMetrics.heightPixels * 0.825);
        }
        // TODO : this.getWindowManager().getDefaultDisplay().getPresentationDeadlineNanos(toto);
    }

    public String getMoodAgeText(int i) {
        int age = moodUtils.convertDate(moodUtils.getTime()) -
                moodUtils.convertDate(MoodUtils.date[i]);
        // TODO : can use switch with < 7 ????
        if (age == 1) return getString(R.string.text_yesterday);
        if (age < 7) return getResources().getString(R.string.text_day, age);
        else if (age < 100) return getResources().getString(R.string.text_week, (int) (age / 7));
        else if (age < 10000) return getResources().getString(R.string.text_month, (int) (age / 30)); // TODO : must do better
        else if (age < 1000000) return getResources().getString(R.string.text_year, (int) (age / 365));
        else return getString(R.string.no_mood); // TODO : don't function
    }

    // TODO : must be on the button to send
    public void setMoodAgeText(int i) {
        TextView moodAgeText = new TextView(this);
        moodAgeText.setText(getMoodAgeText(i));
        moodAgeText.setTextSize(16);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenWidth / 100, screenHeight / 100, 0, 0);//screenWidth /8, 0);
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(moodAgeText, params);
    }

    public void setImageSMS(int i, double smsWidht) {
        ImageButton imageSMS = new ImageButton(this);
        imageSMS.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        imageSMS.setImageResource(R.drawable.ic_comment_black_48px);
        imageSMS.setId(imageS[i]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) (screenWidth * smsWidht), 0,//screenHeight / 22,
                0, 0);// screenHeight / 15);//right :screenWidth / 50
        params.width = screenWidth / 6;
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(imageSMS, params);
    }

    // TODO : must be a Toast???
    public View.OnClickListener showCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 6; i >= 0; i--) {
                if (v.getId() == rLayout[i]) {
                    /*AlertDialog.Builder builder = new AlertDialog.
                            Builder(MoodHistoryActivity.this, R.style.ShowCommentDialog);
                            //Builder(MoodHistoryActivity.this,
                            //android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                    AlertDialog showComment = builder.create();
                    showComment.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    WindowManager.LayoutParams params = showComment.getWindow().getAttributes();
                    params.gravity = Gravity.BOTTOM;
                    params.y = 20;
                    showComment.setMessage(MoodUtils.comment[i]);
                    showComment.show();*/
                    // TODO : make custom Toast
                    Toast.makeText(MoodHistoryActivity.this, MoodUtils.comment[i],Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public View.OnClickListener shareBySMSListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO : Dialog box for ask share media?
            for (int i = 6; i >= 0; i--) {
                if (v.getId() == imageS[i]) {
                    String moodDay;
                    switch (MoodUtils.mood[i]) {
                        case 0: moodDay = getString(R.string.mood_day_super_happy); break;
                        case 1: moodDay = getString(R.string.mood_day_happy); break;
                        case 2: moodDay = getString(R.string.mood_day_normal); break;
                        case 3: moodDay = getString(R.string.mood_day_disappointed); break;
                        case 4: moodDay = getString(R.string.mood_day_sad); break;
                        default: moodDay = " : "; // TODO : Is it good???
                    }
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, getMoodAgeText(i) + moodDay +
                            MoodUtils.comment[i]);
                    startActivity(Intent.createChooser(share, getString(R.string.share_title)));
                }
            }
        }
    };
}
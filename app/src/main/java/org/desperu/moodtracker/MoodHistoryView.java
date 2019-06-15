package org.desperu.moodtracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MoodHistoryView extends AppCompatActivity {

    View historyView = null; // LinearLayout? No not here, must be a View object to be inflate!!
    int[] rLayout = {R.id.Day1, R.id.Day2, R.id.Day3,
            R.id.Day4, R.id.Day5, R.id.Day6, R.id.Day7};
    int[] imageS = {R.id.imageS1, R.id.imageS2, R.id.imageS3,
            R.id.imageS4, R.id.imageS5, R.id.imageS6, R.id.imageS7};
    int screenWidth;
    int screenHeight;

    Context savedContext;
    String noMoodText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noMoodText = getString(R.string.noMood);
        this.getScreenWidthHeight();
        setContentView(this.onCreateHistoryView(getBaseContext()));
        savedContext = getBaseContext();
    }

    public View onCreateHistoryView(Context context) {

        historyView = LayoutInflater.from(context).inflate(R.layout.history_layout, null);

        MoodHistory getTabs = new MoodHistory();
        getTabs.manageHistory(context, false);

        for (int i = 7; i > 0; i--) {
            switch (MoodHistory.mood[i]) {
                case 0:
                    this.setHistoryView(context, rLayout[i - 1], "#fff9ec4f", 1,
                            MoodHistory.comment[i], imageS[i - 1], 0.915);
                    break;
                case 1:
                    this.setHistoryView(context, rLayout[i - 1], "#ffb8e986", 0.825,
                            MoodHistory.comment[i], imageS[i - 1], 0.74);
                    break;
                case 2:
                    this.setHistoryView(context, rLayout[i - 1], "#a5468ad9", 0.65,
                            MoodHistory.comment[i], imageS[i - 1], 0.565);
                    break;
                case 3:
                    this.setHistoryView(context, rLayout[i - 1], "#ff9b9b9b", 0.475,
                            MoodHistory.comment[i], imageS[i - 1], 0.39);
                    break;
                case 4:
                    this.setHistoryView(context, rLayout[i - 1], "#ffde3c50", 0.3,
                            MoodHistory.comment[i], imageS[i - 1], 0.215);
                    break;
                default:
                    this.setHistoryView(context, rLayout[i - 1], "#ffffffff", 1,
                            null, 0, 0);
                    TextView noMood = new TextView(context);
                    noMood.setText(noMoodText);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                            .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(screenWidth / 4, screenHeight / 20, 0, 0);
                    RelativeLayout rlDay = historyView.findViewById(rLayout[i - 1]);
                    rlDay.addView(noMood, params);
            }
        }
        return historyView;
    }

    public void setHistoryView(Context context, int rLayoutDay, String color, double moodWidth,
                               String comment, int imageS, double smsWidht) {

        historyView.findViewById(rLayoutDay).setBackgroundColor(Color.parseColor(color));
        ViewGroup.LayoutParams params = historyView.findViewById(rLayoutDay).getLayoutParams();
        params.width = (int) (screenWidth * (moodWidth));
        params.height = screenHeight / 7;
        historyView.findViewById(rLayoutDay).setLayoutParams(params);

        if (comment != null) {
            RelativeLayout rlButton = historyView.findViewById(rLayoutDay);
            rlButton.setOnClickListener(showCommentListener);
            this.setImageSMS(context, rLayoutDay, color, imageS, smsWidht);
            ImageButton imageButtonSMS = historyView.findViewById(imageS);
            imageButtonSMS.setOnClickListener(shareBySMSListener);
        }
    }


    public void getScreenWidthHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = (int) (displayMetrics.heightPixels * 0.887);
        //double layoutDecorations = screenHeight * 0.1678; // so 0.832
    }

    public void setImageSMS(Context context, int rLayoutDay, String color,
                            int imageS, double smsWidht) {
        ImageButton imageSMS = new ImageButton(context);
        imageSMS.setBackgroundColor(Color.parseColor(color));
        imageSMS.setImageResource(R.drawable.ic_comment_black_48px);
        imageSMS.setId(imageS);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int) (screenWidth * smsWidht), screenHeight / 18,
                0, screenHeight / 18);//right :screenWidth / 50
        RelativeLayout rlDay = historyView.findViewById(rLayoutDay);
        rlDay.addView(imageSMS, params);
    }

    public View.OnClickListener showCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 7; i > 0; i--) {
                if (v.getId() == rLayout[i - 1]) {
                    RelativeLayout rlComment = new RelativeLayout(savedContext);
                    rlComment.setBackgroundColor(Color.parseColor("#42000000"));
                    rlComment.setGravity(RelativeLayout.ALIGN_PARENT_BOTTOM); // Don't function
                    MoodHistoryView reCreate = new MoodHistoryView();
                    reCreate.onCreateHistoryView(savedContext);
                    TextView showComment = new TextView(savedContext);
                    showComment.setText(MoodHistory.comment[i]);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.
                            LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(screenWidth / 100, 0,
                            screenWidth / 100, screenHeight / 100);
                    rlComment.addView(showComment, params);
                    RelativeLayout rlDay = historyView.findViewById(R.id.Day1);
                    rlDay.addView(rlComment, params);
                    //LinearLayout llDay = historyView.findViewById(R.id.History); // Not print...
                    //llDay.addView(rlComment, params);
                    setContentView(historyView);
                }
            }
        }
    };

    public View.OnClickListener shareBySMSListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 7; i > 0; i--) {
                if (v.getId() == imageS[i - 1]) {
                    //Uri sms = Uri.parse("tel:1234567891");
                    Uri sms = Uri.parse("sms:");
                    Intent intent = new Intent(Intent.ACTION_VIEW, sms);
                    //intent.setType("vnd.android-dir/mms-sms");
                    //intent.putExtra("address", "");
                    intent.putExtra("body", MoodHistory.comment[i]);
                    startActivity(intent);
                    //SmsManager sms = SmsManager.getDefault();
                    //sms.sendTextMessage("+0290874890", null, "slt",
                    //null, null);
                }
            }
        }
    };
}

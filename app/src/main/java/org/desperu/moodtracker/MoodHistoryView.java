package org.desperu.moodtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.onCreateHistoryView());
    }

    public View onCreateHistoryView() {

        historyView = LayoutInflater.from(this).inflate(R.layout.history_layout, null);

        MoodHistory getTabs = new MoodHistory();
        getTabs.manageHistory(this, false);

        for (int i = 7; i > 0; i--) {
            switch (MoodHistory.mood[i]) {
                case 0:
                    this.setHistoryView(rLayout[i - 1], "#fff9ec4f", 1,
                            MoodHistory.comment[i], imageS[i - 1], 0.915);
                    break;
                case 1:
                    this.setHistoryView(rLayout[i - 1], "#ffb8e986", 0.825,
                            MoodHistory.comment[i], imageS[i - 1], 0.74);
                    break;
                case 2:
                    this.setHistoryView(rLayout[i - 1], "#a5468ad9", 0.65,
                            MoodHistory.comment[i], imageS[i - 1], 0.565);
                    break;
                case 3:
                    this.setHistoryView(rLayout[i - 1], "#ff9b9b9b", 0.475,
                            MoodHistory.comment[i], imageS[i - 1], 0.39);
                    break;
                case 4:
                    this.setHistoryView(rLayout[i - 1], "#ffde3c50", 0.3,
                            MoodHistory.comment[i], imageS[i - 1], 0.215);
                    break;
                default:
                    this.setHistoryView(rLayout[i - 1], "#ffffffff", 1,
                            null, 0, 0);
                    TextView noMood = new TextView(this);
                    noMood.setText(getString(R.string.noMood));
                    noMood.setTextSize(16);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(screenWidth / 3,
                            screenHeight / 18, 0, 0);
                    RelativeLayout rlDay = historyView.findViewById(rLayout[i - 1]);
                    rlDay.addView(noMood, params);
            }
        }
        return historyView;
    }

    public void setHistoryView(int rLayoutDay, String color, double moodWidth,
                               String comment, int imageS, double smsWidht) {

        this.getScreenWidthHeight();

        historyView.findViewById(rLayoutDay).setBackgroundColor(Color.parseColor(color));
        ViewGroup.LayoutParams params = historyView.findViewById(rLayoutDay).getLayoutParams();
        params.width = (int) (screenWidth * (moodWidth));
        params.height = screenHeight / 7;
        historyView.findViewById(rLayoutDay).setLayoutParams(params);

        if (comment != null) {
            RelativeLayout rlButton = historyView.findViewById(rLayoutDay);
            rlButton.setOnClickListener(showCommentListener);
            this.setImageSMS(rLayoutDay, color, imageS, smsWidht);
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

    public void setImageSMS(int rLayoutDay, String color, int imageS, double smsWidht) {
        ImageButton imageSMS = new ImageButton(this);
        imageSMS.setBackgroundColor(Color.parseColor(color));
        imageSMS.setImageResource(R.drawable.ic_comment_black_48px);
        imageSMS.setId(imageS);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    AlertDialog.Builder showComment = new AlertDialog.
                            Builder(MoodHistoryView.this, R.style.ShowCommentDialog);
                    AlertDialog dialog = showComment.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.gravity = Gravity.BOTTOM;
                    dialog.setMessage(MoodHistory.comment[i]);
                    //showComment.show();
                    dialog.show();
                }
            }
        }
    };

    public View.OnClickListener shareBySMSListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO : Dialog box for ask share media?
            for (int i = 7; i > 0; i--) {
                if (v.getId() == imageS[i - 1]) {
                    String day = null;
                    switch (i - 1) {
                        case 0: day = "There's one day"; break;
                        case 1: day = "There's two day"; break;
                        case 2: day = "There's three day"; break;
                        case 3: day = "There's for day"; break;
                        case 4: day = "There's five day"; break;
                        case 5: day = "There's six day"; break;
                        case 6: day = "There's one week"; break;
                        default: day = "There's a few day";
                    }
                    // TODO : get the mood to share it
                    String moodDay = null;
                    switch (MoodHistory.mood[i]) {
                        case 0: moodDay = ", i was super happy because : "; break;
                        case 1: moodDay = ", i was happy because : "; break;
                        case 2: moodDay = ", i was normal because : "; break;
                        case 3: moodDay = ", i was disappointed because : "; break;
                        case 4: moodDay = ", i was sad because : "; break;
                        default: moodDay = " : ";
                    }

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, day + moodDay + MoodHistory.comment[i]);
                    //share.setType("image/*");
                    //String mypath = "sdcard/Pictures/Screenshots/Screenshot_20190316-084054.png";
                    //share.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///"+mypath));
                    startActivity(Intent.createChooser(share, "Share Your Mood"));
                }
            }
        }
    };
}

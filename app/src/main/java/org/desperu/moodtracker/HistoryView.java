package org.desperu.moodtracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryView extends AppCompatActivity {

    View historyView = null; // LinearLayout?
    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.onCreateHistoryView(getBaseContext()));
    }

    public View onCreateHistoryView(Context context) { // Can getBaseContext??

        historyView = LayoutInflater.from(this).inflate(R.layout.history_layout, null);

        History getTabs = new History();
        getTabs.manageHistory(context, false);

        int[] rLayout = {R.id.Day1, R.id.Day2, R.id.Day3,
                R.id.Day4, R.id.Day5, R.id.Day6, R.id.Day7};

        for (int i = 7; i > 0; i--) {
            switch (History.mood[i]) {
                case 0:
                    this.setHistoryView(rLayout[i - 1], "#fff9ec4f", 1);
                    break;
                case 1:
                    this.setHistoryView(rLayout[i - 1], "#ffb8e986", 0.8);
                    break;
                case 2:
                    this.setHistoryView(rLayout[i - 1], "#a5468ad9", 0.6);
                    break;
                case 3:
                    this.setHistoryView(rLayout[i - 1], "#ff9b9b9b", 0.4);
                    break;
                case 4:
                    this.setHistoryView(rLayout[i - 1], "#ffde3c50", 0.2);
                    // too little for the textView
                    break;
                default:
                    this.setHistoryView(rLayout[i - 1], "#ffffffff", 1);
                    TextView noMood = new TextView(this);
                    noMood.setText(getString(R.string.noMood));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(screenWidth / 4, screenHeight / 20, 0, 0);
                    RelativeLayout rl = historyView.findViewById(rLayout[i - 1]);
                    rl.addView(noMood, params);
            }
        }
        return historyView;
    }

    public void setHistoryView(int rLayoutDay, String color, double moodWidth) {

        this.getScreenWidthHeight();

        historyView.findViewById(rLayoutDay).setBackgroundColor(Color.parseColor(color));
        ViewGroup.LayoutParams params = historyView.findViewById(rLayoutDay).getLayoutParams();
        params.width = (int) (screenWidth * (moodWidth));
        params.height = screenHeight / 7;
        historyView.findViewById(rLayoutDay).setLayoutParams(params);
        //historyView.requestLayout();
        //historyView.findViewById(rLayoutDay).requestLayout();
    }

    public void getScreenWidthHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = (int) (displayMetrics.heightPixels * 0.87);//0.88);
        //double layoutDecorations = screenHeight * 0.1678; // so 0.832
    }
}

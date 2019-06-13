package org.desperu.moodtracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryView extends AppCompatActivity {

    View historyView;
    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.onCreateHistoryView(getBaseContext()));
    }

    public View onCreateHistoryView(Context context) {

        historyView = LayoutInflater.from(this).inflate(R.layout.history_layout, null);

        History getTabs = new History();
        getTabs.manageHistory(context, false);

        int[] rLayout = {R.id.Day1, R.id.Day2, R.id.Day3, R.id.Day4, R.id.Day5, R.id.Day6,
                R.id.Day7};

        for (int i = 7; i > 0; i--) {
            switch (History.mood[i]) {
                case 0:
                    this.setHistoryView(rLayout[i - 1], "#fff9ec4f", 1);
                    break;
                case 1:
                    this.setHistoryView(rLayout[i - 1], "#ffb8e986", 4/5);
                    break;
                case 2:
                    this.setHistoryView(rLayout[i - 1], "#a5468ad9", 3/5);
                    break;
                case 3:
                    this.setHistoryView(rLayout[i - 1], "#ff9b9b9b", 2/5);
                    break;
                case 4:
                    this.setHistoryView(rLayout[i - 1], "#ffde3c50", 5);
                    break;
                default:
                    this.setHistoryView(rLayout[i - 1], "#ff000000", 5);
            }
        }
        return historyView;
    }

    public void setHistoryView(int rLayoutDay, String color, int moodWidth) {

        this.getScreenWidthHeight();

        historyView.findViewById(rLayoutDay).setBackgroundColor(Color.parseColor(color));
        ViewGroup.LayoutParams params = historyView.findViewById(rLayoutDay).getLayoutParams();
        params.width = screenWidth * moodWidth;
        params.height = screenHeight / 7;
        historyView.setLayoutParams(params);
    }

    public void getScreenWidthHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }
}

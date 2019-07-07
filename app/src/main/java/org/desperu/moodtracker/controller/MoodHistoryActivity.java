package org.desperu.moodtracker.controller;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.desperu.moodtracker.R;
import org.desperu.moodtracker.utils.MoodUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.desperu.moodtracker.MoodTools.Constant.*;
import static org.desperu.moodtracker.utils.MoodUtils.*;

public class MoodHistoryActivity extends AppCompatActivity {

    View historyView = null;
    MoodUtils moodUtils = new MoodUtils();
    private int screenWidth;
    private int screenHeight;
    private int[] mood = new int[numberOfDays];
    private long[] date = new long[numberOfDays];
    private String[] comment = new String[numberOfDays];
    private final int[] rLayout = {R.id.day1, R.id.day2, R.id.day3, R.id.day4,
            R.id.day5, R.id.day6, R.id.day7};
    private final int[] imageShow = {R.id.imageShow1, R.id.imageShow2, R.id.imageShow3,
            R.id.imageShow4, R.id.imageShow5, R.id.imageShow6, R.id.imageShow7};
    private final int[] imageShare = {R.id.imageShare1, R.id.imageShare2, R.id.imageShare3,
            R.id.imageShare4, R.id.imageShare5, R.id.imageShare6, R.id.imageShare7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getHistoryTabs();
        setContentView(this.onCreateHistoryView());
        // TODO : for test only
        Toast.makeText(this, "Test time 0 = " + moodUtils.compareDate(0) +
                " , " + date[6], Toast.LENGTH_LONG).show();
    }

    /**
     * Get history values, and put in tabs.
     */
    public void getHistoryTabs() {
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
    public View onCreateHistoryView() {
        historyView = LayoutInflater.from(this).inflate(R.layout.activity_mood_history,
                (ViewGroup)findViewById(R.id.history_root));

        // Switch between color and size for each history mood.
        for (int i = (numberOfDays - 1); i >= 0; i--) {
            switch (mood[i]) {
                case 0: this.setHistoryView(i, R.color.colorSuperHappy, 1);
                    break;
                case 1: this.setHistoryView(i, R.color.colorHappy, 0.825);
                    break;
                case 2: this.setHistoryView(i, R.color.colorNormal, 0.65);
                    break;
                case 3: this.setHistoryView(i, R.color.colorDisappointed, 0.475);
                    break;
                case 4: this.setHistoryView(i, R.color.colorSad, 0.3);
                    break;
                default: this.setHistoryView(i, R.color.colorNoMood, 1);
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
    public void setHistoryView(int i, int color, double moodWidth) {
        this.getScreenWidthHeight(); // Get screen size

        // Create RelativeLayout and set basic params.
        RelativeLayout rLayoutDay = new RelativeLayout(this);
        rLayoutDay.setBackgroundColor(getResources().getColor(color));
        rLayoutDay.setTop(screenHeight * ((numberOfDays - 1 - i) / numberOfDays));
        rLayoutDay.setId(rLayout[i]);

        // Create LayoutParams object to set more params.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (screenWidth * (moodWidth)), screenHeight / numberOfDays);

        // Add the RelativeLayout created to the root view and apply params.
        LinearLayout history = historyView.findViewById(R.id.history_root);
        // TODO : on test
        history.setVerticalScrollBarEnabled(true);
        history.canScrollVertically(50);
        history.addView(rLayoutDay, params);

        // If there's a comment for this mood, create image button to show comment
        // and another to share mood. // TODO : correct comment
        if (comment[i] != null && comment[i].length() > 0) {
            this.setCommentAndShareImages(i, moodWidth);
            ImageButton imageButtonComment = historyView.findViewById(imageShow[i]);
            imageButtonComment.setOnClickListener(showCommentListener);
            ImageButton imageButtonShare = historyView.findViewById(imageShare[i]);
            imageButtonShare.setOnClickListener(shareListener);
        }
        this.setMoodAgeText(i); // Create TextView mood age.
    }

    /**
     * Get real usable screen size for the view, Width and Height.
     */
    public void getScreenWidthHeight() { // TODO : to test getWindow().getHeight
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

        //Get full screen size with DisplayMetrics, minus statusBarHeight and actionBarHeight.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double correctError = 1.006; // To correct little screen size difference.
        screenWidth = displayMetrics.widthPixels;
        screenHeight = (int) ((displayMetrics.heightPixels - statusBarHeight- actionBarHeight ) *
                correctError);
    }

    /**
     * Set mood age text, for given mood.
     * @param i Number of the history mood.
     * @return Mood age text.
     */
    public String getMoodAgeText(int i) {
        int age = moodUtils.compareDate(date[i]);
        if (age == moodUtils.compareDate(0)) return getString(R.string.no_mood);
        else if (age == 1) return getString(R.string.text_yesterday);
        else if (age < 7) return getResources().getString(R.string.text_day, age);
        else if (age < 100) return getResources().getString(R.string.text_week, (int) (age / 7)); // TODO : one week??
        else if (age < 10000) return getResources().getString(R.string.text_month, (int) (age / 100));// TODO : to test it's perfect
        else if (age < 1000000) return getResources().getString(R.string.text_year, (int) (age / 10000));
        return getString(R.string.text_problem);
    }

    /**
     * Create TextView for each mood to show its age.
     * @param i Number of the history mood.
     */
    public void setMoodAgeText(int i) {
        // Create TextView and set params.
        TextView moodAgeText = new TextView(this);
        moodAgeText.setText(getMoodAgeText(i));
        moodAgeText.setTextSize(16); // It use sp // TODO : use dp TypedValue.COMPLEX_UNIT_SP, 16

        // Create LayoutParams object to set more params.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenWidth / 100, screenHeight / 100, 0, 0);

        // Add MoodAgeText with corresponding Mood RelativeLayout and apply params.
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(moodAgeText, params);
    }

    /**
     * Create Comment Image Button and Share Image Button.
     * @param i Number of the history mood.
     * @param moodWidth To position share and comment image button.
     */
    public void setCommentAndShareImages(int i, double moodWidth) {
        // Create ImageButtonComment and set basic params.
        ImageButton imageShowComment = new ImageButton(this);
        //imageShowComment.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        // TODO : for test
        imageShowComment.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        imageShowComment.setImageResource(R.drawable.ic_comment_black_48px);
        imageShowComment.setId(imageShow[i]);
        imageShowComment.setBaselineAlignBottom(true);

        // Create RelativeLayout.LayoutParams object to set specific params.
        RelativeLayout.LayoutParams paramsShow = new RelativeLayout.LayoutParams(
                screenWidth / 8, ViewGroup.LayoutParams.MATCH_PARENT);
        //paramsShow.setMargins((int) (screenWidth * (moodWidth - 0.125)), 0, // TODO : use Constant??
          //      0, 0);
        paramsShow.setMarginStart((int) (screenWidth * (moodWidth - 0.125)));

        // Create ImageButtonShare and set basic params.
        ImageButton imageShareMood = new ImageButton(this);
        //imageShareMood.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        imageShareMood.setBackgroundColor(getResources().getColor(R.color.colorBackgroundShowComment));
        imageShareMood.setImageResource(R.drawable.baseline_share_black_36);
        imageShareMood.setId(imageShare[i]);

        // Create RelativeLayout.LayoutParams object to set specific params.
        RelativeLayout.LayoutParams paramsShare = new RelativeLayout.LayoutParams(
                screenWidth / 8, ViewGroup.LayoutParams.MATCH_PARENT);
        //paramsShow.setMargins((int) (screenWidth * (moodWidth - 0.125)), 0, // TODO : use Constant??
        //      0, 0);
        paramsShare.setMarginStart((int) (screenWidth * (moodWidth - 0.25)));
        // TODO : add share image here
        // Add imageShowComment and imageShareMood with corresponding Mood RelativeLayout and apply params.
        RelativeLayout rlDay = historyView.findViewById(rLayout[i]);
        rlDay.addView(imageShowComment, paramsShow);
        rlDay.addView(imageShareMood, paramsShare);
    }

    /**
     * Listener to show corresponding mood comment with a toast message.
     */
    public View.OnClickListener showCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = (numberOfDays - 1); i >= 0; i--) {
                if (v.getId() == imageShow[i]) { // Get history mood number from this listener is called.
                    Toast customToast = Toast.makeText(MoodHistoryActivity.this,
                            comment[i], Toast.LENGTH_LONG);

                    // Get toast view and toast message to set custom params.
                    View toastView = customToast.getView(); // TODO : inflate this??
                    TextView toastMessage = toastView.findViewById(android.R.id.message);

                    // Set params and show toast.
                    toastMessage.setTextSize(18); // It use sp
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setPadding(screenWidth / 50, 0, screenWidth / 50, 0);
                    toastView.setBackgroundColor(getResources().getColor(
                            R.color.colorBackgroundShowComment));
                    toastView.setMinimumWidth(screenWidth - screenWidth / 20);
                    customToast.setGravity(Gravity.BOTTOM,0, screenHeight / 50);
                    customToast.show();
                }
            }
        }
    };

    /**
     * Listener to share mood age, mood and corresponding comment, with wanted user send method.
     */
    public View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = (numberOfDays - 1); i >= 0; i--) {
                if (v.getId() == imageShare[i]) { // Get history mood number from this listener is called.
                    String moodDay;

                    // Find the mood for this day.
                    switch (mood[i]) {
                        case 0: moodDay = getString(R.string.mood_day_super_happy); break;
                        case 1: moodDay = getString(R.string.mood_day_happy); break;
                        case 2: moodDay = getString(R.string.mood_day_normal); break;
                        case 3: moodDay = getString(R.string.mood_day_disappointed); break;
                        case 4: moodDay = getString(R.string.mood_day_sad); break;
                        default: moodDay = " : ";
                    }

                    // Send complete mood with intent.
                    /*Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, getMoodAgeText(i) + moodDay +
                            comment[i]);
                    startActivity(Intent.createChooser(share, getString(R.string.share_title)));*/


                    //ImageView imageView = (ImageView) MoodHistoryActivity.this.findViewById(R.id.imageShare1);
                    //View view = LayoutInflater.from(MoodHistoryActivity.this).inflate(R.layout.mood_fragment,
                      //      (ViewGroup) findViewById(R.id.fragment));
                    //ImageView imageView = getViewModelStore(View);
                    ImageView imageView = new ImageView(MoodHistoryActivity.this);
                    //imageView.setImageResource(R.drawable.smiley_happy);
                    //imageView.setBackgroundColor(getResources().getColor(R.color.colorHappy));
                    //Drawable drawable = imageView.getDrawable();

                    Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");

                    FileOutputStream out = null;
                    Uri bmpUri = null;
                    try {
                        out = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                        //bmp.setHeight(100);
                        //bmp.setWidth(100);
                        out.close();

                        // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.

                        //bmpUri = Uri.fromFile(file);
                    } catch (FileNotFoundException e) { e.printStackTrace();
                    } catch (IOException e) { e.printStackTrace(); }

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_TEXT, getMoodAgeText(i) + moodDay + comment[i]);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    //share.setType("image/*");
                    share.setType("text/*");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(share, getString(R.string.share_title)));
                }
            }
        }
    };
}
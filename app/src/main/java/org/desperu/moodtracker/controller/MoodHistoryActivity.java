package org.desperu.moodtracker.controller;

import android.content.Context;
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

import static org.desperu.moodtracker.MoodTools.Constant.*;
import static org.desperu.moodtracker.MoodTools.ScreenConstant.*;
import static org.desperu.moodtracker.MoodTools.Keys.*;

public class MoodHistoryActivity extends AppCompatActivity {

    View historyView = null;
    MoodUtils moodUtils = new MoodUtils();
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
                (ViewGroup)findViewById(R.id.history_linear));

        // Switch between color and size for each history mood.
        for (int i = (numberOfDays - 1); i >= 0; i--) {
            switch (mood[i]) {
                case 0: this.setHistoryView(i, R.color.colorSuperHappy, sHappyWidth);
                    break;
                case 1: this.setHistoryView(i, R.color.colorHappy, happyWidth);
                    break;
                case 2: this.setHistoryView(i, R.color.colorNormal, normalWidth);
                    break;
                case 3: this.setHistoryView(i, R.color.colorDisappointed, disappointedWidth);
                    break;
                case 4: this.setHistoryView(i, R.color.colorSad, sadWidth);
                    break;
                default: this.setHistoryView(i, R.color.colorNoMood, noMoodWidth);
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

            // Change left margin depending of screen orientation.
            double imageLeftMargin = imageLeftMarginPortrait;
            if (landscape) imageLeftMargin = imageLeftMarginLandscape;

            // Create show comment button.
            this.onCreateImageButton(i, (int) (screenWidth * (moodWidth - imageLeftMargin)),
                    R.drawable.ic_comment_black_48px, showCommentListener, idShow);

            // Create share button.
            this.onCreateImageButton(i, (int) (screenWidth * (moodWidth - imageLeftMargin * 2)),
                    R.drawable.baseline_share_black_24, shareListener, idShare);
        }
        this.onCreateMoodAgeText(i);// Create TextView for mood age, and set params.
    }

    /**
     * Get real usable screen size for the view, Width and Height.
     */
    public void getScreenWidthHeight() {
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
        if (this.getResources().getConfiguration().orientation == 2) landscape = true; // Landscape screen.

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
     * Set mood age text, for given mood.
     * @param i Number of the history mood.
     * @return Mood age text.
     */
    public String getMoodAgeText(int i) {
        int age = moodUtils.compareDate(date[i]);
        if (age == moodUtils.compareDate(0)) return getString(R.string.no_mood);
        else if (age == 1) return getString(R.string.text_yesterday);
        else if (age < 7) return getResources().getString(R.string.text_day, age);
        else if (age < 31) return getResources().getString(R.string.text_week, (int) (age / 7));
        else if (age < 1200) return getResources().getString(R.string.text_month, (int) (age / 100));
        else if (age < 1000000) return getResources().getString(R.string.text_year, (int) (age / 10000));
        return getString(R.string.text_problem);
    }

    /**
     * Create mood age text, for given mood.
     * @param i Number of the history mood.
     */
    public void onCreateMoodAgeText(int i) {
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
     * Create Comment Image Button and Share Image Button, for given mood.
     * @param i Number of the history mood.
     * @param left Margin left, to position share and comment image button.
     * @param drawable To set corresponding image.
     * @param listener To set corresponding listener.
     * @param idTab To save create resource id.
     */
    public void onCreateImageButton(int i, int left, int drawable, View.OnClickListener listener,
                                    int[] idTab) {
        int top = 0;
        // Create ImageButton and set basic params.
        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageResource(drawable);
        imageButton.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        imageButton.setOnClickListener(listener);
        imageButton.setId(View.generateViewId());
        idTab[i] = imageButton.getId();
        // For sad mood, push a little to bottom the imagesButtons
        // for they don't superpose with mood age text.
        if (mood[i] == 4) top = screenHeight / (numberInScreen * 4);

        // Create LayoutParams object to set more params.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                screenWidth / imageWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(left, top, 0, 0);

        // Add childView with corresponding Mood RelativeLayout and apply params.
        RelativeLayout rlDay = historyView.findViewById(idRl[i]);
        rlDay.addView(imageButton, params);
    }

    /**
     * Listener to show corresponding mood comment with a toast message.
     */
    public View.OnClickListener showCommentListener = new View.OnClickListener() {
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
    public View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = (numberOfDays - 1); i >= 0; i--) {
                if (v.getId() == idShare[i]) { // Get history mood number from this listener is called.
                    String moodDay = MoodHistoryActivity.this.moodShareText(
                            MoodHistoryActivity.this, mood[i], -1);

                    Intent share = MoodHistoryActivity.this.prepareShareIntent(
                            MoodHistoryActivity.this, getMoodAgeText(i) + moodDay +
                                    getString(R.string.with_comment) + comment[i]);

                    startActivity(Intent.createChooser(share, getString(R.string.share_title)));
                }
            }
        }
    };

    // TODO : create intent class!!!!!!!!
    public String moodShareText(Context context, int position, int time) { // TODO for color and smiley
        String moodDay;
        String presentOrPast = "";
        if (time == -1) presentOrPast = context.getString(R.string.past);
        else if (time == 0) presentOrPast = context.getString(R.string.present);
        // Find the mood for this day, and return correspond text.
        switch (position) {
            case 0: moodDay = context.getResources().getString(R.string.mood_day_super_happy, presentOrPast); break;
            case 1: moodDay = context.getResources().getString(R.string.mood_day_happy, presentOrPast); break;
            case 2: moodDay = context.getResources().getString(R.string.mood_day_normal, presentOrPast); break;
            case 3: moodDay = context.getResources().getString(R.string.mood_day_disappointed, presentOrPast); break;
            case 4: moodDay = context.getResources().getString(R.string.mood_day_sad, presentOrPast); break;
            default: moodDay = " : ";
        }
        return  moodDay;
    }

    public Intent prepareShareIntent (Context context, String shareText) {
        /*View view = LayoutInflater.from(context).inflate(R.layout.mood_fragment, null);
                //(ViewGroup) findViewById(R.id.fragment));
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.smiley_happy);

        //imageView.setImageURI(Uri uri);
        imageView.setMaxWidth(200);
        imageView.setMaxHeight(250);// TODO on test
        Drawable drawable = imageView.getDrawable();

        Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpeg");

        FileOutputStream out = null;
        Uri bmpUri = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                bmpUri = FileProvider.getUriForFile(context, "com.codepath.fileprovider", file);
            else bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, shareText);
        share.setType("text/plain");
        /*share.putExtra(Intent.EXTRA_STREAM, bmpUri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
        return share;
    }
}
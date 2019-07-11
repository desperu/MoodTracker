package org.desperu.moodtracker.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.desperu.moodtracker.R;

import static org.desperu.moodtracker.MoodTools.Constant.*;

public class MoodActivity extends AppCompatActivity implements View.OnTouchListener{

    View moodView = null;

    private int position = 1;
    private float dy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moodView = LayoutInflater.from(this).inflate(R.layout.mood_view,
                (ViewGroup) findViewById(R.id.fragment_new));
        setContentView(moodView);
        moodView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Get XY values at start and end touch screen, to detect slide.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dy = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Get size and direction of touch screen.
                float y = event.getRawY() - dy;
                if (y < -minSlide && position > 0) { // Slide to bottom.
                    position -= 1;
                    setContentView(onCreateView(position));
                    Toast.makeText(this, "onTouch minus" + position, Toast.LENGTH_SHORT).show();
                } else if (y > minSlide && position < (numberOfPage - 1)) { // Slide to top.
                    position += 1;
                    setContentView(onCreateView(position));
                    Toast.makeText(this, "onTouch plus" + position, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                return false;
        }
        // True if the listener has consumed the event.
        return true;
    }

    /**
     * Set mood view with color and smiley given.
     * @param color Background color to show.
     * @param drawable Smiley to show.
     */
    public void setMoodFragment(int color, int drawable) {
        moodView.findViewById(R.id.fragment_new).setBackgroundColor(getResources().getColor(color));
        ImageView superHappy = moodView.findViewById(R.id.mood_image_new);
        superHappy.setImageResource(drawable);
    }

    /**
     * Switch between color and smiley depending of given position.
     * @param position Mood number to select corresponding elements.
     * @return The view created.
     */
    public View onCreateView(int position) {
        switch (position) {
            case 0: this.setMoodFragment(R.color.colorSuperHappy, R.drawable.smiley_super_happy); break;
            case 1: this.setMoodFragment(R.color.colorHappy, R.drawable.smiley_happy); break;
            case 2: this.setMoodFragment(R.color.colorNormal, R.drawable.smiley_normal); break;
            case 3: this.setMoodFragment(R.color.colorDisappointed, R.drawable.smiley_disappointed); break;
            case 4: this.setMoodFragment(R.color.colorSad, R.drawable.smiley_sad); break;
            default: this.setMoodFragment(R.color.colorNormal, R.drawable.smiley_normal);
        }
        return moodView;
    }
}

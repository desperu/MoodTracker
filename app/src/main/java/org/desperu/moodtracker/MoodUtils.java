package org.desperu.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

class MoodUtils {

    private static final String moodDayFile = "MoodDay";
    private static final String currentMood = "CurrentMood";
    private static final String currentDate = "CurrentDate";
    private static final String currentComment = "CurrentComment";
    private static final String moodHistoryFile = "MoodHistory";
    private SharedPreferences sharedPreferences;

    // TODO : don't use static, use method to get tab
    // TODO : use string for all Toast
    static int[] mood = new int[7];
    static long[] date = new long[7];
    static String[] comment = new String[7];

    /**
     * Save the current mood selected
     * @param moodNum the number of the selected mood view
     * @param context get context from super activity
     */
    void saveCurrentMood(Context context, int moodNum, String comment) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(currentMood, moodNum);
        editor.putLong(currentDate, getTime());
        editor.putString(currentComment, comment);
        editor.apply();
        // TODO : Correct it or for test only
        Toast.makeText(context, "Current Mood Saved! For : " + moodNum + ", Date " + getTime()
                , Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "ConvertDate : "+ convertDate(getTime()), Toast.LENGTH_LONG).show();
    }

    /**
     * Check if there was a mood saved and print it
     * @param context The base context from the method is call
     * @return The number of the mood view
     */
    int getLastMood(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        if (sharedPreferences.contains(currentMood)) {
            int lastMood = sharedPreferences.getInt(currentMood, -1);
            Toast.makeText(context, "Last Mood selected today : " + lastMood,
                    Toast.LENGTH_SHORT).show();
            return lastMood;
        } else {
            Toast.makeText(context, "Hi! What's your mood today??", Toast.LENGTH_SHORT).show();
            // TODO : Useless, defValue give the same...only for the Toast??
            return -1;
        }
    }

    /**
     * Get the comment saved for the current day
     * @param context The base context from the method is called
     * @return Return the comment, null if there isn't
     */
    String getLastComment(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        return sharedPreferences.getString(currentComment, null);
    }

    /**
     * Delete all mood saved
     * @param context The base context from the method is called
     */
    void deleteAllMoods(Context context) {
        sharedPreferences = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }

    // TODO : Delete
    /*public int getDate() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.YEAR) * 10000;
        date += (calendar.get(Calendar.MONTH) + 1) * 100;
        date += calendar.get(Calendar.DATE);
        return date;
    }*/

    private long getTime() {
        return System.currentTimeMillis();
    }

    //long convertDate(long date) {
    private int convertDate(long currentTime) {
        // we start in 01/01/1970, first leap year february 1972
        /*Instant firstInstant = null;
        ZoneId zoneId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            firstInstant = Instant.ofEpochMilli(System.currentTimeMillis());
            zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt1 = ZonedDateTime.ofInstant( firstInstant, zoneId );
            System.out.println(firstInstant.toString());
            Comparator<ZonedDateTime> comparator = Comparator.comparing(new Function<ZonedDateTime,
                    Comparable>() {
                @Override
                public Comparable apply(ZonedDateTime zdt) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        return zdt.truncatedTo(ChronoUnit.SECONDS);
                    }
                    return
                }
            });
        }*/

        currentTime += TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
        long oneDay = (24 * 60 * 60 * 1000);
        int i = 0;
        int year = 1970;
        int[] yearDays = {365, 365, 366, 365}; // we start in 01/01/1970, first leap year february 1972
        boolean leapYear = false;
        int month = 1; // first month of the year

        while (currentTime > (365 * oneDay)) {
            leapYear = false;
            if (i == 2) leapYear = true;
            currentTime -= (yearDays[i] * oneDay);
            year++;
            i++;
            if (i == 4) i = 0;
        }

        // division by one day and find the day?
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (i = 0; currentTime > (monthDays[i + 1] * oneDay); i++) {
            if (i == 1 && leapYear) monthDays[1] = 29;
            currentTime -= (monthDays[i] * oneDay);
            month++;
        }

        int day = (int) (currentTime / oneDay) + 1;
        currentTime = currentTime % oneDay;

        int hour = (int) (currentTime / (60 * 60 * 1000));
        currentTime = currentTime % (60 * 60 * 1000);

        int minutes = (int) (currentTime / (60 * 1000));
        /*currentTime = currentTime % (60 * 1000);

        int seconds = (int) (currentTime / 1000) + 1;
        currentTime = currentTime % 1000;*/

        return (year * 10000) + (month * 100) + day;
        // TODO : For test only
        //return (month * 1000000) + (day * 10000) + (hour * 100) + minutes;
    }

    long checkSavedDate(Context context) {
        sharedPreferences = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        long savedDate = sharedPreferences.getLong(currentDate, 0);
        if (savedDate == 0) return savedDate;
        //return (getTime() - savedDate);
        return convertDate(getTime()) - convertDate(savedDate);
    }

    // TODO : Do for mood, date and comment
    /*int getMood(Context context, int nb) {
        SharedPreferences historyFile = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        return historyFile.getInt("Mood" + (nb + 1), -1);
    }*/

    // TODO : create class SharedPreferences for simplify access to pref ???
    // TODO : simplify -> create method to serialize action read clear, read only, write
    // TODO : crete method for get Tabs, and not use static
    void manageHistory(Context context, boolean newDate) {
        SharedPreferences dayFile = context.getSharedPreferences(moodDayFile, MODE_PRIVATE);
        SharedPreferences.Editor editorDay = dayFile.edit();
        SharedPreferences historyFile = context.getSharedPreferences(moodHistoryFile, MODE_PRIVATE);
        SharedPreferences.Editor editorHistory = historyFile.edit();

        for (int i = 6; i >= 0; i--) {
            mood[i] = historyFile.getInt("Mood" + (i + 1), -1);
            date[i] = historyFile.getLong("Date" + (i + 1), 0);
            comment[i] = historyFile.getString("Comment" + (i + 1), null);

            if (i != 6 && newDate) {
                mood[i + 1] = mood[i]; editorHistory.putInt("Mood" + (i + 2), mood[i]).apply();
                date[i + 1] = date[i]; editorHistory.putLong("Date" + (i + 2), date[i]).apply();
                comment[i + 1] = comment[i]; editorHistory.putString("Comment" + (i + 2),
                        comment[i]).apply();
            }

            if (i == 0 && newDate) {
                mood[i] = dayFile.getInt(currentMood, -1);
                date[i] = dayFile.getLong(currentDate, 0);
                comment[i] = dayFile.getString(currentComment, null);
                editorDay.remove(currentMood).apply();
                editorDay.remove(currentDate).apply();
                editorDay.remove(currentComment).apply();
                editorHistory.putInt("Mood" + (i + 1), mood[i]).apply();
                editorHistory.putLong("Date" + (i + 1), date[i]).apply();
                editorHistory.putString("Comment" + (i + 1), comment[i]).apply();
            }
        }
    }
}

package com.bjorkelid.delays;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


// Hämta all data jag behöver för att visa vyn

// Plocka ut det jag behöver för att visa veckan

// Gruppera och sortera dagarna på format: mån - sön med start [7 dagar sedan] och slut [idag]

// Plotta på graf


public class StatsActivity extends Fragment {

    public static final String TAG = StatsActivity.class.getSimpleName();

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_stats, container, false);

        fetchDelayDataFromParse();
        //fetchWeekStats();
        //fetchMonthStats();

        return rootView;
    }

    @Override
    public void onStart() {
        Log.e("FRAG", "onStart called from Stats");
        super.onStart();
    }

    public void fetchDelayDataFromParse() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR); // 252

        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date timeSpan = calendar.getTime();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Delay");
        query.whereGreaterThan("createdAt", timeSpan);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> delays, ParseException e) {

                if (e == null) {
                    // Success!
                    Log.d(TAG, "Retrieved " + delays.size() + " delays");
                    drawWeekStats(delays);
                    drawMonthStats(delays);
                } else {
                    // Error!
                    Log.d(TAG, "Error: " + e.getMessage());
                }

            }
        });
    }

    public void drawWeekStats(List<ParseObject> delays) {
        int[] groupedDelayValues = new int[7];
        String[] dayNames = new String[7];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (int i = 0; i < 7; i++) {
            dayNames[6 - i] = calendar.getDisplayName((Calendar.DAY_OF_WEEK), Calendar.SHORT, Locale.getDefault());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }


        //calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekAgo = calendar.getTime();
        //Date oneWeekAgo = new Date(tmp.getYear(), tmp.getMonth(), tmp.getDate());

        for (int i = 0; i < delays.size(); i++) {
            ParseObject delayObj = delays.get(i);

            // Plocka ut det jag behöver för att visa veckan
            if (delayObj.getCreatedAt().getTime() > oneWeekAgo.getTime()) {

                calendar.setTime(delayObj.getCreatedAt());
                int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
                if(day > 0) {
                    // Gruppera och sortera dagarna på format: mån - sön med start [7 dagar sedan] och slut [idag]
                    groupedDelayValues[day] += delayObj.getInt("delay");
                }
            }
        }

        // Plotta på graf

        ValueLineChart weekChart = (ValueLineChart) rootView.findViewById(R.id.cubiclinechartWeek);

        // Week
        ValueLineSeries weekSeries = new ValueLineSeries();
        weekSeries.setColor(0xccb7c0c7);

        weekSeries.addPoint(new ValueLinePoint("", 0));

        for (int i = 0; i < groupedDelayValues.length; i++) {
            weekSeries.addPoint(new ValueLinePoint(dayNames[i], groupedDelayValues[i]));
        }

        weekSeries.addPoint(new ValueLinePoint("", 0));


        weekChart.addSeries(weekSeries);
        weekChart.startAnimation();

    }


    public void drawMonthStats(List<ParseObject> delays) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int[] groupedDelayValues = new int[30];
        String[] dayNames = new String[30];

        for (int i = 0; i < 30; i++) {
            dayNames[i] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH)+1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date oneWeekAgo = calendar.getTime();


        for (int i = 0; i < delays.size(); i++) {
            ParseObject delayObj = delays.get(i);

            if (delayObj.getCreatedAt().getTime() > oneWeekAgo.getTime()) {

                calendar.setTime(delayObj.getCreatedAt());
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                groupedDelayValues[day] += delayObj.getInt("delay");
            }
        }

        // Plotta på graf

        ValueLineChart monthChart = (ValueLineChart) rootView.findViewById(R.id.cubiclinechartMonth);

        // Week
        ValueLineSeries monthSeries = new ValueLineSeries();
        monthSeries.setColor(0xccb7c0c7);

        monthSeries.addPoint(new ValueLinePoint("", 0));

        for (int i = 0; i < groupedDelayValues.length; i++) {
            if(groupedDelayValues[i] > 0) {
                monthSeries.addPoint(new ValueLinePoint(dayNames[i], groupedDelayValues[i]));
            }
        }

        monthSeries.addPoint(new ValueLinePoint("", 0));


        monthChart.addSeries(monthSeries);
        monthChart.startAnimation();

    }
}





    /* ********************** */
    /* ********************** */
    /* ********************** */
    /* ********************** */
    /* ********************** */
    /* ********************** */

    /*

    public void fetchWeekStats() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final int currentDay = calendar.get(Calendar.DAY_OF_YEAR); // 252

        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekAgo = calendar.getTime();



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Delay");
        query.whereGreaterThan("createdAt", oneWeekAgo);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> delays, ParseException e) {

                HashMap<String, Integer> groupedDelaysWeekMap = new HashMap<String, Integer>();

                groupedDelaysWeekMap.put("Mon", 0);
                groupedDelaysWeekMap.put("Tue", 0);
                groupedDelaysWeekMap.put("Wed", 0);
                groupedDelaysWeekMap.put("Thu", 0);
                groupedDelaysWeekMap.put("Fri", 0);
                groupedDelaysWeekMap.put("Sat", 0);
                groupedDelaysWeekMap.put("Sun", 0);

                if (e == null) {
                    Log.d("score", "Retrieved " + delays.size() + " delays");
                    ParseObject delay;
                    for (int i = 0; i < delays.size(); i++) {
                        delay = (ParseObject) delays.get(i);
                        calendar.setTime(delay.getCreatedAt());
                        String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

                        groupedDelaysWeekMap.put(dayLongName, groupedDelaysWeekMap.get(dayLongName) + delay.getInt("delay"));
                    }


                    Log.d(TAG, "KLAR");
                    drawWeekStats(groupedDelaysWeekMap);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    public void drawWeekStats(HashMap<String, Integer> groupedDelaysWeekMap) {

        ValueLineChart weekChart = (ValueLineChart) findViewById(R.id.cubiclinechartWeek);

        // Week
        ValueLineSeries weekSeries = new ValueLineSeries();
        weekSeries.setColor(0xccb7c0c7);


        weekSeries.addPoint(new ValueLinePoint("", 0));

        weekSeries.addPoint(new ValueLinePoint("Mon", groupedDelaysWeekMap.get("Mon")));
        weekSeries.addPoint(new ValueLinePoint("Tue", groupedDelaysWeekMap.get("Tue")));
        weekSeries.addPoint(new ValueLinePoint("Wed", groupedDelaysWeekMap.get("Wed")));
        weekSeries.addPoint(new ValueLinePoint("Thu", groupedDelaysWeekMap.get("Thu")));
        weekSeries.addPoint(new ValueLinePoint("Fri", groupedDelaysWeekMap.get("Fri")));
        weekSeries.addPoint(new ValueLinePoint("Sat", groupedDelaysWeekMap.get("Sat")));
        weekSeries.addPoint(new ValueLinePoint("Sun", groupedDelaysWeekMap.get("Sun")));

        weekSeries.addPoint(new ValueLinePoint("", 0));


        //delaysStatsTextView.setText(output);

        weekChart.addSeries(weekSeries);
        weekChart.startAnimation();


    }

    public void fetchMonthStats() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final int currentDay = calendar.get(Calendar.DAY_OF_YEAR); // 252

        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date oneMonthAgo = calendar.getTime();



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Delay");
        query.whereGreaterThan("createdAt", oneMonthAgo);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> delays, ParseException e) {

                HashMap<String, Integer> groupedDelaysMonthMap = new HashMap<String, Integer>();

                for(int i=0;i<30;i++) {
                    groupedDelaysMonthMap.put(String.valueOf(i), 0);
                }

                if (e == null) {
                    Log.d("score", "Retrieved " + delays.size() + " delays");
                    ParseObject delay;
                    for (int i = 0; i < delays.size(); i++) {
                        delay = (ParseObject) delays.get(i);
                        calendar.setTime(delay.getCreatedAt());
                        int dayNumber = calendar.get(Calendar.DAY_OF_MONTH);

                        groupedDelaysMonthMap.put(String.valueOf(dayNumber), groupedDelaysMonthMap.get(String.valueOf(dayNumber)) + delay.getInt("delay"));
                    }


                    Log.d(TAG, "KLAR");
                    drawMonthStats(groupedDelaysMonthMap);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    public void drawMonthStats(HashMap<String, Integer> groupedDelaysMonthMap) {

        ValueLineChart monthChart = (ValueLineChart) findViewById(R.id.cubiclinechartMonth);

        // Month
        ValueLineSeries monthSeries = new ValueLineSeries();
        monthSeries.setColor(0xccb7c0c7);

        for(String day : groupedDelaysMonthMap.keySet()) {
            String value = String.valueOf(groupedDelaysMonthMap.get(day));
            monthSeries.addPoint(new ValueLinePoint(value, groupedDelaysMonthMap.get(day)));
        }

        monthChart.addSeries(monthSeries);
        monthChart.startAnimation();


    }

}
*/

 /*groupedDelaysMonthMap.put("Jan", 0);
                groupedDelaysMonthMap.put("Feb", 0);
                groupedDelaysMonthMap.put("Mar", 0);
                groupedDelaysMonthMap.put("Apr", 0);
                groupedDelaysMonthMap.put("May", 0);
                groupedDelaysMonthMap.put("Jun", 0);
                groupedDelaysMonthMap.put("Jul", 0);
                groupedDelaysMonthMap.put("Aug", 0);
                groupedDelaysMonthMap.put("Sep", 0);
                groupedDelaysMonthMap.put("Oct", 0);
                groupedDelaysMonthMap.put("Nov", 0);
                groupedDelaysMonthMap.put("Dec", 0);*/
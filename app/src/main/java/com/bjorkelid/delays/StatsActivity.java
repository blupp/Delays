package com.bjorkelid.delays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.GetCallback;
import com.parse.ParseException;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class StatsActivity extends Activity {

    public static final String TAG = StatsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);



        fetchWeekStats();
        fetchMonthStats();
    }

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
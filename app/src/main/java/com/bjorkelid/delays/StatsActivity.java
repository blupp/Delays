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

import java.util.List;


public class StatsActivity extends Activity {

    public static final String TAG = StatsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Delay");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> delays, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + delays.size() + " delays");
                    drawStats(delays);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void drawStats(List delays) {

        TextView delaysStatsTextView = (TextView) findViewById(R.id.delaysStatsTextView);

        String output = "";
        ParseObject delay;
        int delayInMinutes = 0;

        for (int i=0; i<delays.size(); i++) {
            delay = (ParseObject) delays.get(i);
            delayInMinutes = delay.getInt("delay");
            output = output + Integer.toString(delayInMinutes) + "\n";
        }

        delaysStatsTextView.setText(output);

    }

}

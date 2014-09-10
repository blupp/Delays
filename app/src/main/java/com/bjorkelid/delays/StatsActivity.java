package com.bjorkelid.delays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.GetCallback;
import com.parse.ParseException;


public class StatsActivity extends Activity {

    public static final String TAG = StatsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Delays");
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                } else {
                    Log.d(TAG, "Failed to get data from Parse");
                }
            }
        });
    }

}

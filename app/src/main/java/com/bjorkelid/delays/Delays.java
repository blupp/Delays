package com.bjorkelid.delays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import java.text.ParseException;


public class Delays extends Activity {

    public static final String TAG = Delays.class.getSimpleName();
    public EditText minutesEditText;
    public Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delays);

        Parse.initialize(this, "N0cQqhZnV5l44nnsayfYuWnajuSDqOcISX7VtT45", "2nFu1jeKocMzUFXWcdsebMOa4QJC4o638VRUl5oU");
        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
        saveButton = (Button) findViewById(R.id.saveButton);


        View.OnClickListener minutesEditTextListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minutesEditText.setText("");
            }
        };

        minutesEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.d(TAG,"Action!");
                saveDelay();
                return false;
            }
        });

        View.OnClickListener saveButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDelay();
            }
        };

        minutesEditText.setOnClickListener(minutesEditTextListener);
        saveButton.setOnClickListener(saveButtonListener);

    }

    public void saveDelay() {
        String minutesDelayString = minutesEditText.getText().toString();
        int delay = 0;

        if(minutesDelayString.length() > 0) {
            delay = Integer.parseInt(minutesDelayString);
        }

        if(delay > 0) {
            Toast.makeText(Delays.this, "Delay saved!", Toast.LENGTH_SHORT).show();
            ParseObject delayObject = new ParseObject("Delay");
            delayObject.put("delay", delay);
            delayObject.saveInBackground();
            Log.d(TAG, "Logged " + delay + " minutes delay to Parse.com");
            minutesEditText.setText("");

            Intent showStats = new Intent(this, StatsActivity.class);
            startActivity(showStats);

        } else {
            Toast.makeText(Delays.this, "Ops! Missing delay", Toast.LENGTH_SHORT).show();
        }
    }
}

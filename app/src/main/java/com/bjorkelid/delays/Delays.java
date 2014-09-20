package com.bjorkelid.delays;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;


public class Delays extends Fragment {

    // TABS

    public static final String TAG = Delays.class.getSimpleName();
    public EditText minutesEditText;
    public Button saveButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_delays, container, false);

        Parse.initialize(getActivity(), "N0cQqhZnV5l44nnsayfYuWnajuSDqOcISX7VtT45", "2nFu1jeKocMzUFXWcdsebMOa4QJC4o638VRUl5oU");
        minutesEditText = (EditText) rootView.findViewById(R.id.minutesEditText);
        saveButton = (Button) rootView.findViewById(R.id.saveButton);


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

        return rootView;
    }

    @Override
    public void onStart() {
        Log.e("FRAG", "onStart called");
        super.onStart();
    }

    public void saveDelay() {
        String minutesDelayString = minutesEditText.getText().toString();
        int delay = 0;

        if(minutesDelayString.length() > 0) {
            delay = Integer.parseInt(minutesDelayString);
        }

        if(delay > 0) {
            Toast.makeText(getActivity(), "Delay saved!", Toast.LENGTH_SHORT).show();
            ParseObject delayObject = new ParseObject("Delay");
            delayObject.put("delay", delay);
            delayObject.saveInBackground();
            Log.d(TAG, "Logged " + delay + " minutes delay to Parse.com");
            minutesEditText.setText("");

            //Intent showStats = new Intent(this, StatsActivity.class);
            //startActivity(showStats);

            getActivity().getActionBar().setSelectedNavigationItem(1);


        } else {
            Toast.makeText(getActivity(), "Ops! Missing delay", Toast.LENGTH_SHORT).show();
        }
    }
}

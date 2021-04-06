package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS_KEY = "com.example.workouttimer.SHARED_PREFS";
    public static final String SECONDS_KEY = "com.example.workouttimer.SECONDS";
    public static final String WORKOUT_TYPE_KEY = "com.example.workouttimer.WORKOUT_TYPE";
    public static final String ISRUNNING_KEY = "com.example.workouttimer.ISRUNNING";

    private int seconds;
    private boolean isRunning;
    private Handler handler;

    private SharedPreferences sharedPreferences;

    private TextView timeSpentTextView;
    private TextView timerTextView;
    private EditText workoutTypeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        assignVariables();
        assignViews();
        checkSharedPreferences();
        displayTimer();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        seconds = savedInstanceState.getInt(SECONDS_KEY);
        displayTimer();
        if (savedInstanceState.getBoolean(ISRUNNING_KEY)) {
            onClickStart(null);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SECONDS_KEY, seconds);
        outState.putBoolean(ISRUNNING_KEY, isRunning);
    }

    private void checkSharedPreferences() {
        int savedSeconds = sharedPreferences.getInt(SECONDS_KEY, 0);
        int minutes = (savedSeconds % 3600) / 60;
        int secs = savedSeconds % 60;

        String workoutType = sharedPreferences.getString(WORKOUT_TYPE_KEY, "");
        if (workoutType.isEmpty()) {
            timeSpentTextView.setText(getString(R.string.time_spent_default));
        } else {
            timeSpentTextView.setText(getString(R.string.time_spent, minutes, secs, workoutType));
        }
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SECONDS_KEY, seconds);
        editor.putString(WORKOUT_TYPE_KEY, workoutTypeEditText.getText().toString());
        editor.apply();
    }

    private void assignVariables() {
        seconds = 0;
        handler = new Handler();
        isRunning = false;
    }

    private void assignViews() {
        timeSpentTextView = findViewById(R.id.timeSpentTextView);
        timerTextView = findViewById(R.id.timerTextView);
        workoutTypeEditText = findViewById(R.id.workoutTypeEditText);
    }

    private void setWorkoutTypeEditText(boolean enabled) {
        if (enabled) {
            workoutTypeEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            workoutTypeEditText.setFocusableInTouchMode(true);
            workoutTypeEditText.getBackground().setAlpha(255);
        } else {
            workoutTypeEditText.setInputType(InputType.TYPE_NULL);
            workoutTypeEditText.setFocusable(false);
            workoutTypeEditText.getBackground().setAlpha(0);
        }
    }

    private void displayTimer() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        timerTextView.setText(getString(R.string.timer, hours, minutes, secs));
    }

    public void onClickStart(View view) {
        if (!isRunning) {
            if (workoutTypeEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please type in the workout type.", Toast.LENGTH_SHORT).show();
                return;
            }
            displayTimer();
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000);
            isRunning = true;
            setWorkoutTypeEditText(false);
        }
    }

    public void onClickPause(View view) {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    public void onClickStop(View view) {
        onClickPause(view);
        setWorkoutTypeEditText(true);
        if (!workoutTypeEditText.getText().toString().isEmpty()) {
            saveSharedPreferences();
            checkSharedPreferences();
        }
        seconds = 0;
    }

    //https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            displayTimer();
            handler.postDelayed(this, 1000);
        }
    };
}
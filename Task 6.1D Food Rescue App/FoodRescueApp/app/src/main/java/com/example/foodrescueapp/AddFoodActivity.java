package com.example.foodrescueapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.foodrescueapp.data.FoodDatabase;
import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.model.FoodData;
import com.example.foodrescueapp.util.Authenticator;
import com.example.foodrescueapp.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFoodActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_CODE = 1;
    private UserDatabase userDb;
    private FoodDatabase foodDb;
    private SharedPreferences sharedPrefs;

    private long chosenDate = 0;
    private String chosenImgUri = "";

    private ImageButton addImageButton;
    private EditText addTitleEditText;
    private EditText addDescEditText;
    private CalendarView addCalendarView;
    private EditText addPickUpEditText;
    private EditText addQuantityEditText;
    private EditText addLocationEditText;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_CODE) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    addImageButton.setImageURI(selectedImageUri);
                    chosenImgUri = selectedImageUri.toString();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        userDb = new UserDatabase(this);
        foodDb = new FoodDatabase(this);
        sharedPrefs = getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, MODE_PRIVATE);

        assignViews();
        setCalendarAndTime();
    }

    private void assignViews() {
        addImageButton = findViewById(R.id.addImageButton);
        addTitleEditText = findViewById(R.id.addTitleEditText);
        addDescEditText = findViewById(R.id.addDescEditText);
        addCalendarView = findViewById(R.id.addCalendarView);
        addPickUpEditText = findViewById(R.id.addPickUpEditText);
        addQuantityEditText = findViewById(R.id.addQuantityEditText);
        addLocationEditText = findViewById(R.id.addLocationEditText);
    }

    private void setCalendarAndTime() {
        Calendar calendar = Calendar.getInstance();
        checkAndSetTime(calendar);

        int initialHour = calendar.get(Calendar.HOUR_OF_DAY);
        int initialMinute = calendar.get(Calendar.MINUTE);

        chosenDate = calendar.getTimeInMillis();
        addCalendarView.setMinDate(chosenDate);
        addPickUpEditText.setText(getString(R.string.time_placeholder, initialHour, initialMinute));

        addCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                chosenDate = c.getTimeInMillis();
            }
        });
    }

    private void checkAndSetTime(Calendar calendar) {
        if (calendar.get(Calendar.MINUTE) < 30) {
            calendar.set(Calendar.MINUTE, 30);
        } else {
            calendar.set(Calendar.MINUTE, 0);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
    }

    public void onAddImageClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_CODE);
    }

    public void onPickUpClick(View view) {
        String[] time = addPickUpEditText.getText().toString().split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                addPickUpEditText.setText(getString(R.string.time_placeholder, hourOfDay, minute));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public void onSaveClick(View view) {
        String title = addTitleEditText.getText().toString();
        String description = addDescEditText.getText().toString();
        String[] time = addPickUpEditText.getText().toString().split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        int quantity = Util.tryParseInt(addQuantityEditText.getText().toString(), 0);
        String location = addLocationEditText.getText().toString();

        Calendar chosenCalendar = Calendar.getInstance();
        chosenCalendar.setTimeInMillis(chosenDate);
        chosenCalendar.set(Calendar.HOUR_OF_DAY, hour);
        chosenCalendar.set(Calendar.MINUTE, minute);
        chosenCalendar.set(Calendar.SECOND, 0);

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title!", Toast.LENGTH_SHORT).show();
            return;
        } else if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description!", Toast.LENGTH_SHORT).show();
            return;
        } else if (chosenCalendar.compareTo(Calendar.getInstance()) <= 0) {
            Toast.makeText(this, "Time is in the past!", Toast.LENGTH_SHORT).show();
            return;
        } else if (quantity < 1) {
            Toast.makeText(this, "Quantity cannot be zero or below!", Toast.LENGTH_SHORT).show();
            return;
        } else if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a valid location!", Toast.LENGTH_SHORT).show();
            return;
        } else if (chosenImgUri.isEmpty()) {
            Toast.makeText(this, "Please enter a valid image!", Toast.LENGTH_SHORT).show();
            return;
        }

        // check if logged in
        Authenticator.checkAuthentication(this);

        String token = sharedPrefs.getString(Util.SPKey.LOGIN_TOKEN, "");
        String userEmail = userDb.fetchEmailFromToken(token);

        FoodData foodData = new FoodData(chosenImgUri, title, description,
                chosenCalendar.getTimeInMillis(), quantity, location, true, userEmail);

        long result = foodDb.insertFoodData(foodData);
        if (result < 0) {
            Toast.makeText(this, "An error has occurred! Please try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully added food to the database!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
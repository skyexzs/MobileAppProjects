package com.example.youtubeplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    private final String re = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|watch\\?v%3D|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

    private EditText urlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEditText = findViewById(R.id.urlEditText);
    }

    public void onPlayClick(View view) {
        String url = urlEditText.getText().toString();

        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String id = matcher.group();
            Intent youtubeIntent = new Intent(this, YoutubeActivity.class);
            youtubeIntent.putExtra(EXTRA_ID, id);
            startActivity(youtubeIntent);
        } else {
            Toast.makeText(this, "Please enter a valid YouTube URL.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView homeTV;
    private EditText homeET;
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        Toast.makeText(this, "OnCreate is called.", Toast.LENGTH_SHORT).show();
    }

    private void assignViews() {
        homeTV = findViewById(R.id.homeTV);
        homeET = findViewById(R.id.homeET);
        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(this::onBtnClick);
    }

    private void onBtnClick(View view) {
        if (homeET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Error! URL cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent wvIntent = new Intent(this, WebViewActivity.class);
        wvIntent.putExtra("url", homeET.getText().toString());
        startActivity(wvIntent);
    }
}
package com.example.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import com.example.foodrescueapp.util.HashGenerator;
import com.example.foodrescueapp.util.TokenGeneration;

public class TestActivity extends AppCompatActivity {
    private TextView longTextView;
    private TextView tokenTextView;
    private EditText testPasswordEditText;
    private TextView testHashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        longTextView = findViewById(R.id.longTextView);
        tokenTextView = findViewById(R.id.tokenTextView);
        testPasswordEditText = findViewById(R.id.testPasswordEditText);
        testHashTextView = findViewById(R.id.testHashTextView);
    }

    public void generateToken(View view) {
        Random random = new Random();
        String randInt = String.valueOf(random.nextInt());
        longTextView.setText(String.valueOf(randInt));
        tokenTextView.setText(String.valueOf(TokenGeneration.getToken(randInt)));
    }

    public void generateHash(View view) {
        testHashTextView.setText(HashGenerator.getSha256Hash(testPasswordEditText.getText().toString()));
    }
}
package com.example.quizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String NAME_KEY = "com.example.quizapp.NAME";
    public static final int QUIZ_ACTIVITY_REQUEST_CODE = 1;
    public static final int RESULT_ACTIVITY_REQUEST_CODE = 2;
    public static final int RESULT_QUIT = 3;

    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QUIZ_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK){
                showResultsActivity(data);
            }
        } else if (requestCode == RESULT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_QUIT) {
                finish();
            }
        }
    }

    private void assignViews() {
        nameEditText = findViewById(R.id.nameEditText);
    }

    public void createQuizActivity(View view) {
        if (nameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(NAME_KEY, nameEditText.getText().toString());
            intent.putExtra(QuizActivity.FILENAME_KEY, "questions.json");
            intent.putExtra(QuizActivity.AMOUNT_KEY, 10);
            startActivityForResult(intent, QUIZ_ACTIVITY_REQUEST_CODE);
        }
    }

    private void showResultsActivity(Intent data) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(NAME_KEY, nameEditText.getText().toString());
        intent.putExtra(QuizActivity.AMOUNT_KEY, data.getIntExtra(QuizActivity.AMOUNT_KEY, 0));
        intent.putExtra(QuizActivity.RESULTS_KEY, data.getIntExtra(QuizActivity.RESULTS_KEY, 0));
        startActivityForResult(intent, RESULT_ACTIVITY_REQUEST_CODE);
    }
}
package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView congratsTextView;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        assignViews();
        getIntentAndShowResults();
    }

    private void assignViews() {
        congratsTextView = findViewById(R.id.congratsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
    }

    private void getIntentAndShowResults() {
        Intent intent = getIntent();
        congratsTextView.setText(getString(R.string.congratulations, intent.getStringExtra(MainActivity.NAME_KEY)));
        scoreTextView.setText(getString(R.string.score, intent.getIntExtra(QuizActivity.RESULTS_KEY, 0), intent.getIntExtra(QuizActivity.AMOUNT_KEY, 0)));
    }

    public void startNewQuiz(View view) {
        finish();
    }

    public void quitGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(MainActivity.RESULT_QUIT, intent);
        finish();
    }
}
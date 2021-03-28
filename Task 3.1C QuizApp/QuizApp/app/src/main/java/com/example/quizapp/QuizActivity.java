package com.example.quizapp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    public static final String FILENAME_KEY = "com.example.quizapp.FILENAME";
    public static final String AMOUNT_KEY = "com.example.quizapp.AMOUNT";
    public static final String RESULTS_KEY = "com.example.quiz.app.RESULTS";

    private String name;
    private String fileName;
    private int amount;
    private ArrayList<Question> questions;
    private int progress;
    private int correctAnswers;
    private Answers currentAnswer;
    private boolean isSubmitted;

    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button nextButton;
    private TextView headerTextView;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private TextView questionDetailTextView;

    private HashMap<Answers, Button> answersButtonHashMap;

    enum Answers {
        ONE,
        TWO,
        THREE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        assignViews();
        addListeners();
        setAnswersButtonHashMap();
        getIntentData();
        setHeader();
        createQuiz();

        setOnBackPressedCallback();
    }

    private void assignViews() {
        headerTextView = findViewById(R.id.headerTextView);
        progressBar = findViewById(R.id.progressBar);
        progressTextView = findViewById(R.id.progressTextView);
        questionDetailTextView = findViewById(R.id.questionDetailTextView);
        questionDetailTextView.setMovementMethod(new ScrollingMovementMethod());
        answerButton1 = findViewById(R.id.answerButton1);
        answerButton2 = findViewById(R.id.answerButton2);
        answerButton3 = findViewById(R.id.answerButton3);
        nextButton = findViewById(R.id.nextButton);
    }

    private void addListeners() {
        answerButton1.setOnClickListener(new AnswerButtonListener(Answers.ONE));
        answerButton2.setOnClickListener(new AnswerButtonListener(Answers.TWO));
        answerButton3.setOnClickListener(new AnswerButtonListener(Answers.THREE));
    }

    private void setAnswersButtonHashMap() {
        answersButtonHashMap = new HashMap<Answers, Button>();
        answersButtonHashMap.put(Answers.ONE, answerButton1);
        answersButtonHashMap.put(Answers.TWO, answerButton2);
        answersButtonHashMap.put(Answers.THREE, answerButton3);
    }

    private void resetButtonColor() {
        answerButton1.setBackground(ContextCompat.getDrawable(this, R.drawable.button_answer_white));
        answerButton2.setBackground(ContextCompat.getDrawable(this, R.drawable.button_answer_white));
        answerButton3.setBackground(ContextCompat.getDrawable(this, R.drawable.button_answer_white));
    }

    private void setHeader() {
        headerTextView.setText(getString(R.string.header, name));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME_KEY);
        fileName = intent.getStringExtra(FILENAME_KEY);
        amount = intent.getIntExtra(AMOUNT_KEY,0);
    }

    private void createQuiz() {
        questions = getQuestions();
        progress = 1;
        correctAnswers = 0;
        currentAnswer = null;
        isSubmitted = false;

        updateProgressBar();
        resetUI();
    }

    private void nextQuestion() {
        if (progress < amount) {
            progress++;
            currentAnswer = null;
            isSubmitted = false;

            questions.remove(0);
            updateProgressBar();
            headerTextView.setText("");
            resetUI();
        } else {
            returnToMainActivity();
        }
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(RESULTS_KEY, correctAnswers);
        intent.putExtra(AMOUNT_KEY, amount);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateProgressBar() {
        progressTextView.setText(getString(R.string.progress, progress, amount));
        progressBar.setMax(amount);
        progressBar.setProgress(progress);
    }

    private void resetUI() {
        Question q = questions.get(0);
        questionDetailTextView.setText(HtmlCompat.fromHtml(q.question, HtmlCompat.FROM_HTML_MODE_LEGACY));
        answerButton1.setText(q.choices.get(0));
        answerButton2.setText(q.choices.get(1));
        answerButton3.setText(q.choices.get(2));
        resetButtonColor();
        nextButton.setText(R.string.submit);
    }

    private void onAnswerClicked(Answers _a) {
        if (isSubmitted) {
            return;
        }

        resetButtonColor();
        if (currentAnswer != _a) {
            currentAnswer = _a;
            answersButtonHashMap.get(currentAnswer).setBackground(ContextCompat.getDrawable(this, R.drawable.button_border_color));
        }
        else {
            currentAnswer = null;
        }
    }

    private void setOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // https://developer.android.com/guide/topics/ui/dialogs#java
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                builder.setMessage(R.string.warning_message).setTitle(R.string.warning_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                // with lambda
                builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dialog.cancel();
                });

                builder.create().show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void onNextButtonClicked(View view) {
        if (!isSubmitted) {
            if (currentAnswer == null) {
                // EMPTY
                Toast.makeText(this, "Please choose an answer.", Toast.LENGTH_SHORT).show();
                return;
            } else if (questions.get(0).answer == currentAnswer) {
                // CORRECT
                correctAnswers++;
                resetButtonColor();
                answersButtonHashMap.get(currentAnswer).setBackground(ContextCompat.getDrawable(this, R.drawable.button_answer_green));
            } else {
                // WRONG
                resetButtonColor();
                answersButtonHashMap.get(questions.get(0).answer).setBackground(ContextCompat.getDrawable(this, R.drawable.button_answer_green));
                answersButtonHashMap.get(currentAnswer).setBackground(ContextCompat.getDrawable(this, R.drawable.button_answer_red));
            }
            isSubmitted = true;
            nextButton.setText(R.string.next);
        }
        else {
            // NEXT QUESTION
            nextQuestion();
        }
    }

    private class Question {
        private String question;
        private ArrayList<String> choices;
        private Answers answer;

        public Question (String q, ArrayList<String> c, int a)
        {
            this.question = q;
            this.choices = c;
            switch (a) {
                case 0:
                    this.answer = Answers.ONE;
                    break;
                case 1:
                    this.answer = Answers.TWO;
                    break;
                case 2:
                    this.answer = Answers.THREE;
                    break;
            };
        }
    }

    private ArrayList<Question> getQuestions() {
        //https://abhiandroid.com/programming/json
        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named questions
            JSONArray questionsJSON = obj.getJSONArray("questions");
            // check which one is larger
            amount = Math.min(questionsJSON.length(), amount);
            // create a temporary variable of amount to allow while loop
            int _a = amount;
            // create Question ArrayList
            ArrayList<Question> list = new ArrayList<>();
            // loop the amount of questions to get and randomly fetch a question each time
            while (_a > 0) {
                // get a random integer between 0 and questionsJSON.length()
                Random rand = new Random();
                int randomInt = rand.nextInt(questionsJSON.length());
                //Log.d("randomInt", String.valueOf(randomInt));
                // create a JSONObject for fetching a single random question
                JSONObject questionDetail = questionsJSON.getJSONObject(randomInt);
                // fetch question, choices, and answer from the JSONObject
                ArrayList<String> _c = new ArrayList<>();

                // random the choices list
                JSONArray jsonArray = questionDetail.getJSONArray("choices");
                // create a temporary variable of jsonArray.length() to allow while loop
                int _b = jsonArray.length();
                // create a temporary variable to re-allocate new answer
                int _i = 0;
                // store the initial answer
                int _answer = questionDetail.getInt("answer");
                // do not re-allocate new answer if isFound = true
                boolean isFound = false;
                // loop
                while (_b > 0) {
                    int randomInt2 = rand.nextInt(_b);
                    _c.add(jsonArray.getString(randomInt2));
                    jsonArray.remove(randomInt2);
                    if (!isFound) {
                        if (randomInt2 == _answer) {
                            _answer = _i;
                            isFound = true;
                        } else if (randomInt2 < _answer) {
                            _answer--;
                        }
                    }
                    _i++;
                    _b--;
                }

                list.add(new Question(questionDetail.getString("question"), _c, _answer));
                questionsJSON.remove(randomInt);
                _a--;
            }
            // Return the list
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String loadJSONFromAsset() {
        //https://abhiandroid.com/programming/json
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private class AnswerButtonListener implements View.OnClickListener {
        Answers answer;

        AnswerButtonListener(Answers _a)
        {
            this.answer = _a;
        }

        @Override
        public void onClick(View v) {
            try {
                onAnswerClicked(this.answer);
            } catch (Exception e) {
                Toast.makeText(QuizActivity.this, "" + e, Toast.LENGTH_LONG).show();
            }
        }
    }
}
package com.example.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    Button subButton;
    Button divButton;
    Button mulButton;

    EditText num1;
    EditText num2;

    TextView resultText;

    private void assignResources() {
        addButton = findViewById(R.id.btn_plus);
        subButton = findViewById(R.id.btn_minus);
        divButton = findViewById(R.id.btn_divide);
        mulButton = findViewById(R.id.btn_multiply);

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);

        resultText = findViewById(R.id.result);
    }

    private void addListeners() {
        addButton.setOnClickListener(new CalcButtonListener("+"));
        subButton.setOnClickListener(new CalcButtonListener("-"));
        divButton.setOnClickListener(new CalcButtonListener("/"));
        mulButton.setOnClickListener(new CalcButtonListener("*"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignResources();
        addListeners();
    }

    private class CalcButtonListener implements View.OnClickListener {
        String op = "";

        CalcButtonListener(String op)
        {
            this.op = op;
        }

        @Override
        public void onClick(View v) {
            try {
                double result = 0;

                switch(op) {
                    case "+":
                        result = Double.parseDouble(num1.getText().toString()) + Double.parseDouble(num2.getText().toString());
                        break;
                    case "-":
                        result = Double.parseDouble(num1.getText().toString()) - Double.parseDouble(num2.getText().toString());
                        break;
                    case "/":
                        result = Double.parseDouble(num1.getText().toString()) / Double.parseDouble(num2.getText().toString());
                        break;
                    case "*":
                        result = Double.parseDouble(num1.getText().toString()) * Double.parseDouble(num2.getText().toString());
                        break;
                }
                resultText.setText(String.valueOf(result));
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this, "An error has occurred. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
package com.example.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddExpensesActivity extends AppCompatActivity {

    private TextView instructionsTextView;
    private EditText numberEditText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        assignViews();
        setActivityType();
    }

    private void assignViews() {
        instructionsTextView = findViewById(R.id.instructions_TextView);
        numberEditText = findViewById(R.id.numberEditText);
        numberEditText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7, 2)});
        imageView = findViewById(R.id.imageView);
    }

    private void setActivityType() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXPENSE_KEY);

        switch (message) {
            case "Home Rent":
                instructionsTextView.setText(R.string.home_rent_instructions);
                imageView.setImageResource(R.mipmap.cash_home);
                break;
            case "Eating Out":
                instructionsTextView.setText(R.string.eating_out_instructions);
                imageView.setImageResource(R.mipmap.eating_out);
                break;
            case "Travel":
                instructionsTextView.setText(R.string.travel_instructions);
                imageView.setImageResource(R.mipmap.train);
                break;
            case "Shopping":
                instructionsTextView.setText(R.string.shopping_instructions);
                imageView.setImageResource(R.mipmap.shopping);
                break;
        }
    }

    public void addExpensesAndReturn(View view) {
        String _s = numberEditText.getText().toString();
        double _value = 0f;

        try {
            if (!_s.isEmpty()) {
                _value = Double.parseDouble(_s);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.VALUE_KEY, _value);
                setResult(AddExpensesActivity.RESULT_OK, intent);
                finish();
            }
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number!", Toast.LENGTH_LONG).show();
        }
    }
}

class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;
    DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}
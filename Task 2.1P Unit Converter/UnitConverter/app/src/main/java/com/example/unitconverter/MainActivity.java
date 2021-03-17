package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText value;
    TextView[] units_value;
    TextView[] units_text;
    ImageButton length_btn;
    ImageButton temp_btn;
    ImageButton weight_btn;

    String type;
    String unit;

    List<String> unitsToConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();
        addListeners();
        type = "length";
        setSpinner();
    }

    private void assignViews() {
        spinner = (Spinner) findViewById(R.id.spinner);
        value = findViewById(R.id.value);
        units_value = new TextView[] {findViewById(R.id.unit1_value), findViewById(R.id.unit2_value), findViewById(R.id.unit3_value)};
        units_text = new TextView[] {findViewById(R.id.unit1_text), findViewById(R.id.unit2_text), findViewById(R.id.unit3_text)};
        length_btn = findViewById(R.id.length_btn);
        temp_btn = findViewById(R.id.temp_btn);
        weight_btn = findViewById(R.id.weight_btn);
    }

    private void addListeners() {
        length_btn.setOnClickListener(new UnitConverterButtonListener("length"));
        temp_btn.setOnClickListener(new UnitConverterButtonListener("temp"));
        weight_btn.setOnClickListener(new UnitConverterButtonListener("weight"));
        spinner.setOnItemSelectedListener(new SpinnerListener());

        value.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                try {
                    Calculate(Double.parseDouble(s.toString()));
                } catch (Exception e) {
                    setZero();
                }
            }
        });
    }

    private void Calculate(double num) {
        for (int i = 0; i < unitsToConvert.size(); i++) {
             units_value[i].setText(String.valueOf(Math.round(Conversion(num, unitsToConvert.get(i)) * 100.0) / 100.0));
        }
    }

    private double Conversion(double num, String toUnit) {
        double _temp = 0;
        if (type == "length") {
            switch (unit) {
                case "Metre":
                    _temp = num;
                    break;
                case "Centimetre":
                    _temp = num / 100;
                    break;
                case "Foot":
                    _temp = num / 3.28084;
                    break;
                case "Inch":
                    _temp = num / 39.3701;
                    break;
            }
            switch (toUnit) {
                case "Metre":
                    return _temp;
                case "Centimetre":
                    return _temp * 100;
                case "Foot":
                    return _temp * 3.28084;
                case "Inch":
                    return _temp * 39.3701;
            }
        } else if (type == "temp") {
            switch (unit) {
                case "Celsius":
                    _temp = num;
                    break;
                case "Fahrenheit":
                    _temp = (num - 32) * 5/9;
                    break;
                case "Kelvin":
                    _temp = (num - 273.15);
                    break;
            }
            switch (toUnit) {
                case "Celsius":
                    return _temp;
                case "Fahrenheit":
                    return (_temp * 9/5) + 32;
                case "Kelvin":
                    return (_temp + 273.15);
            }
        } else {
            switch (unit) {
                case "Kilogram":
                    _temp = num;
                    break;
                case "Gram":
                    _temp = num / 1000;
                    break;
                case "Ounce(Oz)":
                    _temp = num / 35.27396;
                    break;
                case "Pound(lb)":
                    _temp = num / 2.20462;
                    break;
            }
            switch (toUnit) {
                case "Kilogram":
                    return _temp;
                case "Gram":
                    return _temp * 1000;
                case "Ounce(Oz)":
                    return _temp * 35.27396;
                case "Pound(lb)":
                    return _temp * 2.20462;
            }
        }
        return 0;
    }

    private void setSpinner() {
        int array_id = 0;

        switch (type)
        {
            case "length":
                array_id = R.array.length_array;
                break;
            case "weight":
                array_id = R.array.weight_array;
                break;
            case "temp":
                array_id = R.array.temp_array;
                break;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, array_id, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Reference: https://developer.android.com/guide/topics/ui/controls/spinner#java
    }

    private class UnitConverterButtonListener implements View.OnClickListener {
        String type = "";

        UnitConverterButtonListener(String type)
        {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            try {
                setCurrentUnit(type);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setCurrentUnit(String type) {
        this.type = type;
        setSpinner();
        RefreshData(0);
    }

    private void setZero() {
        for (TextView textView : units_value) {
            textView.setText(R.string.zero_dec);
        }
    }

    private void RefreshData(long id) {
        switch (type)
        {
            case "length":
                unitsToConvert = Arrays.asList(getResources().getStringArray(R.array.length_array));
                break;
            case "temp":
                unitsToConvert = Arrays.asList(getResources().getStringArray(R.array.temp_array));
                break;
            case "weight":
                unitsToConvert = Arrays.asList(getResources().getStringArray(R.array.weight_array));
                break;
        }

        unitsToConvert = new ArrayList<String>(unitsToConvert);
        unit = unitsToConvert.get((int)id);
        unitsToConvert.remove((int)id);

        setZero();

        for (int i = 0; i < units_value.length; i++) {
            try {
                units_text[i].setText(unitsToConvert.get(i));
                units_value[i].setVisibility(View.VISIBLE);
                units_text[i].setVisibility(View.VISIBLE);
            } catch (Exception e) {
                units_value[2].setVisibility(View.INVISIBLE);
                units_text[2].setVisibility(View.INVISIBLE);
            }
        }

        if (!value.getText().toString().isEmpty()) {
            Calculate(Double.parseDouble(value.getText().toString()));
        }
    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            MainActivity.this.RefreshData(id);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
package com.example.expensemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXPENSE_KEY = "com.example.expensemanager.EXPENSE_TYPE";
    public static final String VALUE_KEY = "com.example.expensemanager.VALUE";
    public static final int LAUNCH_SECOND_ACTIVITY = 1;
    private ListView listView;
    private TextView totalTextView;
    private double expenses = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();
        addListeners();
        setListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == AddExpensesActivity.RESULT_OK){
                setExpenseTotal(data);
            }
        }
    }

    private void assignViews() {
        listView = findViewById(R.id.expensesListView);
        totalTextView = findViewById(R.id.totalTextView);
        totalTextView.setText(getString(R.string.expenses_total, 0f));
    }

    private void addListeners() {
        listView.setOnItemClickListener(new ListViewListener());
    }

    private void setListView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expenses, android.R.layout.simple_list_item_1);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.expense_list));
        listView.setAdapter(adapter);
    }

    private void setExpenseTotal(Intent data) {
        try {
            double _value = data.getDoubleExtra(MainActivity.VALUE_KEY, 0f);
            this.expenses += _value;
            totalTextView.setText(getString(R.string.expenses_total, expenses));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void createSecondActivity(String type) {
        Intent intent = new Intent(this, AddExpensesActivity.class);
        intent.putExtra(EXPENSE_KEY, type);
        startActivityForResult(intent, 1);
    }

    public class ListViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            createSecondActivity((String) parent.getItemAtPosition(pos));
        }
    }
}
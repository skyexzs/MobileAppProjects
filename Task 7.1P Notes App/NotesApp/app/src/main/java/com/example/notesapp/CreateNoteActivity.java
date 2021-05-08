package com.example.notesapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.data.DatabaseHelper;
import com.example.notesapp.model.Notes;
import com.example.notesapp.util.Util;

public class CreateNoteActivity extends AppCompatActivity {
    DatabaseHelper db;
    private EditText createTitleEditText;
    private EditText createNotesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        createTitleEditText = findViewById(R.id.createTitleEditText);
        createNotesEditText = findViewById(R.id.createNotesEditText);
        db = new DatabaseHelper(this);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle("Create");

        setOnBackPressedCallback();
    }

    public void onSaveClick(View view) {
        if (createTitleEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a title for the note!", Toast.LENGTH_SHORT).show();
        } else if (createNotesEditText.getText().toString().isEmpty()) {
            Util.createAlertDialogBuilder(this, "Confirm save?", "Are you sure you want to save an empty note?", (dialog, id) -> {
                saveNote();
            }).create().show();
        } else {
            saveNote();
        }
    }

    private void saveNote() {
        String title = createTitleEditText.getText().toString();
        String content = createNotesEditText.getText().toString();

        long result = db.insertNote(new Notes(title, content));
        if (result > 0) {
            Toast.makeText(this, "Successfully saved the note!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancelClick(View view) {
        if (!createNotesEditText.getText().toString().isEmpty()) {
            Util.createAlertDialogBuilder(this, "Confirm cancel?", "Unsaved changes will be lost! Are you sure you want to cancel?", (dialog, id) -> {
                finish();
            }).create().show();
        } else {
            finish();
        }
    }

    // https://www.geeksforgeeks.org/how-to-add-and-customize-back-button-of-action-bar-in-android/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onCancelClick(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onCancelClick(null);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
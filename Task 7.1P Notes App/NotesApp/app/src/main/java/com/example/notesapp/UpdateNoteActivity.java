package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.data.DatabaseHelper;
import com.example.notesapp.model.Notes;
import com.example.notesapp.util.Util;

public class UpdateNoteActivity extends AppCompatActivity {
    private int note_id;

    DatabaseHelper db;
    private EditText updateTitleEditText;
    private EditText updateNotesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        Intent intent = getIntent();
        note_id = intent.getIntExtra(Util.NOTE_ID, -1);
        if (note_id == -1) {
            Intent showIntent = new Intent(this, ShowNotesActivity.class);
            startActivity(showIntent);
        }

        updateTitleEditText = findViewById(R.id.updateTitleEditText);
        updateNotesEditText = findViewById(R.id.updateNotesEditText);
        db = new DatabaseHelper(this);

        fetchNote();

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle("Update");
    }

    private void fetchNote() {
        Notes note = db.fetchNote(note_id);

        updateTitleEditText.setText(note.getTitle());
        updateNotesEditText.setText(note.getContent());
    }

    public void onUpdateClick(View view) {
        if (updateTitleEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a title for the note!", Toast.LENGTH_SHORT).show();
        } else if (updateNotesEditText.getText().toString().isEmpty()) {
            Util.createAlertDialogBuilder(this, "Confirm update?", "Are you sure you want to save an empty note?", (dialog, id) -> {
                updateNote();
            }).create().show();
        } else {
            updateNote();
        }
    }

    private void updateNote() {
        String title = updateTitleEditText.getText().toString();
        String content = updateNotesEditText.getText().toString();

        boolean result = db.updateNote(new Notes(note_id, title, content));
        if (result) {
            Toast.makeText(this, "Successfully updated the note!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDeleteClick(View view) {
        Util.createAlertDialogBuilder(this, "Confirm delete?", "You cannot undo this action! Are you sure you want to delete?", (dialog, id) -> {
            deleteNote();
        }).create().show();
    }

    private void deleteNote() {
        int result = db.deleteNote(note_id);
        if (result > 0) {
            Toast.makeText(this, "Successfully deleted the note!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    // https://www.geeksforgeeks.org/how-to-add-and-customize-back-button-of-action-bar-in-android/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
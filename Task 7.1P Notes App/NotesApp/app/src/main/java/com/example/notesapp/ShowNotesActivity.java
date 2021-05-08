package com.example.notesapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.notesapp.data.DatabaseHelper;
import com.example.notesapp.model.Notes;
import com.example.notesapp.util.Util;

import java.util.List;

public class ShowNotesActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener {
    DatabaseHelper db;
    private List<Notes> notesList;
    private RecyclerView notesRecyclerView;
    private LinearLayout noNoteLinearLayout;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        noNoteLinearLayout = findViewById(R.id.noNoteLinearLayout);
        db = new DatabaseHelper(this);
        notesList = db.fetchAllNotes();

        if (notesList != null) {
            setNotesAdapter();
            notesRecyclerView.setVisibility(View.VISIBLE);
            noNoteLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            noNoteLinearLayout.setVisibility(View.VISIBLE);
            notesRecyclerView.setVisibility(View.INVISIBLE);
        }

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle("Notes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        notesList = db.fetchAllNotes();

        if (notesList != null) {
            setNotesAdapter();
            notesRecyclerView.setVisibility(View.VISIBLE);
            noNoteLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            noNoteLinearLayout.setVisibility(View.VISIBLE);
            notesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void setNotesAdapter() {
        notesAdapter = new NotesAdapter(notesList, this, this);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void onCreateNoteClick(View view) {
        Intent createNoteIntent = new Intent(this, CreateNoteActivity.class);
        startActivityForResult(createNoteIntent, 0);
    }

    @Override
    public void onNoteClick(int position) {
        Intent updateIntent = new Intent(this, UpdateNoteActivity.class);
        updateIntent.putExtra(Util.NOTE_ID, notesList.get(position).getNote_id());
        startActivityForResult(updateIntent, 0);
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
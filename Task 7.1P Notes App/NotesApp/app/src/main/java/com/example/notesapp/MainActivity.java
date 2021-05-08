package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCreateNoteClick(View view) {
        Intent createNoteIntent = new Intent(MainActivity.this, CreateNoteActivity.class);
        startActivity(createNoteIntent);
    }

    public void onShowNotesClick(View view) {
        Intent showNotesIntent = new Intent(MainActivity.this, ShowNotesActivity.class);
        startActivity(showNotesIntent);
    }
}
package com.example.notesapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notesapp.model.Notes;
import com.example.notesapp.util.Util;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.TITLE + " TEXT, " + Util.CONTENT + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_USER_TABLE);

        onCreate(sqLiteDatabase);
    }

    public long insertNote(Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TITLE, notes.getTitle());
        contentValues.put(Util.CONTENT, notes.getContent());

        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public boolean updateNote (Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TITLE, note.getTitle());
        contentValues.put(Util.CONTENT, note.getContent());

        int updRowId = db.update(Util.TABLE_NAME, contentValues, Util.NOTE_ID + "=?", new String[] {String.valueOf(note.getNote_id())});
        return true;
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delRowId = db.delete(Util.TABLE_NAME, Util.NOTE_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
        return delRowId;
    }

    public Notes fetchNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, null, Util.NOTE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst(); // IMPORTANT

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            int note_id = cursor.getInt(cursor.getColumnIndex(Util.NOTE_ID));
            String title = cursor.getString(cursor.getColumnIndex(Util.TITLE));
            String content = cursor.getString(cursor.getColumnIndex(Util.CONTENT));
            Notes note = new Notes(note_id, title, content);
            return note;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public ArrayList<Notes> fetchAllNotes() {
        ArrayList<Notes> notes_list = new ArrayList<Notes>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, null, null,
                null, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            while (!cursor.isAfterLast()){
                int note_id = cursor.getInt(cursor.getColumnIndex(Util.NOTE_ID));
                String title = cursor.getString(cursor.getColumnIndex(Util.TITLE));
                String content = cursor.getString(cursor.getColumnIndex(Util.CONTENT));
                notes_list.add(new Notes(note_id, title, content));
                cursor.moveToNext();
            }
            return notes_list;
        } finally {
            cursor.close();
            db.close();
        }
    }
}

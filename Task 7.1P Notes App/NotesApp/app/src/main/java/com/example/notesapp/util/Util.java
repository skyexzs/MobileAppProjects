package com.example.notesapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Util {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes_db";
    public static final String TABLE_NAME = "notes";

    public static final String NOTE_ID = "note_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    public static AlertDialog.Builder createAlertDialogBuilder(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("Yes", listener);
        // with lambda
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
        });

        return builder;
    }
}

package com.example.foodrescueapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.foodrescueapp.MainActivity;
import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.model.User;

public class Authenticator {
    public static void checkAuthentication(Context context) {
        UserDatabase userDb = new UserDatabase(context);
        SharedPreferences sharedPrefs = context.getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, Context.MODE_PRIVATE);

        String token = sharedPrefs.getString(Util.SPKey.LOGIN_TOKEN, "");

        if (token.isEmpty() || userDb.fetchUserFromToken(token) == null) {
            Toast.makeText(context, "You are not logged in!", Toast.LENGTH_SHORT).show();
            signOut(context);
        }
    }

    public static String getUserEmail(Context context) {
        UserDatabase userDb = new UserDatabase(context);
        SharedPreferences sharedPrefs = context.getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, Context.MODE_PRIVATE);

        String token = sharedPrefs.getString(Util.SPKey.LOGIN_TOKEN, "");
        if (!token.isEmpty() && userDb.fetchUserFromToken(token) != null) {
            return userDb.fetchEmailFromToken(token);
        } else {
            return null;
        }
    }

    public static void signOut(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();

        Intent loginIntent = new Intent(context, MainActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }
}

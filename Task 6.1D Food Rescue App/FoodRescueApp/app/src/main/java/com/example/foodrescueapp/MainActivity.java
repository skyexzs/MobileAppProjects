package com.example.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.util.HashGenerator;
import com.example.foodrescueapp.util.TokenGeneration;
import com.example.foodrescueapp.util.Util;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText loginEmailEditText;
    private EditText loginPassEditText;
    private CheckBox loginRememberCheckBox;

    private UserDatabase userDb;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.checkReadPermission(this);

        assignViews();
        userDb = new UserDatabase(this);
        sharedPrefs = getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, MODE_PRIVATE);
        checkAutoLogin();
    }

    private void assignViews() {
        loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginPassEditText = findViewById(R.id.loginPassEditText);
        loginRememberCheckBox = findViewById(R.id.loginRememberCheckBox);
    }

    private void checkAutoLogin() {
        String token = sharedPrefs.getString(Util.SPKey.LOGIN_TOKEN, "");
        if (sharedPrefs.getBoolean(Util.SPKey.REMEMBER, false) && !token.isEmpty() && userDb.checkIfTokenExists(token)) {
            goToHomeActivity();
        } else {
            sharedPrefs.edit().remove(Util.SPKey.LOGIN_TOKEN).apply();
            sharedPrefs.edit().remove(Util.SPKey.REMEMBER).apply();
        }
    }

    public void onLoginClick(View view) {
        String email = loginEmailEditText.getText().toString();
        String hashedPass = HashGenerator.getSha256Hash(loginPassEditText.getText().toString());

        if (userDb.checkEmailAndPassword(email, hashedPass)) {
            Random random = new Random();
            String token = String.valueOf(TokenGeneration.getToken(String.valueOf(random.nextInt())));
            while (userDb.checkIfTokenExists(token)) {
                token = String.valueOf(TokenGeneration.getToken(String.valueOf(random.nextInt())));
            }
            userDb.updateToken(email, token);
            sharedPrefs.edit().putString(Util.SPKey.LOGIN_TOKEN, token).putBoolean(Util.SPKey.REMEMBER, loginRememberCheckBox.isChecked()).apply();

            goToHomeActivity();
        } else {
            Toast.makeText(this, "Your e-mail or password is incorrect!", Toast.LENGTH_SHORT).show();
        }

        /*
        Intent testIntent = new Intent(this, TestActivity.class);
        startActivity(testIntent);
         */
    }

    /*
    public void resetData() {
        deleteDatabase(Util.DATABASE_NAME);
        sharedPrefs.edit().clear().apply();
    }
    */

    public void onSignUpClick(View view) {
        Intent signUpIntent = new Intent(this, SignupActivity.class);
        startActivity(signUpIntent);
    }

    private void goToHomeActivity() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }
}
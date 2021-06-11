package com.example.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.model.User;
import com.example.foodrescueapp.util.HashGenerator;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private UserDatabase userDb;

    private EditText signUpNameEditText;
    private EditText signUpEmailEditText;
    private EditText signUpPhoneEditText;
    private EditText signUpAddressEditText;
    private EditText signUpPassEditText;
    private EditText signUpConfPassEditText;

    private final String emailRegEx = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private final String phoneRegEx = "^((\\+61\\s?)?(\\((0|02|03|04|07|08)\\))?)?\\s?\\d{1,4}\\s?\\d{1,4}\\s?\\d{0,4}$";
    private final String addressRegEx = "([0-9]+[/]?)([0-9]+[-]?[0-9]*[a-zA-Z]?)\\s([a-zA-Z]{1}.*)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        assignViews();
        userDb = new UserDatabase(this);
    }

    private void assignViews() {
        signUpNameEditText = findViewById(R.id.signUpNameEditText);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditText);
        signUpPhoneEditText = findViewById(R.id.signUpPhoneEditText);
        signUpAddressEditText = findViewById(R.id.signUpAddressEditText);
        signUpPassEditText = findViewById(R.id.signUpPassEditText);
        signUpConfPassEditText = findViewById(R.id.signUpConfPassEditText);
    }

    public void onSaveClick(View view) {
        String fullName = signUpNameEditText.getText().toString();
        String email = signUpEmailEditText.getText().toString();
        String phone = signUpPhoneEditText.getText().toString();
        String address = signUpAddressEditText.getText().toString();
        String password = signUpPassEditText.getText().toString();
        String confPass = signUpConfPassEditText.getText().toString();

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Pattern.compile(emailRegEx).matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid e-mail!", Toast.LENGTH_SHORT).show();
            return;
        } else if (userDb.checkIfEmailExists(email)) {
            Toast.makeText(this, "That email is already in-use!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Pattern.compile(phoneRegEx).matcher(phone).matches()) {
            Toast.makeText(this, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Pattern.compile(addressRegEx).matcher(address).matches()) {
            Toast.makeText(this, "Please enter a valid address!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 4) {
            Toast.makeText(this, "Password must be at least 4 characters long!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(confPass)) {
            Toast.makeText(this, "Password does not match the confirm password!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String hashedPass = HashGenerator.getSha256Hash(password);
            User user = new User(email, hashedPass, fullName, phone, address);
            long result = userDb.insertUser(user);
            if (result < 0) {
                Toast.makeText(this, "An error has occurred! Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successfully created user!", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            }
        }
    }
}
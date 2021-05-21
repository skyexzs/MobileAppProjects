package com.example.foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.foodrescueapp.data.FoodDatabase;
import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.model.FoodData;
import com.example.foodrescueapp.util.Authenticator;
import com.example.foodrescueapp.util.Util;

import java.util.ArrayList;

public class MyListActivity extends AppCompatActivity implements FoodAdapter.OnFoodClickListener {
    private UserDatabase userDb;
    private FoodDatabase foodDb;
    private SharedPreferences sharedPrefs;

    private RecyclerView myListRecyclerView;
    private FoodAdapter foodAdapter;

    private TextView myListNoItemTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        Authenticator.checkAuthentication(this);

        userDb = new UserDatabase(this);
        foodDb = new FoodDatabase(this);
        sharedPrefs = getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, MODE_PRIVATE);
        myListRecyclerView = findViewById(R.id.myListRecyclerView);
        myListNoItemTextView = findViewById(R.id.myListNoItemTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFoodAdapter();
    }

    private void setFoodAdapter() {
        String token = sharedPrefs.getString(Util.SPKey.LOGIN_TOKEN, "");
        String email = userDb.fetchEmailFromToken(token);

        ArrayList<FoodData> foodList = foodDb.fetchAllOwnedFoodData(email);
        if (foodList != null) {
            myListNoItemTextView.setVisibility(View.INVISIBLE);
            foodAdapter = new FoodAdapter(foodList, this, this, true);
            myListRecyclerView.setAdapter(foodAdapter);
            myListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            myListNoItemTextView.setVisibility(View.VISIBLE);
        }

    }

    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.settings_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeMenu:
                        Intent homeIntent = new Intent(MyListActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.accountMenu:
                        // intent
                        break;
                    case R.id.myListMenu:
                        // intent
                        break;
                    case R.id.signOutMenu:
                        Authenticator.signOut(MyListActivity.this);
                        break;
                    default:
                        return MyListActivity.super.onOptionsItemSelected(item);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void goToAddActivity(View view) {
        Intent addIntent = new Intent(this, AddFoodActivity.class);
        startActivity(addIntent);
    }

    @Override
    public void onFoodClick(int position) {

    }
}
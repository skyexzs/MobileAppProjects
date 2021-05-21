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
import android.widget.Toast;

import com.example.foodrescueapp.data.FoodDatabase;
import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.model.FoodData;
import com.example.foodrescueapp.util.Authenticator;
import com.example.foodrescueapp.util.Util;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements FoodAdapter.OnFoodClickListener {
    private UserDatabase userDb;
    private FoodDatabase foodDb;
    private SharedPreferences sharedPrefs;

    private RecyclerView homeRecyclerView;
    private FoodAdapter foodAdapter;

    private TextView homeNoItemTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Authenticator.checkAuthentication(this);

        userDb = new UserDatabase(this);
        foodDb = new FoodDatabase(this);
        sharedPrefs = getSharedPreferences(Util.SPKey.SHARED_PREFS_KEY, MODE_PRIVATE);
        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        homeNoItemTextView = findViewById(R.id.homeNoItemTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFoodAdapter();
    }

    private void setFoodAdapter() {
        ArrayList<FoodData> foodList = foodDb.fetchAllSharedFoodData();
        if (foodList != null) {
            homeNoItemTextView.setVisibility(View.INVISIBLE);
            foodAdapter = new FoodAdapter(foodList, this, this, false);
            homeRecyclerView.setAdapter(foodAdapter);
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            homeNoItemTextView.setVisibility(View.VISIBLE);
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
                        break;
                    case R.id.accountMenu:
                        // intent
                        break;
                    case R.id.myListMenu:
                        Intent myListIntent = new Intent(HomeActivity.this, MyListActivity.class);
                        startActivity(myListIntent);
                        break;
                    case R.id.signOutMenu:
                        Authenticator.signOut(HomeActivity.this);
                        break;
                    default:
                        return HomeActivity.super.onOptionsItemSelected(item);
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

    /*
    private void Test() {
        TextView homeTitleTextView = findViewById(R.id.homeTitleTextView);
        homeTitleTextView.setText("Hello, " + userDb.fetchUserFromToken(sharedPrefs.getString(Util.SPKey.LOGIN_TOKEN, "")).getFullName());
    }*/

}
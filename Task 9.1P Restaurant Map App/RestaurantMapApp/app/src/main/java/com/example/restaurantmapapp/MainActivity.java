package com.example.restaurantmapapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String LAT_LNG_DICT = "LAT_LNG_DICT";
    HashMap<String, LatLng> latLngDict = new HashMap<String, LatLng>();

    ActivityResultLauncher<Intent> intentLauncher;
    LatLng chosenLatLng = null;
    String chosenPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            chosenPlaceName = data.getStringExtra(AddActivity.PLACE_NAME);
                            chosenLatLng = (LatLng) data.getParcelableExtra(AddActivity.LAT_LNG);

                            if (chosenLatLng != null) {
                                latLngDict.put(chosenPlaceName, chosenLatLng);
                            }

                            return;
                        }
                    }
                });
    }

    public void onAddButtonClick(View view) {
        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        intentLauncher.launch(addIntent);
    }

    public void onShowButtonClick(View view) {
        if (latLngDict != null && !latLngDict.isEmpty()) {
            Intent mapsIntent = new Intent(this, MapsActivity.class);
            mapsIntent.putExtra(LAT_LNG_DICT, latLngDict);
            startActivity(mapsIntent);
        } else {
            Toast.makeText(this, "Please add at least one location.", Toast.LENGTH_LONG).show();
        }
    }
}
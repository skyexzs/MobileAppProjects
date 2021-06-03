package com.example.restaurantmapapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    public static final String PLACE_NAME = "PLACE_NAME";
    public static final String LAT_LNG = "LAT_LNG";

    private static final String TAG = "AddActivity";

    ActivityResultLauncher<Intent> intentLauncher;
    LocationManager locationManager;
    LocationListener locationListener;
    TextView addPlaceNameTextView;
    TextView locationTextView;
    LatLng chosenLatLng = null;
    LatLng currentLatLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addPlaceNameTextView = findViewById(R.id.addPlaceNameTextView);
        locationTextView = findViewById(R.id.locationTextView);

        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.API_KEY));
        }

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        //https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes

                            Place place = Autocomplete.getPlaceFromIntent(data);
                            chosenLatLng = place.getLatLng();
                            locationTextView.setText(chosenLatLng.toString());

                            return;
                        } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                            // TODO: Handle the error.
                            Status status = Autocomplete.getStatusFromIntent(data);
                            Log.i(TAG, status.getStatusMessage());
                        }
                    }
                });

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void onGetCurrentLocationClick(View view) {
        if (currentLatLng == null) {
            Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        chosenLatLng = currentLatLng;
        locationTextView.setText(chosenLatLng.toString());
    }

    public void onLocationTextClick(View view) {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        intentLauncher.launch(intent);
    }

    public void onShowClick(View view) {
        if (chosenLatLng != null) {
            Intent mapsIntent = new Intent(this, MapsActivity.class);
            mapsIntent.putExtra(PLACE_NAME, addPlaceNameTextView.getText().toString());
            mapsIntent.putExtra(LAT_LNG, chosenLatLng);
            startActivity(mapsIntent);
        } else {
            Toast.makeText(this, "Please choose a valid location.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveClick(View view) {
        if (addPlaceNameTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a name for the location.", Toast.LENGTH_SHORT).show();
        } else if (chosenLatLng != null) {
            Toast.makeText(this, "Location successfully added!", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra(PLACE_NAME, addPlaceNameTextView.getText().toString());
            mainIntent.putExtra(LAT_LNG, chosenLatLng);
            setResult(Activity.RESULT_OK, mainIntent);
            finish();
        } else {
            Toast.makeText(this, "Please choose a valid location.", Toast.LENGTH_SHORT).show();
        }
    }
}
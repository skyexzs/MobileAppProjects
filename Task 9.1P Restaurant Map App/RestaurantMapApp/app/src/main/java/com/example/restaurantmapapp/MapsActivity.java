package com.example.restaurantmapapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private HashMap<String, LatLng> latLngDict = null;
    private LatLng chosenLatLng;
    private String chosenPlaceName;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            latLngDict = (HashMap<String, LatLng>) intent.getSerializableExtra(MainActivity.LAT_LNG_DICT);
            chosenLatLng = (LatLng) intent.getParcelableExtra(AddActivity.LAT_LNG);
            chosenPlaceName = intent.getStringExtra(AddActivity.PLACE_NAME);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (latLngDict == null || latLngDict.isEmpty()) {
            if (chosenLatLng != null) {
                mMap.addMarker(new MarkerOptions().position(chosenLatLng).title(chosenPlaceName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLatLng, 15f));
            }
        } else {
            List<Marker> markers = new ArrayList<Marker>();

            latLngDict.forEach((s, l) -> {
                Marker m = mMap.addMarker(new MarkerOptions().position(l).title(s)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markers.add(m);
            });

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            CameraUpdate cu;
            if (markers.size() == 1) {
                cu = CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 15F);
            } else {
                int padding = 200; // offset from edges of the map in pixels
                cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            }

            googleMap.animateCamera(cu);
        }
    }
}
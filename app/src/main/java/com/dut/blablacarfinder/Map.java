package com.dut.blablacarfinder;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dut.blablacarfinder.databinding.ActivityMapBinding;

import java.util.ArrayList;

public class Map<S, O> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    double[] location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        location = getIntent().getDoubleArrayExtra(MainActivity.INTENT_LOCATION);

        LatLng userPos = new LatLng(location[0], location[1]);
        mMap.addMarker(new MarkerOptions().position(userPos).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
        mMap.animateCamera( CameraUpdateFactory.zoomTo(10f));
    }
}
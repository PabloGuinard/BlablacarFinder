package com.dut.blablacarfinder;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dut.blablacarfinder.databinding.ActivityMapBinding;

import java.util.ArrayList;

public class Map<S, O> extends FragmentActivity implements OnMapReadyCallback, ApiInterface, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    double[] area;
    ArrayList<Point> pointsList;
    private CameraPosition oldPosition;
    private ConstraintLayout infosContainer;
    private ActivityMapBinding binding;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        infosContainer = findViewById(R.id.infos_container);
        infosContainer.setVisibility(View.GONE);
        infosContainer.setBackgroundColor(R.color.white);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        pointsList = (ArrayList<Point>) getIntent().getSerializableExtra(MainActivity.INTENT_POINTSLIST);
        area = getIntent().getDoubleArrayExtra(MainActivity.INTENT_LOCATION);

        //user's marker
        LatLng userPos = new LatLng(area[0], area[1]);
        mMap.addMarker(new MarkerOptions()
                .position(userPos)
                .title("You are here")
                .zIndex(2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                infosContainer.setVisibility(View.GONE);
            }
        });
        mMap.setMinZoomPreference(10f);
        oldPosition = mMap.getCameraPosition();

        //add markers
        addMarkers(pointsList);

        //request new data
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition newPosition = mMap.getCameraPosition();

                if(Math.abs(oldPosition.target.latitude - newPosition.target.latitude)
                        + Math.abs(oldPosition.target.latitude - newPosition.target.latitude) > 0.1){
                    area[0] = newPosition.target.latitude;
                    area[1] = newPosition.target.longitude;
                    new APIAsyncTask().execute(area, Map.this, pointsList, Map.this.getBaseContext());
                    oldPosition = newPosition;
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        for (int cpt = 0; cpt < pointsList.size(); cpt ++){
            if(pointsList.get(cpt).placeName.equals(marker.getTitle())){
                infosContainer.setVisibility(View.VISIBLE);
                setInfos(pointsList.get(cpt));
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    private void setInfos(Point point){
        TextView tv = findViewById(R.id.tv_place_name);
        tv.setText(point.placeName);
        tv = findViewById(R.id.tv_address_1);
        tv.setText(point.address);
        tv = findViewById(R.id.tv_address_2);
        tv.setText(point.city + " " + point.code);
        tv = findViewById(R.id.tv_distance);
        tv.setText(point.distanceFromUser + " meters");
        tv = findViewById(R.id.tv_nb_place);
        tv.setText(point.nbPlaces + "");
    }

    private void addMarkers(ArrayList<Point> pointsList){
        for (int cpt = 0; cpt < pointsList.size(); cpt++){
            LatLng position = new LatLng(pointsList.get(cpt).latitude, (pointsList.get(cpt).longitude));
            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title((pointsList.get(cpt).placeName)));

        }
    }

    @Override
    public void result(ArrayList<Point> pointsList) {
        addMarkers(pointsList);
        this.pointsList.addAll(pointsList);
    }
}
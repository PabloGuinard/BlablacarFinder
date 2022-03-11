package com.dut.blablacarfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    double[]area;
    public static final String INTENT_LOCATION = "location";
    public int AREA_RADIUS = 100000;
    public ArrayList<Point> pointsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_map = findViewById(R.id.bt_map);

        setLocation();
        new APIAsyncTask().execute(area, pointsArray);

        bt_map.setOnClickListener(view -> {
            Intent intent = new Intent(this, Map.class);
            intent.putExtra(INTENT_LOCATION, area);
            startActivity(intent);
        });
    }

    @SuppressLint("MissingPermission")
    private void setLocation(){
        Location location;
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider;
        Criteria criteria =new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        double[] area = new double[3];
        if(location != null){
            area[0] = location.getLatitude();
            area[1] = location.getLongitude();
            area[2] = AREA_RADIUS;
        } else {
            Log.e("error", "location null");
        }
        this.area = area;
    }
}
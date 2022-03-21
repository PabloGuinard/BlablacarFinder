package com.dut.blablacarfinder;

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
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ApiInterface {

    double[]area;
    public static final String INTENT_LOCATION = "location";
    public int AREA_RADIUS = 100000;

    Button btMap;
    ListView lvPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocation();
        new APIAsyncTask().execute(area, this);

        btMap = findViewById(R.id.bt_map);
        btMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, Map.class);
            intent.putExtra(INTENT_LOCATION, area);
            startActivity(intent);
        });

        lvPoints = findViewById(R.id.lv_main_page);
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

    @Override
    public void result(ArrayList<Point> pointsList) {
        lvPoints.setAdapter(new ListPointsAdapter(pointsList));
    }
}
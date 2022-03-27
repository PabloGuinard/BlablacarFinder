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

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity implements ApiInterface{

    public static final int AREA_RADIUS = 50000;
    public static final String INTENT_POINTS_LIST = "pointsList";
    public static final String INTENT_AREA = "area";

    ArrayList<Point> pointsList = new ArrayList<>();
    double[] area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setLocation();
        new APIAsyncTask().execute(area, this, pointsList, this.getBaseContext());
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(INTENT_POINTS_LIST, pointsList);
        intent.putExtra(INTENT_AREA, area);
        startActivity(intent);
        finish();
    }
}
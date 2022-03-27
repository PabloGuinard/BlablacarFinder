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
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ApiInterface {

    private double[]area;
    public static final String INTENT_LOCATION = "location";
    public static final String INTENT_POINTSLIST = "pointsList";
    public static final String INTENT_SELECTED_POINT = "selectedPoint";
    public int AREA_RADIUS = 50000;
    private ArrayList<Point> pointsList = new ArrayList<>();
    private ListPointsAdapter adapter;
    private boolean isRefreshing = false;
    private ProgressBar spinner;

    Button btMap;
    ListView lvPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocation();
        new APIAsyncTask().execute(area, this, pointsList, this.getBaseContext());
        isRefreshing = true;

        btMap = findViewById(R.id.bt_map);
        btMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, Map.class);
            intent.putExtra(INTENT_LOCATION, area);
            intent.putExtra(INTENT_POINTSLIST, pointsList);
            startActivity(intent);
        });


        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        lvPoints = findViewById(R.id.lv_main_page);
        lvPoints.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
                final int lastItem = firstVisible + visibleCount;
                if (lastItem == totalCount){
                    if(!isRefreshing){
                        new APIAsyncTask().execute(area, MainActivity.this, pointsList, MainActivity.this.getBaseContext());
                        spinner.setVisibility(View.VISIBLE);
                        isRefreshing = true;
                    }
                }
            }
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

    @Override
    public void result(ArrayList<Point> pointsList) {
        //first request
        this.pointsList.addAll(pointsList);
        if(lvPoints.getAdapter() == null){
            adapter = new ListPointsAdapter(this.pointsList, area);
            lvPoints.setAdapter(adapter);
        }
        adapter.refresh(this.pointsList);
        spinner.setVisibility(View.GONE);
        isRefreshing = false;
    }
}













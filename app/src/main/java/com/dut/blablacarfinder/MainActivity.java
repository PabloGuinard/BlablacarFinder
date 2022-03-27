package com.dut.blablacarfinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ApiInterface {

    public static final String INTENT_LOCATION = "location";
    public static final String INTENT_POINTSLIST = "pointsList";
    public static final String INTENT_SELECTED_POINT = "selectedPoint";
    public static final String INTENT_CONFIGURATION = "conf";
    public static final int CODE_SETTINGS = 1000;

    private double[]area;
    public int AREA_RADIUS = 50000;
    private ArrayList<Point> pointsList = new ArrayList<>();
    private ListPointsAdapter adapter;
    private boolean isRefreshing = false;
    private ProgressBar spinner;

    Button btMap, btSettings;
    ListView lvPoints;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLanguage(Settings.language);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocation();
        if(isNetworkAvailable()){
            new APIAsyncTask().execute(area, this, pointsList, this.getBaseContext());
        } else {

        }
        isRefreshing = true;

        btMap = findViewById(R.id.bt_map);
        btMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, Map.class);
            Resources res = getResources();
            Configuration conf = res.getConfiguration();
            intent.putExtra(INTENT_LOCATION, area);
            intent.putExtra(INTENT_POINTSLIST, pointsList);
            intent.putExtra(INTENT_CONFIGURATION, conf.locale);
            startActivity(intent);
        });

        btSettings = findViewById(R.id.bt_settings);
        btSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, Settings.class);
            startActivityForResult(intent, CODE_SETTINGS);
        });

        if(Settings.isDarkMode){
            @SuppressLint("ResourceType") ConstraintLayout layout = findViewById(R.id.lv_container);
            layout.setBackgroundColor(getColor(R.color.black));

        }

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
            adapter = new ListPointsAdapter(this);
            lvPoints.setAdapter(adapter);
        }
        adapter.refresh(this.pointsList);
        spinner.setVisibility(View.GONE);
        isRefreshing = false;
    }

    public ArrayList<Point> getPointsList() {
        return pointsList;
    }

    public double[] getArea() {
        return area;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_SETTINGS){
            if(resultCode == RESULT_OK){
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }


    private void setLanguage(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static String meterToKilometer(String meters){
        String result = meters.substring(0, meters.length() - 4);
        if(result.equals("")){
            result = "0";
        }
        return result;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}













package com.dut.blablacarfinder;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ListPointsAdapter extends BaseAdapter {
    private ArrayList<Point> pointsList;
    MainActivity activity;

    public ListPointsAdapter(MainActivity activity) {
        this.pointsList = activity.getPointsList();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pointsList.size();
    }

    @Override
    public Object getItem(int i) {
        return pointsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void refresh(ArrayList<Point> pointsList){
        this.pointsList = pointsList;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if(view == null){
            view = inflater.inflate(R.layout.list_points_adapter, viewGroup, false);
        }

        TextView tvPlaceName = view.findViewById(R.id.tv_place_name);
        tvPlaceName.setText(pointsList.get(i).placeName);
        SettingsActivity.setWithDarkMode(tvPlaceName);
        TextView tvDistance = view.findViewById(R.id.tv_address_popup);
        String text;
        if(SettingsActivity.isDistanceMeters) {
            text = pointsList.get(i).distanceFromUser + " " + view.getContext().getString(R.string.meters);
        } else {
            text = MainActivity.meterToKilometer(pointsList.get(i).distanceFromUser + "")
                    + " " + view.getContext().getString(R.string.kilometers);
        }
        SettingsActivity.setWithDarkMode(tvDistance);
        tvDistance.setText(text);
        View finalView = view;
        view.setOnClickListener(see -> {
            new PopupFragment().setPopup(pointsList.get(i), finalView.getContext(), activity);
        });
        return view;
    }


}

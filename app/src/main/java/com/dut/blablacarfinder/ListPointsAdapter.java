package com.dut.blablacarfinder;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ListPointsAdapter extends BaseAdapter {
    private ArrayList<Point> pointsList;

    public ListPointsAdapter(ArrayList<Point> pointsList) {
        this.pointsList = pointsList;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if(view == null){
            view = inflater.inflate(R.layout.list_points_adapter, viewGroup, false);
        }

        TextView tvPlaceName = view.findViewById(R.id.tv_place_name);
        tvPlaceName.setText(pointsList.get(i).placeName);
        TextView tvDistance = view.findViewById(R.id.tv_distance);
        String text = pointsList.get(i).distanceFromUser + " " + view.getContext().getString(R.string.meters);
        tvDistance.setText(text);
        View finalView = view;
        view.setOnClickListener(see -> {
            Log.e("button", "");
            PopupFragment popup = new PopupFragment(pointsList.get(i), finalView.getContext());
        });

        return view;
    }
}

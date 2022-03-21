package com.dut.blablacarfinder;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListPointsAdapter extends BaseAdapter {
    private final ArrayList<Point> pointsList;

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
        TextView tvAdress = view.findViewById(R.id.tv_address);
        tvAdress.setText(pointsList.get(i).address);
        TextView tvAdress2 = view.findViewById(R.id.tv_adresse_2);
        tvAdress2.setText(pointsList.get(i).city + " " + pointsList.get(i).code);
        TextView tvPlaces = view.findViewById(R.id.tv_places_amount);
        tvPlaces.setText(String.valueOf(pointsList.get(i).nbPlaces));
        TextView tvDistance = view.findViewById(R.id.tv_distance);
        String text = pointsList.get(i).distanceFromUser + " meters";
        tvDistance.setText(text);

        return view;
    }
}
